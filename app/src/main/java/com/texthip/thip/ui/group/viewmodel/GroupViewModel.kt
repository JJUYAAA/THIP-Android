package com.texthip.thip.ui.group.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.repository.GroupRepository
import com.texthip.thip.ui.group.myroom.mock.GroupCardData
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
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
    private val PAGES_PER_BATCH = 3  // 5페이지에서 3페이지로 줄임
    private val PRELOAD_THRESHOLD = 2  // 임계점도 2로 줄임
    private var isBatchLoading = false

    private val _roomSections = MutableStateFlow<List<GroupRoomSectionData>>(emptyList())
    val roomSections: StateFlow<List<GroupRoomSectionData>> = _roomSections.asStateFlow()
    
    private val _roomSectionsError = MutableStateFlow<String?>(null)
    val roomSectionsError: StateFlow<String?> = _roomSectionsError.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _searchGroups = MutableStateFlow<List<GroupCardItemRoomData>>(emptyList())
    val searchGroups: StateFlow<List<GroupCardItemRoomData>> = _searchGroups.asStateFlow()
    
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
        loadSearchGroups()
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
            // Pager를 위한 초기 배치 로드
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
            
            // 3페이지씩 배치로 로드 (Pager 미리보기용)
            val currentBatchStart = currentMyGroupsPage
            val batchEndPage = currentBatchStart + PAGES_PER_BATCH - 1
            
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
                        // 에러 발생 시 배치 로딩 중단
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
    
    // GroupPager에서 현재 카드 인덱스를 알려주면 미리 로드 판단
    fun onCardVisible(cardIndex: Int) {
        val totalCards = _myGroups.value.size
        val currentPageEquivalent = (cardIndex / 3) + 1  // 3개씩 한 페이지라고 가정
        
        // 현재 로드된 페이지의 임계점에 도달하면 다음 배치 로드
        if (currentPageEquivalent >= loadedPagesCount - PRELOAD_THRESHOLD && 
            _hasMoreMyGroups.value && !isBatchLoading) {
            loadPageBatch()
        }
    }

    private fun loadRoomSections() {
        viewModelScope.launch {
            _roomSectionsError.value = null
            
            // Repository에서 직접 장르 목록 가져오기
            val genresResult = repository.getGenres()
            val selectedIndex = _selectedGenreIndex.value
            val selectedGenre = if (genresResult.isSuccess) {
                val genres = genresResult.getOrThrow()
                if (selectedIndex >= 0 && selectedIndex < genres.size) {
                    genres[selectedIndex]
                } else {
                    genres.firstOrNull() ?: "문학" // 기본값
                }
            } else {
                "문학" // 기본값
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
        // Repository에서 직접 장르 목록 확인
        val genresResult = repository.getGenres()
        if (genresResult.isSuccess) {
            val genres = genresResult.getOrThrow()
            if (genreIndex >= 0 && genreIndex < genres.size && genreIndex != _selectedGenreIndex.value) {
                _selectedGenreIndex.value = genreIndex
                loadRoomSections() // 장른 변경 시 새로운 데이터 로드
            }
        }
    }
    
    
    private fun loadSearchGroups() {
        viewModelScope.launch {
            repository.getSearchGroups()
                .onSuccess { groups ->
                    _searchGroups.value = groups
                }
        }
    }
    
    fun refreshGroupData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                // 모든 데이터를 병렬로 새로고침하고 완료를 기다림
                val jobs = listOf(
                    async { loadUserName() },
                    async { 
                        // 내 모임방 데이터 리셋 후 로드
                        resetMyGroupsData()
                        loadPageBatchSuspend()
                    },
                    async { loadRoomSections() },
                    async { loadSearchGroups() }
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

}