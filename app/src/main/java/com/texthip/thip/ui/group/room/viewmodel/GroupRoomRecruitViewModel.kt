package com.texthip.thip.ui.group.room.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.R
import com.texthip.thip.data.repository.RoomsRepository
import com.texthip.thip.ui.group.myroom.mock.GroupBottomButtonType
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
    private val repository: RoomsRepository,
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
                    // RoomRecruitingResponse에서 buttonType 결정
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
                }
                .onFailure { error ->
                    updateState { it.copy(isLoading = false) }
                }
        }
    }
    
    fun onParticipationClick() {
        viewModelScope.launch {
            val roomId = uiState.value.roomDetail?.roomId ?: return@launch
            
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
                val roomId = uiState.value.roomDetail?.roomId ?: return@launch
                
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
                val currentRoomId = _uiState.value.roomDetail?.roomId
                if (currentRoomId != null) {
                    repository.closeRoom(currentRoomId)
                        .onSuccess { response ->
                            showToastMessage(context.getString(R.string.success_recruitment_closed))
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
                                throwable.message?.contains("140008") == true -> "방 모집 마감 권한이 없습니다."
                                throwable.message?.contains("100004") == true -> "이미 모집기간이 만료된 방입니다."
                                else -> throwable.message ?: "모집 마감 중 오류가 발생했습니다."
                            }
                            showToastMessage(errorMessage)
                        }
                } else {
                    showToastMessage("방 정보를 찾을 수 없습니다.")
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