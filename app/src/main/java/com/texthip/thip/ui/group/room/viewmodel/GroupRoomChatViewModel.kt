package com.texthip.thip.ui.group.room.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.rooms.response.TodayCommentList
import com.texthip.thip.data.repository.RoomsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GroupRoomChatUiState(
    val greetings: List<TodayCommentList> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isLastPage: Boolean = false,
    val error: String? = null
)

enum class ToastType {
    DAILY_GREETING_LIMIT,
    FIRST_WRITE
}

sealed interface GroupRoomChatEvent {
    data object LoadMore : GroupRoomChatEvent
    data class ShowToast(val type: ToastType) : GroupRoomChatEvent
    data class ShowErrorToast(val message: String) : GroupRoomChatEvent
    data class DeleteGreeting(val attendanceCheckId: Int) : GroupRoomChatEvent
}

@HiltViewModel
class GroupRoomChatViewModel @Inject constructor(
    private val roomsRepository: RoomsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val roomId: Int = requireNotNull(savedStateHandle["roomId"])

    private val _uiState = MutableStateFlow(GroupRoomChatUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<GroupRoomChatEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var nextCursor: String? = null

    init {
        fetchDailyGreetings(isRefresh = true)
    }

    fun onEvent(event: GroupRoomChatEvent) {
        when (event) {
            is GroupRoomChatEvent.LoadMore -> fetchDailyGreetings()
            is GroupRoomChatEvent.DeleteGreeting -> deleteDailyGreeting(event.attendanceCheckId)
            else -> Unit
        }
    }

    private fun deleteDailyGreeting(attendanceCheckId: Int) {
        viewModelScope.launch {
            roomsRepository.deleteRoomsDailyGreeting(
                roomId = roomId,
                attendanceCheckId = attendanceCheckId
            ).onSuccess {
                fetchDailyGreetings(isRefresh = true)
            }.onFailure { throwable ->
                _eventFlow.emit(GroupRoomChatEvent.ShowErrorToast(throwable.message ?: "삭제에 실패했습니다."))
            }
        }
    }

    private fun fetchDailyGreetings(isRefresh: Boolean = false) {
        val currentState = _uiState.value
        if (currentState.isLoading || currentState.isLoadingMore || (currentState.isLastPage && !isRefresh)) return

        viewModelScope.launch {
            if (isRefresh) {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        greetings = emptyList(),
                        isLastPage = false
                    )
                }
                nextCursor = null
            } else {
                _uiState.update { it.copy(isLoadingMore = true) }
            }

            roomsRepository.getRoomsDailyGreeting(
                roomId = roomId,
                cursor = nextCursor
            ).onSuccess { response ->
                response?.let { data ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            greetings = if (isRefresh) data.todayCommentList else it.greetings + data.todayCommentList,
                            isLastPage = data.isLast
                        )
                    }
                    nextCursor = data.nextCursor
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = throwable.message
                    )
                }
            }
        }
    }

    fun postDailyGreeting(content: String) {
        if (content.isBlank()) return

        viewModelScope.launch {
            roomsRepository.postRoomsDailyGreeting(
                roomId = roomId,
                content = content
            ).onSuccess { response ->
                if (response != null) {
                    if (response.isFirstWrite) {
                        _eventFlow.emit(GroupRoomChatEvent.ShowToast(ToastType.FIRST_WRITE))
                    }
                    fetchDailyGreetings(isRefresh = true)
                }
            }.onFailure { throwable ->
                _eventFlow.emit(GroupRoomChatEvent.ShowToast(ToastType.DAILY_GREETING_LIMIT))
            }
        }
    }
}