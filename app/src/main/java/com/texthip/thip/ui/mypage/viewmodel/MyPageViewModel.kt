package com.texthip.thip.ui.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val showLogoutDialog: Boolean = false
)
@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchMyPageInfo()
    }

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

    fun confirmLogout() {
        _uiState.update { it.copy(showLogoutDialog = false) }
        // TODO: 실제 로그아웃 로직 구현
    }
}