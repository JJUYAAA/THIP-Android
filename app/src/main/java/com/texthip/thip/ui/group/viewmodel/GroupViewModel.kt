package com.texthip.thip.ui.group.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.repository.GroupRepository
import com.texthip.thip.ui.group.myroom.mock.GroupCardData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomSectionData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val repository: GroupRepository
) : ViewModel() {

    private val _myGroups = MutableStateFlow<List<GroupCardData>>(emptyList())
    val myGroups: StateFlow<List<GroupCardData>> = _myGroups

    private val _hasMoreMyGroups = MutableStateFlow(true)
    val hasMoreMyGroups: StateFlow<Boolean> = _hasMoreMyGroups.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isLoadingMoreMyGroups = MutableStateFlow(false)
    val isLoadingMoreMyGroups: StateFlow<Boolean> = _isLoadingMoreMyGroups.asStateFlow()
    
    private var currentMyGroupsPage = 1
    private var loadedPagesCount = 0
    private val pagesPerBatch = 3
    private val preloadThreshold = 2
    private var isBatchLoading = false

    private val _roomSections = MutableStateFlow<List<GroupRoomSectionData>>(emptyList())
    val roomSections: StateFlow<List<GroupRoomSectionData>> = _roomSections.asStateFlow()
    
    private val _roomSectionsError = MutableStateFlow<String?>(null)
    val roomSectionsError: StateFlow<String?> = _roomSectionsError.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    
    private val _selectedGenreIndex = MutableStateFlow(0)
    val selectedGenreIndex: StateFlow<Int> = _selectedGenreIndex.asStateFlow()
    
    private val _showToast = MutableStateFlow(false)
    val showToast: StateFlow<Boolean> = _showToast.asStateFlow()
    
    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage.asStateFlow()
    
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
                    _userName.value = userName
                }
        }
    }

    fun loadMyGroups(reset: Boolean = false) = viewModelScope.launch {
        if (reset) {
            resetMyGroupsData()
            _isRefreshing.value = true
        }
        try {
            loadPageBatchSuspend()
        } finally {
            _isRefreshing.value = false
        }
    }

    private suspend fun loadPageBatchSuspend() {
        if (!_hasMoreMyGroups.value || isBatchLoading) return

        try {
            isBatchLoading = true
            _isLoadingMoreMyGroups.value = true

            val currentBatchStart = currentMyGroupsPage
            val batchEndPage = currentBatchStart + pagesPerBatch - 1

            for (page in currentBatchStart..batchEndPage) {
                if (!_hasMoreMyGroups.value) break

                repository.getMyJoinedRooms(page)
                    .onSuccess { paginationResult ->
                        _myGroups.value = _myGroups.value + paginationResult.data
                        _hasMoreMyGroups.value = paginationResult.hasMore
                        loadedPagesCount++
                        currentMyGroupsPage = page + 1
                    }
                    .onFailure {
                        break
                    }
            }
        } finally {
            isBatchLoading = false
            _isLoadingMoreMyGroups.value = false
        }
    }

    private fun loadPageBatch() = viewModelScope.launch {
        loadPageBatchSuspend()
    }

    fun onCardVisible(cardIndex: Int) {
        val currentPageEquivalent = (cardIndex / 3) + 1

        if (currentPageEquivalent >= loadedPagesCount - preloadThreshold &&
            _hasMoreMyGroups.value && !isBatchLoading
        ) {
            loadPageBatch()
        }
    }

    private fun loadRoomSections() {
        viewModelScope.launch {
            _roomSectionsError.value = null

            val genresResult = repository.getGenres()
            val selectedIndex = _selectedGenreIndex.value
            val selectedGenre = if (genresResult.isSuccess) {
                val genres = genresResult.getOrThrow()
                if (selectedIndex >= 0 && selectedIndex < genres.size) {
                    genres[selectedIndex]
                } else {
                    genres.firstOrNull() ?: "문학"
                }
            } else {
                "문학"
            }

            repository.getRoomSections(selectedGenre)
                .onSuccess { sections ->
                    _roomSections.value = sections
                }
                .onFailure { error ->
                    _roomSectionsError.value = error.message
                }
        }
    }

    fun selectGenre(genreIndex: Int) {
        val genresResult = repository.getGenres()
        if (genresResult.isSuccess) {
            val genres = genresResult.getOrThrow()
            if (genreIndex >= 0 && genreIndex < genres.size && genreIndex != _selectedGenreIndex.value) {
                _selectedGenreIndex.value = genreIndex
                loadRoomSections()
            }
        }
    }



    fun refreshGroupData() {
        viewModelScope.launch {
            _isRefreshing.value = true
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
                _isRefreshing.value = false
            }
        }
    }

    private fun resetMyGroupsData() {
        currentMyGroupsPage = 1
        loadedPagesCount = 0
        _myGroups.value = emptyList()
        _hasMoreMyGroups.value = true
    }

    fun showToastMessage(message: String) {
        _toastMessage.value = message
        _showToast.value = true
    }

    fun hideToast() {
        _showToast.value = false
    }

    fun refreshDataOnScreenEnter() {
        refreshGroupData()
    }

}