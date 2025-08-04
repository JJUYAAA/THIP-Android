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
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val repository: GroupRepository
) : ViewModel() {

    private val _myGroups = MutableStateFlow<List<GroupCardData>>(emptyList())
    val myGroups: StateFlow<List<GroupCardData>> = _myGroups

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
        loadGenres()
        loadRoomSections()
        loadDoneGroups()
        loadMyRoomGroups()
        loadSearchGroups()
    }
    
    private fun loadGenres() {
        viewModelScope.launch {
            repository.getGenres()
                .onSuccess { genreList ->
                    _genres.value = genreList
                }
        }
    }
    
    private fun loadUserName() {
        viewModelScope.launch {
            repository.getUserName()
                .onSuccess { userName ->
                    _userName.value = userName
                }
        }
    }

    fun loadMyGroups(page: Int = 1) = viewModelScope.launch {
        repository.getMyGroups(page)
            .onSuccess { _myGroups.value = it }
            .onFailure { }
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
        }
    }
    
    fun selectGenre(genreIndex: Int) {
        if (genreIndex >= 0 && genreIndex != _selectedGenreIndex.value) {
            _selectedGenreIndex.value = genreIndex
            loadRoomSections() // 장르 변경 시 새로운 데이터 로드
        }
    }

    private fun loadDoneGroups() {
        viewModelScope.launch {
            repository.getDoneGroups()
                .onSuccess { groups ->
                    _doneGroups.value = groups
                }
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
        loadInitialData()
    }
    
    
    suspend fun getRoomDetail(roomId: Int): GroupRoomData? {
        return repository.getRoomDetail(roomId).getOrNull()
    }

}