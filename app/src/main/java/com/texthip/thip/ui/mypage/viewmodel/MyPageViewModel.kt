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

data class MyPageUiState(
    val isLoading: Boolean = true,
    val profileImageUrl: String? = null,
    val nickname: String = "",
    val aliasName: String = "",
    val aliasColor: String = "#0XFFFFFF",
    val errorMessage: String? = null,
    val showLogoutDialog: Boolean = false,
    val isLogoutCompleted: Boolean = false
)
@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState = _uiState.asStateFlow()


    fun fetchMyPageInfo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            userRepository.getMyPageInfo()
                .onSuccess { data ->
                    data?.let {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                profileImageUrl = data.profileImageUrl,
                                nickname = data.nickname,
                                aliasName = data.aliasName,
                                aliasColor = data.aliasColor
                            )
                        }
                    }
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = exception.message) }
                }
        }
    }

    fun onLogoutClick() {
        _uiState.update { it.copy(showLogoutDialog = true) }
    }

    fun onDismissLogoutDialog() {
        _uiState.update { it.copy(showLogoutDialog = false) }
    }

    fun confirmLogout(context: Context) {
        viewModelScope.launch {
            tokenManager.clearTokens()
            // 2. 카카오 SDK에서 로그아웃
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Log.e("MyPageViewModel", "카카오 로그아웃 실패", error)
                } else {
                    Log.d("MyPageViewModel", "카카오 로그아웃 성공")
                }
            }

            // 3. 구글 SDK에서 로그아웃
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            GoogleSignIn.getClient(context, gso).signOut()

            _uiState.update { it.copy(showLogoutDialog = false, isLogoutCompleted = true) }
        }
    }
}