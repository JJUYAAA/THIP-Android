package com.texthip.thip.ui.mypage.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.user.UserApiClient
import com.texthip.thip.data.manager.TokenManager
import com.texthip.thip.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DeleteAccountUiState(
    val isLoading: Boolean = false,
    val isDeleteCompleted: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeleteAccountUiState())
    val uiState = _uiState.asStateFlow()

    fun deleteAccount(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            // 1. 서버에 회원탈퇴 요청
            userRepository.deleteAccount()
                .onSuccess {
                    // 2. 서버 요청 성공 후 토큰 삭제
                    tokenManager.clearTokens()
                    
                    // 3. 카카오 SDK에서 연결 끊기
                    UserApiClient.instance.unlink { error ->
                        if (error != null) {
                            Log.e("DeleteAccountViewModel", "카카오 연결 끊기 실패", error)
                        } else {
                            Log.d("DeleteAccountViewModel", "카카오 연결 끊기 성공")
                        }
                    }
                    
                    // 4. 구글 SDK에서 로그아웃
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                    GoogleSignIn.getClient(context, gso).signOut()
                    
                    _uiState.update { it.copy(isLoading = false, isDeleteCompleted = true) }
                }
                .onFailure { exception ->
                    Log.e("DeleteAccountViewModel", "회원탈퇴 실패", exception)
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            errorMessage = exception.message ?: "회원탈퇴에 실패했습니다."
                        ) 
                    }
                }
        }
    }
}