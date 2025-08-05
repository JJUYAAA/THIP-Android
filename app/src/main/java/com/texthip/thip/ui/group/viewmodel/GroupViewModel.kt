package com.texthip.thip.ui.group.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.repository.GroupRepository
import com.texthip.thip.ui.group.myroom.mock.GroupCardData
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomSectionData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    
    private var currentMyGroupsPage = 1
    private var loadedPagesCount = 0
    private val PAGES_PER_BATCH = 5  // 5페이지씩 미리 로드
    private val PRELOAD_THRESHOLD = 3  // 3페이지에 도달하면 다음 배치 로드

    private val _roomSections = MutableStateFlow<List<GroupRoomSectionData>>(emptyList())
    val roomSections: StateFlow<List<GroupRoomSectionData>> = _roomSections.asStateFlow()
    

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _doneGroups = MutableStateFlow<List<GroupCardItemRoomData>>(emptyList())
    val doneGroups: StateFlow<List<GroupCardItemRoomData>> = _doneGroups.asStateFlow()

    private val _myRoomGroups = MutableStateFlow<List<GroupCardItemRoomData>>(emptyList())
    val myRoomGroups: StateFlow<List<GroupCardItemRoomData>> = _myRoomGroups.asStateFlow()

    private val _searchGroups = MutableStateFlow<List<GroupCardItemRoomData>>(emptyList())
    val searchGroups: StateFlow<List<GroupCardItemRoomData>> = _searchGroups.asStateFlow()

    private val _genres = MutableStateFlow<List<String>>(emptyList())
    val genres: StateFlow<List<String>> = _genres.asStateFlow()
    
    private val _selectedGenreIndex = MutableStateFlow(0)
    val selectedGenreIndex: StateFlow<Int> = _selectedGenreIndex.asStateFlow()
    
    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        loadUserName()
        loadMyGroups()
        loadRoomSections()
        loadMyRoomGroups()
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
            currentMyGroupsPage = 1
            loadedPagesCount = 0
            _myGroups.value = emptyList()
            _hasMoreMyGroups.value = true
        }
        // 초기 로드 시 첫 번째 배치(5페이지) 미리 로드
        loadPageBatch()
    }
    
    private fun loadPageBatch() = viewModelScope.launch {
        if (!_hasMoreMyGroups.value) return@launch
        
        // 5페이지씩 배치로 로드
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
                    // 에러 처리 - 배치 로딩 중단
                    break
                }
        }
    }
    
    // GroupPager에서 현재 카드 인덱스를 알려주면 미리 로드 판단
    fun onCardVisible(cardIndex: Int) {
        val totalCards = _myGroups.value.size
        val currentPageEquivalent = (cardIndex / 3) + 1  // 3개씩 한 페이지라고 가정
        
        // 현재 로드된 페이지의 3페이지 지점에 도달하면 다음 배치 로드
        if (currentPageEquivalent >= loadedPagesCount - PRELOAD_THRESHOLD && 
            _hasMoreMyGroups.value) {
            loadPageBatch()
        }
    }
    
    fun loadMoreMyGroups() {
        loadPageBatch()
    }

    private fun loadRoomSections() {
        viewModelScope.launch {
            
            val currentGenres = _genres.value
            val selectedIndex = _selectedGenreIndex.value
            val selectedGenre = if (currentGenres.isNotEmpty() && selectedIndex >= 0 && selectedIndex < currentGenres.size) {
                currentGenres[selectedIndex]
            } else {
                "문학" // 기본값
            }
            
            repository.getRoomSections(selectedGenre)
                .onSuccess { sections ->
                    _roomSections.value = sections
                }
                .onFailure {
                    // 에러 처리 (필요시 에러 상태 추가)
                }
        }
    }
    
    fun selectGenre(genreIndex: Int) {
        if (genreIndex >= 0 && genreIndex != _selectedGenreIndex.value) {
            _selectedGenreIndex.value = genreIndex
            loadRoomSections() // 장르 변경 시 새로운 데이터 로드
        }
    }
    
    private fun loadMyRoomGroups() {
        viewModelScope.launch {
            repository.getMyRoomGroups()
                .onSuccess { groups ->
                    _myRoomGroups.value = groups
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
                    async {
                        repository.getUserName()
                            .onSuccess { userName ->
                                _userName.value = userName
                            }
                    },
                    async {
                        // 내 모임방 데이터 리셋 후 로드
                        currentMyGroupsPage = 1
                        loadedPagesCount = 0
                        _myGroups.value = emptyList()
                        _hasMoreMyGroups.value = true
                        
                        // 첫 번째 배치 로드
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
                                    break
                                }
                        }
                    },
                    async {
                        val currentGenres = _genres.value
                        val selectedIndex = _selectedGenreIndex.value
                        val selectedGenre = if (currentGenres.isNotEmpty() && selectedIndex >= 0 && selectedIndex < currentGenres.size) {
                            currentGenres[selectedIndex]
                        } else {
                            "문학" // 기본값
                        }
                        
                        repository.getRoomSections(selectedGenre)
                            .onSuccess { sections ->
                                _roomSections.value = sections
                            }
                    },
                    async {
                        repository.getMyRoomGroups()
                            .onSuccess { groups ->
                                _myRoomGroups.value = groups
                            }
                    }
                )
                
                jobs.awaitAll()
            } finally {
                _isRefreshing.value = false
            }
        }
    }
    
    
    suspend fun getRoomDetail(roomId: Int): GroupRoomData? {
        return repository.getRoomDetail(roomId).getOrNull()
    }

}