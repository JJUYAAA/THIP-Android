package com.texthip.thip.ui.group.note.viewmodel

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.repository.RoomsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GroupNoteCreateUiState(
    val pageText: String = "",
    val opinionTextFieldValue: TextFieldValue = TextFieldValue(""),
    val isEditMode: Boolean = false,
    val isGeneralReview: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val totalPage: Int = 0,
    val isOverviewPossible: Boolean = false
) {
    // 입력 폼이 모두 채워졌는지 확인
    val isFormFilled: Boolean
        get() = (pageText.isNotBlank() || isGeneralReview) && opinionTextFieldValue.text.isNotBlank()
}

sealed interface GroupNoteCreateEvent {
    data class PageChanged(val text: String) : GroupNoteCreateEvent
    data class OpinionChanged(val newTextFieldValue: TextFieldValue) : GroupNoteCreateEvent
    data class GeneralReviewToggled(val isChecked: Boolean) : GroupNoteCreateEvent
    data object CreateRecordClicked : GroupNoteCreateEvent
}

@HiltViewModel
class GroupNoteCreateViewModel @Inject constructor(
    private val roomsRepository: RoomsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(GroupNoteCreateUiState())
    val uiState = _uiState.asStateFlow()

    private var roomId: Int = -1
    private var postIdToEdit: Int? = null // 수정할 포스트 ID 저장

    fun initialize(
        roomId: Int,
        recentPage: Int,
        totalPage: Int,
        isOverviewPossible: Boolean,
        postId: Int?,
        page: Int?,
        content: String?,
        isOverview: Boolean?
    ) {
        this.roomId = roomId
        _uiState.update {
            it.copy(
                pageText = recentPage.toString(),
                totalPage = totalPage,
                isOverviewPossible = isOverviewPossible
            )
        }

        // postId가 null이 아니면 수정 모드로 진입
        if (postId != null && page != null && content != null && isOverview != null) {
            this.postIdToEdit = postId
            _uiState.update {
                it.copy(
                    isEditMode = true,
                    pageText = page.toString(),
                    isGeneralReview = isOverview,
                    opinionTextFieldValue = TextFieldValue(
                        text = content,
                        selection = TextRange(content.length)
                    )
                )
            }
        }
    }

    fun onEvent(event: GroupNoteCreateEvent) {
        when (event) {
            is GroupNoteCreateEvent.PageChanged -> {
                if (!_uiState.value.isGeneralReview) {
                    _uiState.update { it.copy(pageText = event.text) }
                }
            }

            is GroupNoteCreateEvent.OpinionChanged -> {
                _uiState.update { it.copy(opinionTextFieldValue = event.newTextFieldValue) }
            }

            is GroupNoteCreateEvent.GeneralReviewToggled -> {
                _uiState.update {
                    val newPageText = if (event.isChecked) "" else it.pageText
                    it.copy(isGeneralReview = event.isChecked, pageText = newPageText)
                }
            }

            GroupNoteCreateEvent.CreateRecordClicked -> {
                if (_uiState.value.isEditMode) {
                    updateRecord()
                } else {
                    createRecord()
                }
            }
        }
    }

    private fun createRecord() {
        val currentState = _uiState.value
        val pageNumber = if (currentState.isGeneralReview) {
            currentState.totalPage
        } else {
            currentState.pageText.toIntOrNull()
        }
        if (pageNumber == null || !currentState.isFormFilled) {
            _uiState.update { it.copy(error = "페이지 번호를 정확히 입력해주세요.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val content = _uiState.value.opinionTextFieldValue.text

            roomsRepository.postRoomsRecord(
                roomId = roomId,
                content = content,
                isOverview = currentState.isGeneralReview,
                page = pageNumber
            ).onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(isLoading = false, error = throwable.message)
                }
            }
        }
    }

    private fun updateRecord() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val content = _uiState.value.opinionTextFieldValue.text
            val postId = postIdToEdit ?: return@launch

            roomsRepository.patchRoomsRecord(
                roomId = roomId,
                recordId = postId,
                content = content
            ).onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = throwable.message) }
            }
        }
    }
}