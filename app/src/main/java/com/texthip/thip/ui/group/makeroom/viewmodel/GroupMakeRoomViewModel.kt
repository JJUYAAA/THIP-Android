package com.texthip.thip.ui.group.makeroom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.book.response.BookSavedResponse
import com.texthip.thip.data.repository.GroupRepository
import com.texthip.thip.data.model.group.request.CreateRoomRequest
import com.texthip.thip.data.repository.BookRepository
import com.texthip.thip.ui.group.makeroom.mock.BookData
import com.texthip.thip.ui.group.makeroom.mock.GroupMakeRoomUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class GroupMakeRoomViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupMakeRoomUiState())
    val uiState: StateFlow<GroupMakeRoomUiState> = _uiState.asStateFlow()
    
    private fun updateState(update: (GroupMakeRoomUiState) -> GroupMakeRoomUiState) {
        _uiState.value = update(_uiState.value)
    }
    
    init {
        loadGenres()
    }
    
    private fun loadGenres() {
        viewModelScope.launch {
            groupRepository.getGenres()
                .onSuccess { genresList ->
                    updateState { it.copy(genres = genresList) }
                }
        }
    }

    fun selectBook(book: BookData) {
        updateState { it.copy(selectedBook = book) }
    }

    fun toggleBookSearchSheet(show: Boolean) {
        updateState { it.copy(showBookSearchSheet = show) }
        if (show) {
            loadBooks()
        }
    }
    
    private fun loadBooks() {
        viewModelScope.launch {
            updateState { it.copy(isLoadingBooks = true) }
            try {
                val savedBooksResult = bookRepository.getBooks("saved")
                savedBooksResult.onSuccess { bookDtos ->
                    updateState { it.copy(savedBooks = bookDtos.map { dto -> dto.toBookData() }) }
                }.onFailure {
                    updateState { it.copy(savedBooks = emptyList()) }
                }
                
                val groupBooksResult = bookRepository.getBooks("joining")
                groupBooksResult.onSuccess { bookDtos ->
                    updateState { it.copy(groupBooks = bookDtos.map { dto -> dto.toBookData() }) }
                }.onFailure {
                    updateState { it.copy(groupBooks = emptyList()) }
                }
            } catch (e: Exception) {
                updateState { it.copy(savedBooks = emptyList(), groupBooks = emptyList()) }
            } finally {
                updateState { it.copy(isLoadingBooks = false) }
            }
        }
    }
    
    private fun BookSavedResponse.toBookData(): BookData {
        return BookData(
            title = this.bookTitle,
            imageUrl = this.imageUrl,
            author = this.authorName,
            isbn = this.isbn
        )
    }

    fun selectGenre(index: Int) {
        updateState { it.copy(selectedGenreIndex = index) }
    }

    fun updateRoomTitle(title: String) {
        updateState { it.copy(roomTitle = title) }
    }

    fun updateRoomDescription(description: String) {
        updateState { it.copy(roomDescription = description) }
    }

    fun setDateRange(startDate: LocalDate, endDate: LocalDate) {
        updateState { 
            it.copy(
                meetingStartDate = startDate,
                meetingEndDate = endDate
            )
        }
    }

    fun setMemberLimit(count: Int) {
        updateState { it.copy(memberLimit = count) }
    }

    fun togglePrivate(isPrivate: Boolean) {
        updateState { 
            it.copy(
                isPrivate = isPrivate,
                password = if (!isPrivate) "" else it.password
            )
        }
    }

    fun updatePassword(password: String) {
        updateState { it.copy(password = password) }
    }

    fun createGroup(onSuccess: (Int) -> Unit, onError: (String) -> Unit) {
        val currentState = _uiState.value

        if (!currentState.isFormValid) {
            onError("입력 정보를 확인해주세요")
            return
        }

        val selectedBook = currentState.selectedBook
        if (selectedBook?.isbn == null) {
            onError("책 정보가 올바르지 않습니다")
            return
        }

        viewModelScope.launch {
            try {
                updateState { it.copy(isLoading = true, errorMessage = null) }

                val request = CreateRoomRequest(
                    isbn = selectedBook.isbn,
                    category = getApiCategoryName(currentState.selectedGenreIndex),
                    roomName = currentState.roomTitle.trim(),
                    description = currentState.roomDescription.trim(),
                    progressStartDate = currentState.meetingStartDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                    progressEndDate = currentState.meetingEndDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                    recruitCount = currentState.memberLimit,
                    password = if (currentState.isPrivate) currentState.password else null,
                    isPublic = !currentState.isPrivate
                )

                val result = groupRepository.createRoom(request)
                result.onSuccess { roomId ->
                    onSuccess(roomId)
                }.onFailure { exception ->
                    onError("모임방 생성에 실패했습니다: ${exception.message}")
                }
            } catch (e: Exception) {
                onError("네트워크 오류가 발생했습니다: ${e.message}")
            } finally {
                updateState { it.copy(isLoading = false) }
            }
        }
    }
    
    private fun getApiCategoryName(genreIndex: Int): String {
        val currentGenres = uiState.value.genres
        if (genreIndex >= 0 && genreIndex < currentGenres.size) {
            val genre = currentGenres[genreIndex]
            return when (genre) {
                "과학·IT" -> "과학/IT"
                else -> genre
            }
        }
        return "문학"
    }

    fun clearError() {
        updateState { it.copy(errorMessage = null) }
    }
}