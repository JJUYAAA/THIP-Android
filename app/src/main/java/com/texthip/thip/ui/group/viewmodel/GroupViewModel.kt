package com.texthip.thip.ui.group.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.manager.Genre
import com.texthip.thip.data.repository.RoomsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val repository: RoomsRepository,
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupUiState())
    val uiState: StateFlow<GroupUiState> = _uiState.asStateFlow()
    
    private var nextCursor: String? = null
    private var isLoadingMyGroups = false
    
    private fun updateState(update: (GroupUiState) -> GroupUiState) {
        _uiState.value = update(_uiState.value)
    }
    
    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        loadUserName()
        loadMyGroups()
        loadRoomSections()
    }

    private fun loadUserName() {
        viewModelScope.launch {
            repository.getUserName()
                .onSuccess { userName ->
                    updateState { it.copy(userName = userName) }
                }
        }
    }

    fun loadMyGroups(reset: Boolean = false) = viewModelScope.launch {
        if (reset) {
            resetMyGroupsData()
            updateState { it.copy(isRefreshing = true) }
        }
        try {
            loadMyGroupsSuspend(isInitial = reset)
        } finally {
            updateState { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun loadMyGroupsSuspend(isInitial: Boolean = false) {
        if (isLoadingMyGroups) return
        if (!isInitial && _uiState.value.isLast) return

        try {
            isLoadingMyGroups = true
            
            if (isInitial) {
                updateState { it.copy(isLoadingMoreMyGroups = false) }
            } else {
                updateState { it.copy(isLoadingMoreMyGroups = true) }
            }

            val cursor = if (isInitial) null else nextCursor

            repository.getMyJoinedRooms(cursor)
                .onSuccess { joinedRoomsResponse ->
                    if (joinedRoomsResponse != null) {
                        val currentList = if (isInitial) emptyList() else _uiState.value.myJoinedRooms
                        updateState { 
                            it.copy(
                                myJoinedRooms = currentList + joinedRoomsResponse.roomList,
                                hasMoreMyGroups = !joinedRoomsResponse.isLast,
                                isLast = joinedRoomsResponse.isLast
                            )
                        }
                        nextCursor = joinedRoomsResponse.nextCursor
                    } else {
                        updateState { it.copy(hasMoreMyGroups = false, isLast = true) }
                    }
                }
                .onFailure { exception ->
                    updateState { it.copy(error = exception.message) }
                }
        } finally {
            isLoadingMyGroups = false
            updateState { it.copy(isLoadingMoreMyGroups = false) }
        }
    }

    fun loadMoreGroups() {
        if (_uiState.value.hasMoreMyGroups && !isLoadingMyGroups) {
            viewModelScope.launch {
                loadMyGroupsSuspend(isInitial = false)
            }
        }
    }

    private fun loadRoomSections() {
        viewModelScope.launch {
            updateState { it.copy(roomSectionsError = null) }

            val genresResult = repository.getGenres()
            val selectedIndex = uiState.value.selectedGenreIndex
            val selectedGenre = if (genresResult.isSuccess) {
                val genres = genresResult.getOrThrow()
                if (selectedIndex >= 0 && selectedIndex < genres.size) {
                    genres[selectedIndex]
                } else {
                    genres.firstOrNull() ?: Genre.getDefault()
                }
            } else {
                Genre.getDefault()
            }

            repository.getRoomSections(selectedGenre)
                .onSuccess { roomMainList ->
                    updateState { it.copy(roomMainList = roomMainList) }
                }
                .onFailure { error ->
                    updateState { it.copy(roomSectionsError = error.message) }
                }
        }
    }

    fun selectGenre(genreIndex: Int) {
        val genresResult = repository.getGenres()
        if (genresResult.isSuccess) {
            val genres = genresResult.getOrThrow()
            if (genreIndex >= 0 && genreIndex < genres.size && genreIndex != uiState.value.selectedGenreIndex) {
                updateState { it.copy(selectedGenreIndex = genreIndex) }
                loadRoomSections()
            }
        }
    }



    fun refreshGroupData() {
        viewModelScope.launch {
            updateState { it.copy(isRefreshing = true) }
            try {
                val jobs = listOf(
                    async { loadUserName() },
                    async {
                        resetMyGroupsData()
                        loadMyGroupsSuspend(isInitial = true)
                    },
                    async { loadRoomSections() },
                )

                jobs.awaitAll()
            } finally {
                updateState { it.copy(isRefreshing = false) }
            }
        }
    }

    private fun resetMyGroupsData() {
        nextCursor = null
        updateState { 
            it.copy(
                myJoinedRooms = emptyList(),
                hasMoreMyGroups = true,
                isLast = false
            )
        }
    }

    fun showToastMessage(message: String) {
        updateState { 
            it.copy(
                toastMessage = message,
                showToast = true
            )
        }
    }

    fun hideToast() {
        updateState { it.copy(showToast = false) }
    }

    fun refreshDataOnScreenEnter() {
        refreshGroupData()
    }

    fun resetToInitialState() {
        // 장르 선택을 초기화하고 모든 데이터를 새로고침
        updateState { 
            it.copy(
                selectedGenreIndex = 0,
                roomMainList = it.roomMainList?.copy(
                    deadlineRoomList = emptyList(),
                    popularRoomList = emptyList()
                )
            )
        }
        refreshGroupData()
    }

}