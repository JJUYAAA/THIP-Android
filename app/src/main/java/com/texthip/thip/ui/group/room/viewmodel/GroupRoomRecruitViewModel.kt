package com.texthip.thip.ui.group.room.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.repository.GroupRepository
import com.texthip.thip.ui.group.myroom.mock.GroupBottomButtonType
import com.texthip.thip.ui.group.room.mock.GroupRoomRecruitUiState
import com.texthip.thip.ui.group.room.mock.RoomAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupRoomRecruitViewModel @Inject constructor(
    private val repository: GroupRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupRoomRecruitUiState())
    val uiState: StateFlow<GroupRoomRecruitUiState> = _uiState.asStateFlow()
    
    private var pendingAction: (() -> Unit)? = null
    
    private fun updateState(update: (GroupRoomRecruitUiState) -> GroupRoomRecruitUiState) {
        _uiState.value = update(_uiState.value)
    }

    fun loadRoomDetail(roomId: Int) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            
            repository.getRoomRecruiting(roomId)
                .onSuccess { data ->
                    updateState { 
                        it.copy(
                            roomDetail = data,
                            currentButtonType = data.buttonType,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    updateState { it.copy(isLoading = false) }
                }
        }
    }
    
    fun onParticipationClick() {
        viewModelScope.launch {
            val roomId = uiState.value.roomDetail?.id ?: return@launch
            
            repository.joinOrCancelRoom(roomId, RoomAction.JOIN.value)
                .onSuccess {
                    updateState { it.copy(currentButtonType = GroupBottomButtonType.CANCEL) }
                    showToastMessage("참여가 완료되었습니다")
                }
                .onFailure { error ->
                    showToastMessage("참여 처리 중 오류가 발생했습니다: ${error.message}")
                }
        }
    }
    
    fun onCancelParticipationClick(dialogTitle: String, dialogDescription: String) {
        updateState { 
            it.copy(
                dialogTitle = dialogTitle,
                dialogDescription = dialogDescription,
                showDialog = true
            )
        }
        pendingAction = {
            viewModelScope.launch {
                val roomId = uiState.value.roomDetail?.id ?: return@launch
                
                repository.joinOrCancelRoom(roomId, RoomAction.CANCEL.value)
                    .onSuccess {
                        updateState { 
                            it.copy(
                                currentButtonType = GroupBottomButtonType.JOIN,
                                toastMessage = "모임방 참여가 취소되었어요! 다른 방을 찾아보세요.",
                                showToast = true,
                                shouldNavigateToGroupScreen = true
                            )
                        }
                    }
                    .onFailure { error ->
                        showToastMessage("참여 취소 중 오류가 발생했습니다: ${error.message}")
                    }
            }
        }
    }
    
    fun onCloseRecruitmentClick(dialogTitle: String, dialogDescription: String) {
        updateState { 
            it.copy(
                dialogTitle = dialogTitle,
                dialogDescription = dialogDescription,
                showDialog = true
            )
        }
        pendingAction = {
            viewModelScope.launch {
                // TODO: 실제 모집 마감 API 호출
                showToastMessage("모집이 마감되었습니다")
            }
        }
    }
    
    fun onDialogConfirm() {
        updateState { it.copy(showDialog = false) }
        pendingAction?.invoke()
        pendingAction = null
    }
    
    fun onDialogCancel() {
        updateState { it.copy(showDialog = false) }
        pendingAction = null
    }
    
    fun hideToast() {
        updateState { it.copy(showToast = false) }
    }
    
    fun onNavigatedToGroupScreen() {
        updateState { it.copy(shouldNavigateToGroupScreen = false) }
    }
    
    private fun showToastMessage(message: String) {
        updateState { 
            it.copy(
                toastMessage = message,
                showToast = true
            )
        }
    }
}