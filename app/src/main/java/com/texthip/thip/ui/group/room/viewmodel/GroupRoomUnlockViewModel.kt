package com.texthip.thip.ui.group.room.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.repository.RoomsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GroupRoomUnlockUiState(
    val isLoading: Boolean = false,
    val passwordMatched: Boolean? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class GroupRoomUnlockViewModel @Inject constructor(
    private val roomsRepository: RoomsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupRoomUnlockUiState())
    val uiState = _uiState.asStateFlow()
    
    private var passwordCheckJob: Job? = null

    fun checkPassword(roomId: Int, password: String) {
        // 이전 요청이 있다면 취소
        passwordCheckJob?.cancel()
        
        // 이미 요청 중이면 중복 실행 방지
        if (_uiState.value.isLoading) return

        passwordCheckJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            roomsRepository.postParticipateSecreteRoom(roomId, password)
                .onSuccess { response ->
                    // API 호출은 성공했고, 응답 바디의 matched 값으로 상태 업데이트
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            passwordMatched = response.matched
                        )
                    }
                }
                .onFailure { throwable ->
                    // API 호출 자체 실패 (네트워크 오류 등)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            passwordMatched = false, // 실패 시 불일치로 간주
                            errorMessage = throwable.message ?: "알 수 없는 오류가 발생했습니다."
                        )
                    }
                }
        }
    }

    fun resetPasswordState() {
        _uiState.update { it.copy(passwordMatched = null, errorMessage = null) }
    }
    
    /**
     * 화면 진입 시 상태 완전 초기화
     * Screen에서 LaunchedEffect로 호출
     */
    fun initializeState() {
        passwordCheckJob?.cancel()
        passwordCheckJob = null
        _uiState.value = GroupRoomUnlockUiState()
    }
    
    override fun onCleared() {
        super.onCleared()
        // 진행 중인 작업 취소 및 리소스 정리
        passwordCheckJob?.cancel()
        passwordCheckJob = null
    }
}