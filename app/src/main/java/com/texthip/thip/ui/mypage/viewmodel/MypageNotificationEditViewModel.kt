package com.texthip.thip.ui.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MypageNotificationEditUiState(
    val isLoading: Boolean = true,
    val isNotificationEnabled: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class MypageNotificationEditViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MypageNotificationEditUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchNotificationEnableState()
    }

    private fun updateState(update: (MypageNotificationEditUiState) -> MypageNotificationEditUiState) {
        _uiState.value = update(_uiState.value)
    }

    fun fetchNotificationEnableState() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            notificationRepository.getNotificationEnableState()
                .onSuccess { data ->
                    data?.let {
                        updateState {
                            it.copy(
                                isLoading = false,
                                isNotificationEnabled = data.isEnabled,
                                errorMessage = null
                            )
                        }
                    } ?: run {
                        updateState { 
                            it.copy(
                                isLoading = false,
                                errorMessage = "알림 설정 정보를 가져올 수 없습니다."
                            )
                        }
                    }
                }
                .onFailure { exception ->
                    updateState { 
                        it.copy(
                            isLoading = false, 
                            errorMessage = exception.message
                        )
                    }
                }
        }
    }

    fun onNotificationToggle(enabled: Boolean) {
        updateState { it.copy(isNotificationEnabled = enabled) }
        // TODO: 서버에 알림 설정 업데이트 API 호출 (향후 구현)
    }
}