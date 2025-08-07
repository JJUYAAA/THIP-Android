package com.texthip.thip.ui.group.room.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.R
import com.texthip.thip.data.repository.GroupRepository
import com.texthip.thip.ui.group.myroom.mock.GroupBottomButtonType
import com.texthip.thip.ui.group.room.mock.GroupRoomRecruitUiState
import com.texthip.thip.ui.group.room.mock.RoomAction
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupRoomRecruitViewModel @Inject constructor(
    private val repository: GroupRepository,
    @param:ApplicationContext private val context: Context
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
                    showToastMessage(context.getString(R.string.success_participation_complete))
                }
                .onFailure { error ->
                    showToastMessage(context.getString(R.string.error_participation_failed, error.message ?: ""))
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
                                toastMessage = context.getString(R.string.success_participation_cancelled),
                                showToast = true,
                                shouldNavigateToGroupScreen = true
                            )
                        }
                    }
                    .onFailure { error ->
                        showToastMessage(context.getString(R.string.error_participation_cancel_failed))
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
                showToastMessage(context.getString(R.string.success_recruitment_closed))
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