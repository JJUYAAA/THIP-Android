package com.texthip.thip.ui.group.room.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.repository.GroupRepository
import com.texthip.thip.ui.group.myroom.mock.GroupBottomButtonType
import com.texthip.thip.ui.group.myroom.mock.GroupRoomData
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

    private val _roomDetail = MutableStateFlow<GroupRoomData?>(null)
    val roomDetail: StateFlow<GroupRoomData?> = _roomDetail.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _currentButtonType = MutableStateFlow<GroupBottomButtonType?>(null)
    val currentButtonType: StateFlow<GroupBottomButtonType?> = _currentButtonType.asStateFlow()
    
    private val _showToast = MutableStateFlow(false)
    val showToast: StateFlow<Boolean> = _showToast.asStateFlow()
    
    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage.asStateFlow()
    
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()
    
    private val _dialogTitle = MutableStateFlow("")
    val dialogTitle: StateFlow<String> = _dialogTitle.asStateFlow()
    
    private val _dialogDescription = MutableStateFlow("")
    val dialogDescription: StateFlow<String> = _dialogDescription.asStateFlow()
    
    private val _shouldNavigateToGroupScreen = MutableStateFlow(false)
    val shouldNavigateToGroupScreen: StateFlow<Boolean> = _shouldNavigateToGroupScreen.asStateFlow()
    
    private var pendingAction: (() -> Unit)? = null

    fun loadRoomDetail(roomId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            
            repository.getRoomRecruiting(roomId)
                .onSuccess { data ->
                    _roomDetail.value = data
                    _currentButtonType.value = data.buttonType
                }
                .onFailure { error ->
                    // 에러 처리 (필요시 에러 상태 추가)
                }
            
            _isLoading.value = false
        }
    }
    
    fun onParticipationClick() {
        viewModelScope.launch {
            val roomId = _roomDetail.value?.id ?: return@launch
            
            repository.joinOrCancelRoom(roomId, "join")
                .onSuccess {
                    _currentButtonType.value = GroupBottomButtonType.CANCEL
                    showToastMessage("참여가 완료되었습니다")
                }
                .onFailure { error ->
                    showToastMessage("참여 처리 중 오류가 발생했습니다: ${error.message}")
                }
        }
    }
    
    fun onCancelParticipationClick(dialogTitle: String, dialogDescription: String) {
        // 참여 취소 확인 다이얼로그 표시
        _dialogTitle.value = dialogTitle
        _dialogDescription.value = dialogDescription
        pendingAction = {
            viewModelScope.launch {
                val roomId = _roomDetail.value?.id ?: return@launch
                
                repository.joinOrCancelRoom(roomId, "cancel")
                    .onSuccess {
                        _currentButtonType.value = GroupBottomButtonType.JOIN
                        _toastMessage.value = "모임방 참여가 취소되었어요! 다른 방을 찾아보세요."
                        _shouldNavigateToGroupScreen.value = true
                    }
                    .onFailure { error ->
                        showToastMessage("참여 취소 중 오류가 발생했습니다: ${error.message}")
                    }
            }
        }
        _showDialog.value = true
    }
    
    fun onCloseRecruitmentClick(dialogTitle: String, dialogDescription: String) {
        // 모집 마감 확인 다이얼로그 표시
        _dialogTitle.value = dialogTitle
        _dialogDescription.value = dialogDescription
        pendingAction = {
            viewModelScope.launch {
                // TODO: 실제 모집 마감 API 호출
                // 현재는 mock 로직으로 처리
                showToastMessage("모집이 마감되었습니다")
            }
        }
        _showDialog.value = true
    }
    
    fun onDialogConfirm() {
        _showDialog.value = false
        pendingAction?.invoke()
        pendingAction = null
    }
    
    fun onDialogCancel() {
        _showDialog.value = false
        pendingAction = null
    }
    
    fun hideToast() {
        _showToast.value = false
    }
    
    fun onNavigatedToGroupScreen() {
        _shouldNavigateToGroupScreen.value = false
    }
    
    private fun showToastMessage(message: String) {
        _toastMessage.value = message
        _showToast.value = true
    }
}