package com.texthip.thip.ui.signin.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.auth.response.AuthResponse
import com.texthip.thip.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Success(val response: AuthResponse) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
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
           /* //신규유저 아닌 경우 피드화면으로 테스트
            val fakeToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjcsImlhdCI6MTc1NDM4MjY1MiwiZXhwIjoxNzU2OTc0NjUyfQ.5CrcGkff5rcwF25qSsw8BY_GZ4W9w7QMJ6kXlwW4Ub0"
            val fakeResponse = AuthResponse(token = fakeToken, isNewUser = false)
            _uiState.update { LoginUiState.Success(fakeResponse) }
*/
        }
    }

    fun googleLogin(idToken: String){
        viewModelScope.launch {
            _uiState.update { LoginUiState.Loading }

            //구글 로그인부터 서버 통신까지
            authRepository.loginWithGoogle(idToken)
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

    //상태를 다시 초기화-> 이벤트 중복 실행 방지
    fun clearLoginState() {
        _uiState.update { LoginUiState.Idle }
    }
}