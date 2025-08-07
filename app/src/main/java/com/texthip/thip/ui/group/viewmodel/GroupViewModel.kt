package com.texthip.thip.ui.group.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.R
import com.texthip.thip.data.repository.GroupRepository
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
    private val repository: GroupRepository,
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupUiState())
    val uiState: StateFlow<GroupUiState> = _uiState.asStateFlow()
    
    private var currentMyGroupsPage = 1
    private var loadedPagesCount = 0
    private val pagesPerBatch = 3
    private val preloadThreshold = 2
    private var isBatchLoading = false
    
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
            loadPageBatchSuspend()
        } finally {
            updateState { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun loadPageBatchSuspend() {
        if (!uiState.value.hasMoreMyGroups || isBatchLoading) return

        try {
            isBatchLoading = true
            updateState { it.copy(isLoadingMoreMyGroups = true) }

            val currentBatchStart = currentMyGroupsPage
            val batchEndPage = currentBatchStart + pagesPerBatch - 1

            for (page in currentBatchStart..batchEndPage) {
                if (!uiState.value.hasMoreMyGroups) break

                repository.getMyJoinedRooms(page)
                    .onSuccess { paginationResult ->
                        updateState { 
                            it.copy(
                                myGroups = it.myGroups + paginationResult.data,
                                hasMoreMyGroups = paginationResult.hasMore
                            )
                        }
                        loadedPagesCount++
                        currentMyGroupsPage = page + 1
                    }
                    .onFailure {
                        break
                    }
            }
        } finally {
            isBatchLoading = false
            updateState { it.copy(isLoadingMoreMyGroups = false) }
        }
    }

    private fun loadPageBatch() = viewModelScope.launch {
        loadPageBatchSuspend()
    }

    fun onCardVisible(cardIndex: Int) {
        val currentPageEquivalent = (cardIndex / 3) + 1

        if (currentPageEquivalent >= loadedPagesCount - preloadThreshold &&
            uiState.value.hasMoreMyGroups && !isBatchLoading
        ) {
            loadPageBatch()
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
                    genres.firstOrNull() ?: context.getString(R.string.literature)
                }
            } else {
                context.getString(R.string.literature)
            }

            repository.getRoomSections(selectedGenre)
                .onSuccess { sections ->
                    updateState { it.copy(roomSections = sections) }
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
                        loadPageBatchSuspend()
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
        currentMyGroupsPage = 1
        loadedPagesCount = 0
        updateState { 
            it.copy(
                myGroups = emptyList(),
                hasMoreMyGroups = true
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

}