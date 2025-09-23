package com.texthip.thip.ui.common.alarmpage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.repository.NotificationRepository
import com.texthip.thip.ui.common.alarmpage.mock.NotificationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmUiState())
    val uiState: StateFlow<AlarmUiState> = _uiState.asStateFlow()

    private var nextCursor: String? = null
    private var isLastPage = false
    private var isLoadingData = false

    private fun updateState(update: (AlarmUiState) -> AlarmUiState) {
        _uiState.value = update(_uiState.value)
    }

    init {
        loadNotifications(reset = true)
    }

    fun loadNotifications(reset: Boolean = false) {
        if (isLoadingData && !reset) return
        if (isLastPage && !reset) return

        viewModelScope.launch {
            try {
                isLoadingData = true

                if (reset) {
                    updateState {
                        it.copy(
                            isLoading = true,
                            notifications = emptyList(),
                            hasMore = true
                        )
                    }
                    nextCursor = null
                    isLastPage = false
                } else {
                    updateState { it.copy(isLoadingMore = true) }
                }

                val type =
                    if (uiState.value.currentNotificationType == NotificationType.FEED_AND_ROOM) {
                        null
                    } else {
                        uiState.value.currentNotificationType.value
                    }

                repository.getNotifications(type, nextCursor)
                    .onSuccess { notificationListResponse ->
                        notificationListResponse?.let { response ->
                            val currentList =
                                if (reset) emptyList() else uiState.value.notifications
                            updateState {
                                it.copy(
                                    notifications = currentList + response.notifications,
                                    error = null,
                                    hasMore = !response.isLast
                                )
                            }
                            nextCursor = response.nextCursor
                            isLastPage = response.isLast
                        } ?: run {
                            updateState { it.copy(hasMore = false) }
                            isLastPage = true
                        }
                    }
                    .onFailure { exception ->
                        updateState { it.copy(error = exception.message) }
                    }
            } finally {
                isLoadingData = false
                updateState { it.copy(isLoading = false, isLoadingMore = false) }
            }
        }
    }

    fun loadMoreNotifications() {
        loadNotifications(reset = false)
    }

    fun refreshData() {
        loadNotifications(reset = true)
    }

    fun changeNotificationType(notificationType: NotificationType) {
        if (notificationType != uiState.value.currentNotificationType) {
            updateState { it.copy(currentNotificationType = notificationType) }
            loadNotifications(reset = true)
        }
    }
}