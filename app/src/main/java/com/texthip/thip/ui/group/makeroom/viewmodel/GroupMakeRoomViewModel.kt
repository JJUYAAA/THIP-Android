package com.texthip.thip.ui.group.makeroom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.book.response.BookSavedResponse
import com.texthip.thip.data.model.repository.GroupRepository
import com.texthip.thip.data.model.group.request.CreateRoomRequest
import com.texthip.thip.data.model.repository.BookRepository
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

    private val _savedBooks = MutableStateFlow<List<BookData>>(emptyList())
    val savedBooks: StateFlow<List<BookData>> = _savedBooks.asStateFlow()
    
    private val _groupBooks = MutableStateFlow<List<BookData>>(emptyList())
    val groupBooks: StateFlow<List<BookData>> = _groupBooks.asStateFlow()
    
    private val _isLoadingBooks = MutableStateFlow(false)
    val isLoadingBooks: StateFlow<Boolean> = _isLoadingBooks.asStateFlow()

    private val _genres = MutableStateFlow<List<String>>(emptyList())
    val genres: StateFlow<List<String>> = _genres.asStateFlow()
    
    init {
        loadGenres()
    }
    
    private fun loadGenres() {
        viewModelScope.launch {
            groupRepository.getGenres()
                .onSuccess { genresList ->
                    _genres.value = genresList
                }
        }
    }

    fun selectBook(book: BookData) {
        _uiState.value = _uiState.value.copy(selectedBook = book)
    }

    fun toggleBookSearchSheet(show: Boolean) {
        _uiState.value = _uiState.value.copy(showBookSearchSheet = show)
        if (show) {
            loadBooks()
        }
    }
    
    private fun loadBooks() {
        viewModelScope.launch {
            _isLoadingBooks.value = true
            try {
                val savedBooksResult = bookRepository.getBooks("saved")
                savedBooksResult.onSuccess { bookDtos ->
                    _savedBooks.value = bookDtos.map { it.toBookData() }
                }.onFailure {
                    _savedBooks.value = emptyList()
                }
                
                val groupBooksResult = bookRepository.getBooks("joining")
                groupBooksResult.onSuccess { bookDtos ->
                    _groupBooks.value = bookDtos.map { it.toBookData() }
                }.onFailure {
                    _groupBooks.value = emptyList()
                }
            } catch (e: Exception) {
                _savedBooks.value = emptyList()
                _groupBooks.value = emptyList()
            } finally {
                _isLoadingBooks.value = false
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
        _uiState.value = _uiState.value.copy(selectedGenreIndex = index)
    }

    fun updateRoomTitle(title: String) {
        _uiState.value = _uiState.value.copy(roomTitle = title)
    }

    fun updateRoomDescription(description: String) {
        _uiState.value = _uiState.value.copy(roomDescription = description)
    }

    fun setDateRange(startDate: LocalDate, endDate: LocalDate) {
        _uiState.value = _uiState.value.copy(
            meetingStartDate = startDate,
            meetingEndDate = endDate
        )
    }

    fun setMemberLimit(count: Int) {
        _uiState.value = _uiState.value.copy(memberLimit = count)
    }

    fun togglePrivate(isPrivate: Boolean) {
        _uiState.value = _uiState.value.copy(
            isPrivate = isPrivate,
            password = if (!isPrivate) "" else _uiState.value.password
        )
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
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
                _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

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
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
    
    private fun getApiCategoryName(genreIndex: Int): String {
        val currentGenres = _genres.value
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
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}