package com.texthip.thip.ui.group.room.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.rooms.response.RoomsUsersResponse
import com.texthip.thip.data.repository.RoomsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface GroupRoomMatesUiState {
    data object Loading : GroupRoomMatesUiState
    data class Success(val users: RoomsUsersResponse) : GroupRoomMatesUiState
    data class Error(val message: String) : GroupRoomMatesUiState
}

@HiltViewModel
class GroupRoomMatesViewModel @Inject constructor(
    private val roomsRepository: RoomsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GroupRoomMatesUiState>(GroupRoomMatesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchUsers(roomId: Int) {
        viewModelScope.launch {
            _uiState.value = GroupRoomMatesUiState.Loading

            roomsRepository.getRoomsUsers(roomId = roomId)
                .onSuccess { users ->
                    if (users != null) {
                        _uiState.value = GroupRoomMatesUiState.Success(users)
                    }
                }
                .onFailure { throwable ->
                    _uiState.value = GroupRoomMatesUiState.Error(
                        throwable.message ?: "알 수 없는 오류가 발생했습니다."
                    )
                }
        }
    }
}