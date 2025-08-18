package com.texthip.thip.ui.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.R
import com.texthip.thip.data.model.base.ThipApiFailureException
import com.texthip.thip.data.model.users.request.ProfileUpdateRequest
import com.texthip.thip.data.repository.UserRepository
import com.texthip.thip.ui.mypage.mock.RoleItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditProfileUiState(
    val isLoading: Boolean = true,
    val nickname: String = "",
    val roleCards: List<RoleItem> = emptyList(),
    val selectedIndex: Int = -1,
    val initialNickname: String = "",
    val initialSelectedIndex: Int = -1,
    val nicknameWarningMessageResId: Int? = null,
    val errorMessage: String? = null,
    val isSaveSuccess: Boolean = false
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadInitialProfile()
    }

    private fun loadInitialProfile() {
        viewModelScope.launch {
            val profileDeferred = async { userRepository.getMyPageInfo() }
            val aliasDeferred = async { userRepository.getAliasChoices() }

            val profileResult = profileDeferred.await()
            val aliasResult = aliasDeferred.await()

            if (profileResult.isSuccess && aliasResult.isSuccess) {
                val profileData = profileResult.getOrNull()
                val aliasResponse = aliasResult.getOrNull()

                val roleCards = aliasResponse?.aliasChoices?.map {
                    RoleItem(it.aliasName, it.categoryName, it.imageUrl, it.aliasColor)
                } ?: emptyList()

                // 서버에서 받아온 현재 닉네임과 역할을 설정
                val currentNickname = profileData?.nickname ?: ""
                val currentAliasName = profileData?.aliasName ?: ""
                val currentSelectedIndex = roleCards.indexOfFirst { it.genre == currentAliasName }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        nickname = currentNickname,
                        roleCards = roleCards,
                        selectedIndex = currentSelectedIndex,
                        initialNickname = currentNickname,
                        initialSelectedIndex = currentSelectedIndex
                    )
                }
            } else {
                // 둘 중 하나라도 실패하면 에러 메시지 표시
                val error = profileResult.exceptionOrNull() ?: aliasResult.exceptionOrNull()
                _uiState.update { it.copy(isLoading = false, errorMessage = error?.message) }
            }
        }
    }

    fun onNicknameChange(newNickname: String) {
        _uiState.update {
            it.copy(
                nickname = newNickname,
                nicknameWarningMessageResId = null
            )
        }
    }

    fun selectCard(index: Int) {
        _uiState.update { it.copy(selectedIndex = index) }
    }

    fun saveProfile() {
        viewModelScope.launch {
            val currentState = _uiState.value

            val selectedRole = currentState.roleCards.getOrNull(currentState.selectedIndex)
            if (selectedRole == null) {
                _uiState.update { it.copy(errorMessage = "역할을 선택해주세요.") }
                return@launch
            }

            val nicknameToSend: String? =
                if (currentState.nickname != currentState.initialNickname) {
                    currentState.nickname
                } else {
                    null
                }

            val request = ProfileUpdateRequest(
                nickname = nicknameToSend,
                aliasName = selectedRole.genre
            )

            userRepository.updateProfile(request)
                .onSuccess {
                    _uiState.update { it.copy(isSaveSuccess = true) }
                }
                .onFailure { exception ->
                    if (exception is ThipApiFailureException) {
                        when (exception.code) {
                            70004, 70005, 70006 -> {
                                val messageResId = when (exception.code) {
                                    70004 -> R.string.nickname_error_same
                                    70005 -> R.string.nickname_error_cooldown
                                    else -> R.string.nickname_error_duplicate
                                }
                                _uiState.update { it.copy(nicknameWarningMessageResId = messageResId) }
                            }

                            else -> {
                                _uiState.update { it.copy(errorMessage = exception.message) }
                            }
                        }
                    } else {
                        _uiState.update { it.copy(errorMessage = "네트워크 오류가 발생했습니다.") }
                    }
                }
        }
    }
    fun onSaveComplete() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }
}