package com.texthip.thip.ui.signin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.repository.UserRepository
import com.texthip.thip.ui.mypage.mock.RoleItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.onSuccess

data class SignupAliasUiState(
    val isLoading: Boolean = false,
    val roleCards: List<RoleItem> = emptyList(),
    val selectedIndex: Int = -1,
    val errorMessage: String? = null
)

@HiltViewModel
class SignupAliasViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupAliasUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchAliasChoices()
    }

    fun fetchAliasChoices() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            userRepository.getAliasChoices()
                .onSuccess { response ->
                    val roleCards = response?.aliasChoices?.map { aliasChoice ->
                        RoleItem(
                            genre = aliasChoice.aliasName,
                            role = aliasChoice.categoryName,
                            imageUrl = aliasChoice.imageUrl,
                            roleColor = aliasChoice.aliasColor
                        )
                    } ?: emptyList()
                    _uiState.update { it.copy(isLoading = false, roleCards = roleCards) }
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = exception.message) }
                }
        }
    }

    fun selectCard(index: Int) {
        _uiState.update { it.copy(selectedIndex = index) }
    }
}

