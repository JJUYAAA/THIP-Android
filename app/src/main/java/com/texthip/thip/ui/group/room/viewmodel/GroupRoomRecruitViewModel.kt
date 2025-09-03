package com.texthip.thip.ui.group.room.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.R
import com.texthip.thip.data.provider.StringResourceProvider
import com.texthip.thip.data.repository.RoomsRepository
import com.texthip.thip.ui.group.myroom.mock.GroupBottomButtonType
import com.texthip.thip.ui.group.room.mock.RoomAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupRoomRecruitViewModel @Inject constructor(
    private val repository: RoomsRepository,
    private val stringResourceProvider: StringResourceProvider
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
                    if (data != null) {
                        val buttonType = when {
                            data.isHost -> GroupBottomButtonType.CLOSE
                            data.isJoining -> GroupBottomButtonType.CANCEL
                            else -> GroupBottomButtonType.JOIN
                        }
                        updateState { 
                            it.copy(
                                roomDetail = data,
                                currentButtonType = buttonType,
                                isLoading = false
                            )
                        }
                    } else {
                        updateState { 
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                }
                .onFailure { error ->
                    updateState { it.copy(isLoading = false) }
                }
        }
    }
    
    fun onParticipationClick() {
        val currentState = uiState.value
        val roomDetail = currentState.roomDetail ?: return
        
        // 인원이 가득 찬 경우 메시지 표시
        if (roomDetail.memberCount >= roomDetail.recruitCount) {
            showToastMessage(stringResourceProvider.getString(R.string.error_max_participate))
            return
        }
        
        viewModelScope.launch {
            repository.joinOrCancelRoom(roomDetail.roomId, RoomAction.JOIN.value)
                .onSuccess {
                    updateState { it.copy(currentButtonType = GroupBottomButtonType.CANCEL) }
                    showToastMessage(stringResourceProvider.getString(R.string.success_participation_complete))
                }
                .onFailure { error ->
                    showToastMessage(stringResourceProvider.getString(R.string.error_participation_failed, error.message ?: ""))
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
                val roomId = uiState.value.roomDetail?.roomId ?: return@launch
                
                repository.joinOrCancelRoom(roomId, RoomAction.CANCEL.value)
                    .onSuccess {
                        updateState { 
                            it.copy(
                                currentButtonType = GroupBottomButtonType.JOIN,
                                toastMessage = stringResourceProvider.getString(R.string.success_participation_cancelled),
                                showToast = true,
                                shouldNavigateToGroupScreen = true
                            )
                        }
                    }
                    .onFailure { error ->
                        showToastMessage(stringResourceProvider.getString(R.string.error_participation_cancel_failed))
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
                val currentRoomId = _uiState.value.roomDetail?.roomId
                if (currentRoomId != null) {
                    repository.closeRoom(currentRoomId)
                        .onSuccess { response ->
                            showToastMessage(stringResourceProvider.getString(R.string.success_recruitment_closed))
                            // 마감 성공 시 기록장 화면으로 이동
                            updateState { 
                                it.copy(
                                    shouldNavigateToRoomPlayingScreen = true,
                                    roomId = response.roomId
                                ) 
                            }
                        }
                        .onFailure { throwable ->
                            // 에러 처리
                            val errorMessage = when {
                                throwable.message?.contains("140008") == true -> stringResourceProvider.getString(R.string.error_room_close_permission)
                                throwable.message?.contains("100004") == true -> stringResourceProvider.getString(R.string.error_room_expired)
                                else -> throwable.message ?: stringResourceProvider.getString(R.string.error_room_close_failed)
                            }
                            showToastMessage(errorMessage)
                        }
                } else {
                    showToastMessage(stringResourceProvider.getString(R.string.error_room_info_not_found))
                }
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
    
    fun onNavigatedToRoomPlayingScreen() {
        updateState { 
            it.copy(
                shouldNavigateToRoomPlayingScreen = false,
                roomId = null
            ) 
        }
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