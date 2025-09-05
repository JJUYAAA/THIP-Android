package com.texthip.thip.ui.group.room.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.rooms.response.RoomsPlayingResponse
import com.texthip.thip.data.repository.RoomsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface GroupRoomUiState {
    data object Loading : GroupRoomUiState // 로딩 중
    data class Success(val roomsPlaying: RoomsPlayingResponse) : GroupRoomUiState // 성공
    data class Error(val message: String) : GroupRoomUiState // 실패
}

enum class GroupRoomToastType {
    LEAVE_ROOM_SUCCESS,
    ACTION_FAILURE // 방 나가기/삭제 등 실패 시 공통으로 사용
}

sealed interface GroupRoomEvent {
    data class ShowToast(val type: GroupRoomToastType) : GroupRoomEvent
}

@HiltViewModel
class GroupRoomViewModel @Inject constructor(
    private val roomsRepository: RoomsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<GroupRoomUiState>(GroupRoomUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<GroupRoomEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun fetchRoomsPlaying(roomId: Int) {
        // ViewModel의 생명주기와 연결된 코루틴 스코프에서 실행
        viewModelScope.launch {
            _uiState.value = GroupRoomUiState.Loading

            roomsRepository.getRoomsPlaying(roomId = roomId)
                .onSuccess { roomsPlaying ->
                    if (roomsPlaying != null) {
                        _uiState.value = GroupRoomUiState.Success(roomsPlaying)
                    }
                }
                .onFailure { throwable ->
                    _uiState.value = GroupRoomUiState.Error(
                        throwable.message ?: "알 수 없는 오류가 발생했습니다."
                    )
                }
        }
    }

    fun leaveRoom(roomId: Int) {
        viewModelScope.launch {
            roomsRepository.leaveRoom(roomId = roomId)
                .onSuccess {
                    _eventFlow.emit(GroupRoomEvent.ShowToast(GroupRoomToastType.LEAVE_ROOM_SUCCESS))
                }
                .onFailure { throwable ->
                    _eventFlow.emit(GroupRoomEvent.ShowToast(GroupRoomToastType.ACTION_FAILURE))
                }
        }
    }
}