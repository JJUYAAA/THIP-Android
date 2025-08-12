package com.texthip.thip.ui.signin.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.auth.response.AuthResponse
import com.texthip.thip.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    data object Idle : LoginUiState // 초기 상태
    data object Loading : LoginUiState
    data class Success(val response: AuthResponse) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository // 실제로는 AuthRepository를 주입받아야 합니다.
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun kakaoLogin(context: Context) {
        viewModelScope.launch {
            _uiState.update { LoginUiState.Loading }

            //카카오 로그인부터 서버 통신까지
            authRepository.loginWithKakao(context)
                .onSuccess { response ->
                    if (response != null) {
                        _uiState.update { LoginUiState.Success(response) }
                    } else {
                        _uiState.update { LoginUiState.Error("서버로부터 응답을 받지 못했습니다.") }
                    }
                }
                .onFailure { throwable ->
                    Log.e("LoginViewModel", "Login failed: ${throwable.message}", throwable)
                    _uiState.update {
                        LoginUiState.Error(throwable.message ?: "알 수 없는 통신 오류가 발생했습니다.")
                    }
                }
        }
    }

    // 화면 이동 또는 에러 메시지 표시 후, 상태를 다시 초기화하여 이벤트가 중복 실행되는 것을 방지
    fun clearLoginState() {
        _uiState.update { LoginUiState.Idle }
    }
}