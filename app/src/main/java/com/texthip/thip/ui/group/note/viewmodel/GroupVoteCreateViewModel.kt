package com.texthip.thip.ui.group.note.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.rooms.request.VoteItem
import com.texthip.thip.data.repository.RoomsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GroupVoteCreateUiState(
    // 입력 값
    val pageText: String = "",
    val title: String = "",
    val options: List<String> = listOf("", ""), // 옵션은 최소 2개로 시작
    val isGeneralReview: Boolean = false,

    // 상태 값
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,

    // 초기 설정 값
    val bookTotalPage: Int = 0,
    val isGeneralReviewEnabled: Boolean = false,

    // 내부 관리용
    internal val savedPageText: String = ""
) {
    // 완료 버튼 활성화 조건
    val isFormFilled: Boolean
        get() {
            val filledOptionsCount = options.count { it.isNotBlank() }
            return (isGeneralReview || pageText.isNotBlank()) &&
                    title.isNotBlank() &&
                    filledOptionsCount >= 2
        }
}

sealed interface GroupVoteCreateEvent {
    data class PageChanged(val text: String) : GroupVoteCreateEvent
    data class TitleChanged(val text: String) : GroupVoteCreateEvent
    data class OptionChanged(val index: Int, val text: String) : GroupVoteCreateEvent
    data class GeneralReviewToggled(val isChecked: Boolean) : GroupVoteCreateEvent
    data object AddOptionClicked : GroupVoteCreateEvent
    data class RemoveOptionClicked(val index: Int) : GroupVoteCreateEvent
    data object CreateVoteClicked : GroupVoteCreateEvent
}

@HiltViewModel
class GroupVoteCreateViewModel @Inject constructor(
    private val roomsRepository: RoomsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupVoteCreateUiState())
    val uiState = _uiState.asStateFlow()

    private var roomId: Int = -1

    fun initialize(
        roomId: Int,
        recentPage: Int,
        totalPage: Int,
        isOverviewPossible: Boolean
    ) {
        this.roomId = roomId
        _uiState.update {
            it.copy(
                pageText = recentPage.toString(),
                bookTotalPage = totalPage,
                isGeneralReviewEnabled = isOverviewPossible
            )
        }
    }

    fun onEvent(event: GroupVoteCreateEvent) {
        when (event) {
            is GroupVoteCreateEvent.PageChanged -> if (!_uiState.value.isGeneralReview) {
                _uiState.update { it.copy(pageText = event.text) }
            }
            is GroupVoteCreateEvent.TitleChanged -> _uiState.update { it.copy(title = event.text) }
            is GroupVoteCreateEvent.OptionChanged -> _uiState.update {
                val newOptions = it.options.toMutableList()
                newOptions[event.index] = event.text
                it.copy(options = newOptions)
            }
            is GroupVoteCreateEvent.GeneralReviewToggled -> _uiState.update {
                if (event.isChecked) {
                    it.copy(isGeneralReview = true, savedPageText = it.pageText, pageText = "")
                } else {
                    it.copy(isGeneralReview = false, pageText = it.savedPageText)
                }
            }
            GroupVoteCreateEvent.AddOptionClicked -> if (_uiState.value.options.size < 5) {
                _uiState.update { it.copy(options = it.options + "") }
            }
            is GroupVoteCreateEvent.RemoveOptionClicked -> if (_uiState.value.options.size > 2) {
                _uiState.update {
                    val newOptions = it.options.toMutableList()
                    newOptions.removeAt(event.index)
                    it.copy(options = newOptions)
                }
            }
            GroupVoteCreateEvent.CreateVoteClicked -> createVote()
        }
    }

    private fun createVote() {
        val currentState = _uiState.value
        if (!currentState.isFormFilled) return

        val pageNumber = if (currentState.isGeneralReview) 0 else currentState.pageText.toIntOrNull()
        if (pageNumber == null) {
            _uiState.update { it.copy(error = "페이지 번호를 정확히 입력해주세요.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val voteItems = currentState.options
                .filter { it.isNotBlank() }
                .map { VoteItem(itemName = it) }

            roomsRepository.postRoomsCreateVote(
                roomId = roomId,
                page = pageNumber,
                isOverview = currentState.isGeneralReview,
                content = currentState.title,
                voteItemList = voteItems
            ).onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = throwable.message) }
            }
        }
    }
}