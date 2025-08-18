package com.texthip.thip.ui.group.room.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.repository.RoomsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GroupRoomChatUiState(
    val isSubmitting: Boolean = false,
    val error: String? = null
)

sealed interface GroupRoomChatEvent {
    data class ShowToast(val message: String) : GroupRoomChatEvent
    data object SubmissionSuccess : GroupRoomChatEvent
}

@HiltViewModel
class GroupRoomChatViewModel @Inject constructor(
    private val roomsRepository: RoomsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val roomId: Int = requireNotNull(savedStateHandle["roomId"])

    // TODO: 오늘의 한마디 조회 연결
    // private val _uiState = MutableStateFlow(GroupRoomChatUiState())
    // val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<GroupRoomChatEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun postDailyGreeting(content: String) {
        if (content.isBlank()) return

        viewModelScope.launch {
            roomsRepository.postRoomsDailyGreeting(
                roomId = roomId,
                content = content
            ).onSuccess {
                _eventFlow.emit(GroupRoomChatEvent.SubmissionSuccess)
                _eventFlow.emit(GroupRoomChatEvent.ShowToast("오늘의 한마디가 등록되었어요!"))
            }.onFailure {
                _eventFlow.emit(GroupRoomChatEvent.ShowToast(it.message ?: "등록에 실패했습니다."))
            }
        }
    }
}