package com.texthip.thip.ui.feed.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.R
import com.texthip.thip.data.model.book.response.BookSavedResponse
import com.texthip.thip.data.model.book.response.BookSearchItem
import com.texthip.thip.data.provider.StringResourceProvider
import com.texthip.thip.data.repository.BookRepository
import com.texthip.thip.data.repository.FeedRepository
import com.texthip.thip.ui.group.makeroom.mock.BookData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedWriteViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val bookRepository: BookRepository,
    private val stringResourceProvider: StringResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedWriteUiState())
    val uiState: StateFlow<FeedWriteUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    private fun updateState(update: (FeedWriteUiState) -> FeedWriteUiState) {
        _uiState.value = update(_uiState.value)
    }

    init {
        loadFeedWriteInfo()
    }

    private fun loadFeedWriteInfo() {
        viewModelScope.launch {
            updateState { it.copy(isLoadingCategories = true) }
            feedRepository.getFeedWriteInfo()
                .onSuccess { response ->
                    updateState {
                        it.copy(
                            categories = response?.categoryList ?: emptyList(),
                            isLoadingCategories = false
                        )
                    }
                }
                .onFailure {
                    updateState {
                        it.copy(
                            categories = emptyList(),
                            isLoadingCategories = false,
                            errorMessage = stringResourceProvider.getString(R.string.error_network_error)
                        )
                    }
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
                val savedBooksResult = bookRepository.getBooks("SAVED")
                savedBooksResult.onSuccess { response ->
                    updateState {
                        it.copy(savedBooks = response?.bookList?.map { dto -> dto.toBookData() }
                            ?: emptyList())
                    }
                }.onFailure {
                    updateState { it.copy(savedBooks = emptyList()) }
                }

                val groupBooksResult = bookRepository.getBooks("JOINING")
                groupBooksResult.onSuccess { response ->
                    updateState {
                        it.copy(groupBooks = response?.bookList?.map { dto -> dto.toBookData() }
                            ?: emptyList())
                    }
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
            imageUrl = this.bookImageUrl,
            author = this.authorName,
            isbn = this.isbn
        )
    }

    private fun BookSearchItem.toBookData(): BookData {
        return BookData(
            title = this.title,
            imageUrl = this.imageUrl,
            author = this.authorName,
            isbn = this.isbn
        )
    }

    fun searchBooks(query: String) {
        searchJob?.cancel()

        if (query.isBlank()) {
            updateState { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(300) // 디바운싱
            updateState { it.copy(isSearching = true) }

            try {
                val result = bookRepository.searchBooks(query, page = 1, isFinalized = false)
                result.onSuccess { response ->
                    val searchResults =
                        response?.searchResult?.map {
                            it.toBookData()
                        } ?: emptyList()
                    updateState {
                        it.copy(
                            searchResults = searchResults,
                            isSearching = false
                        )
                    }
                }.onFailure {
                    updateState {
                        it.copy(
                            searchResults = emptyList(),
                            isSearching = false
                        )
                    }
                }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        searchResults = emptyList(),
                        isSearching = false
                    )
                }
            }
        }
    }

    fun updateFeedContent(content: String) {
        if (content.length <= 2000) {
            updateState { it.copy(feedContent = content) }
        }
    }

    fun addImages(newImageUris: List<Uri>) {
        val currentState = _uiState.value
        val availableSlots = 3 - currentState.imageUris.size
        val imagesToAdd = newImageUris.take(availableSlots)

        updateState {
            it.copy(imageUris = currentState.imageUris + imagesToAdd)
        }
    }

    fun removeImage(index: Int) {
        val currentImages = _uiState.value.imageUris.toMutableList()
        if (index in currentImages.indices) {
            currentImages.removeAt(index)
            updateState { it.copy(imageUris = currentImages) }
        }
    }

    fun togglePrivate(isPrivate: Boolean) {
        updateState { it.copy(isPrivate = isPrivate) }
    }

    fun selectCategory(index: Int) {
        updateState {
            it.copy(
                selectedCategoryIndex = index,
                selectedTags = emptyList() // 카테고리 변경 시 태그 초기화
            )
        }
    }

    fun toggleTag(tag: String) {
        val currentState = _uiState.value
        val newSelectedTags = if (currentState.selectedTags.contains(tag)) {
            currentState.selectedTags - tag
        } else {
            if (currentState.canAddMoreTags) {
                currentState.selectedTags + tag
            } else {
                currentState.selectedTags
            }
        }
        updateState { it.copy(selectedTags = newSelectedTags) }
    }

    fun removeTag(tag: String) {
        val currentTags = _uiState.value.selectedTags
        updateState {
            it.copy(selectedTags = currentTags - tag)
        }
    }

    fun createFeed(onSuccess: (Int) -> Unit, onError: (String) -> Unit) {
        val currentState = _uiState.value

        if (!currentState.isFormValid) {
            onError(stringResourceProvider.getString(R.string.error_form_validation))
            return
        }

        val selectedBook = currentState.selectedBook
        if (selectedBook?.isbn == null) {
            onError(stringResourceProvider.getString(R.string.error_book_info_invalid))
            return
        }

        viewModelScope.launch {
            try {
                updateState { it.copy(isLoading = true, errorMessage = null) }

                val result = feedRepository.createFeed(
                    isbn = selectedBook.isbn,
                    contentBody = currentState.feedContent.trim(),
                    isPublic = !currentState.isPrivate,
                    tagList = currentState.selectedTags,
                    imageUris = currentState.imageUris
                )

                result.onSuccess { response ->
                    val feedId = response?.feedId
                    if (feedId != null) {
                        onSuccess(feedId)
                    } else {
                        onError(stringResourceProvider.getString(R.string.error_feed_id_not_returned))
                    }
                }.onFailure { exception ->
                    onError(
                        exception.message
                            ?: stringResourceProvider.getString(R.string.error_network_error)
                    )
                }

            } catch (e: Exception) {
                onError(
                    stringResourceProvider.getString(
                        R.string.error_network_error,
                        e.message ?: ""
                    )
                )
            } finally {
                updateState { it.copy(isLoading = false) }
            }
        }
    }

    fun clearError() {
        updateState { it.copy(errorMessage = null) }
    }
}