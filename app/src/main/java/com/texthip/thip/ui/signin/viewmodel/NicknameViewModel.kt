package com.texthip.thip.ui.signin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.R
import com.texthip.thip.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class NicknameUiState(
    val isLoading: Boolean = false,
    val nickname: String = "",
    val isVerified: Boolean? = null,
    val warningMessageResId: Int? = null,
    val errorMessage: String? = null,
    val navigateToNext: Boolean = false
)

@HiltViewModel
class NicknameViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NicknameUiState())
    val uiState = _uiState.asStateFlow()

    fun onNicknameChange(nickname: String) {
        // 닉네임 입력 시, 경고 메시지 초기화
        _uiState.update {
            it.copy(
                nickname = nickname,
                warningMessageResId = null
            )
        }
    }

    fun checkNickname() {
        if (_uiState.value.isLoading) return
        if (_uiState.value.nickname.isBlank()) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    warningMessageResId = null,
                    errorMessage = null
                )
            }

            userRepository.checkNickname(_uiState.value.nickname)
                .onSuccess { response ->
                    if (response?.isVerified == true) {
                        _uiState.update { it.copy(isLoading = false, navigateToNext = true) }
                    } else {
                        // 중복된 닉네임
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                warningMessageResId = R.string.nickname_warning
                            )
                        }
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "알 수 없는 오류가 발생했습니다."
                        )
                    }
                }
        }
    }
    fun onNavigated() {
        _uiState.update { it.copy(navigateToNext = false, errorMessage = null) }
    }
}