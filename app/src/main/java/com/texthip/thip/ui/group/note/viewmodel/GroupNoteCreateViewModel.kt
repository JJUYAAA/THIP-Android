package com.texthip.thip.ui.group.note.viewmodel

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
    val opinionText: String = "",
    val isGeneralReview: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
) {
    // 입력 폼이 모두 채워졌는지 확인
    val isFormFilled: Boolean get() = pageText.isNotBlank() && opinionText.isNotBlank()
}

sealed interface GroupNoteCreateEvent {
    data class PageChanged(val text: String) : GroupNoteCreateEvent
    data class OpinionChanged(val text: String) : GroupNoteCreateEvent
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

    fun initialize(id: Int) {
        roomId = id
    }

    fun onEvent(event: GroupNoteCreateEvent) {
        when (event) {
            is GroupNoteCreateEvent.PageChanged -> {
                _uiState.update { it.copy(pageText = event.text) }
            }
            is GroupNoteCreateEvent.OpinionChanged -> {
                _uiState.update { it.copy(opinionText = event.text) }
            }
            is GroupNoteCreateEvent.GeneralReviewToggled -> {
                _uiState.update { it.copy(isGeneralReview = event.isChecked) }
            }
            GroupNoteCreateEvent.CreateRecordClicked -> {
                createRecord()
            }
        }
    }

    private fun createRecord() {
        val currentState = _uiState.value
        val pageNumber = currentState.pageText.toIntOrNull()
        if (pageNumber == null || !currentState.isFormFilled) {
            _uiState.update { it.copy(error = "페이지 번호를 정확히 입력해주세요.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            roomsRepository.postRoomsRecord(
                roomId = roomId,
                content = currentState.opinionText,
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
}