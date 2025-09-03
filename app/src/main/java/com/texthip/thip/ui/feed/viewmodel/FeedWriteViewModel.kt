package com.texthip.thip.ui.feed.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.R
import com.texthip.thip.data.model.book.response.BookSavedResponse
import com.texthip.thip.data.model.book.response.BookSearchItem
import com.texthip.thip.data.model.book.response.BookUserSaveList
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
import kotlin.onFailure

@HiltViewModel
class FeedWriteViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val bookRepository: BookRepository,
    private val stringResourceProvider: StringResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedWriteUiState())
    val uiState: StateFlow<FeedWriteUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var loadMoreSearchJob: Job? = null
    private var savedBooksCursor: String? = null
    private var groupBooksCursor: String? = null
    private var isLoadingSavedBooks = false
    private var isLoadingGroupBooks = false

    private fun updateState(update: (FeedWriteUiState) -> FeedWriteUiState) {
        _uiState.value = update(_uiState.value)
    }

    init {
        loadFeedWriteInfo()
    }

    fun setPinnedRecord(
        isbn: String,
        bookTitle: String,
        bookAuthor: String,
        bookImageUrl: String,
        recordContent: String
    ) {
        val pinnedBook = BookData(
            title = bookTitle,
            imageUrl = bookImageUrl,
            author = bookAuthor,
            isbn = isbn
        )
        updateState {
            it.copy(
                selectedBook = pinnedBook,
                isBookPreselected = true,
                feedContent = recordContent
            )
        }
    }

    fun loadFeedForEdit(feedId: Long) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }

            feedRepository.getFeedDetail(feedId)
                .onSuccess { feedDetail ->
                    if (feedDetail != null) {
                        // 선택된 카테고리 인덱스 찾기
                        val categoryIndex = _uiState.value.categories.indexOfFirst { category ->
                            feedDetail.tagList.any { tag ->
                                category.tagList.contains(tag)
                            }
                        }.let { if (it == -1) 0 else it }

                        val selectedBook = BookData(
                            title = feedDetail.bookTitle,
                            imageUrl = feedDetail.bookImageUrl ?: "", // 새로 추가된 필드 사용
                            author = feedDetail.bookAuthor,
                            isbn = feedDetail.isbn
                        )

                        updateState { currentState ->
                            currentState.copy(
                                selectedBook = selectedBook,
                                isBookPreselected = true,
                                feedContent = feedDetail.contentBody,
                                isPrivate = !(feedDetail.isPublic ?: true), // 새로 추가된 필드 사용, 기본값은 공개
                                selectedCategoryIndex = categoryIndex,
                                selectedTags = feedDetail.tagList,
                                existingImageUrls = feedDetail.contentUrls, // 기존 이미지 URL 저장
                                isLoading = false,
                                isEditMode = true,
                                editingFeedId = feedId
                            )
                        }
                    } else {
                        updateState {
                            it.copy(
                                isLoading = false,
                                errorMessage = "피드 정보를 불러올 수 없습니다."
                            )
                        }
                    }
                }
                .onFailure { exception ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "네트워크 오류가 발생했습니다."
                        )
                    }
                }
        }
    }

    fun setEditData(
        feedId: Long,
        isbn: String,
        bookTitle: String,
        bookAuthor: String,
        bookImageUrl: String,
        contentBody: String,
        isPublic: Boolean,
        tagList: List<String>
    ) {
        // 선택된 카테고리 인덱스 찾기
        val categoryIndex = _uiState.value.categories.indexOfFirst { category ->
            tagList.any { tag ->
                category.tagList.contains(tag)
            }
        }.let { if (it == -1) 0 else it }

        val selectedBook = BookData(
            title = bookTitle,
            imageUrl = bookImageUrl,
            author = bookAuthor,
            isbn = isbn
        )

        updateState { currentState ->
            currentState.copy(
                selectedBook = selectedBook,
                isBookPreselected = true,
                feedContent = contentBody,
                isPrivate = !isPublic,
                selectedCategoryIndex = categoryIndex,
                selectedTags = tagList,
                isEditMode = true,
                editingFeedId = feedId
            )
        }
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
        updateState { it.copy(isLoadingBooks = true) }
        loadSavedBooks(isInitial = true)
        loadGroupBooks(isInitial = true)
    }

    fun loadSavedBooks(isInitial: Boolean = false) {
        if (isLoadingSavedBooks) return
        if (!isInitial && _uiState.value.isLastSavedBooks) return

        viewModelScope.launch {
            try {
                isLoadingSavedBooks = true
                
                if (isInitial) {
                    updateState { it.copy(savedBooks = emptyList(), isLastSavedBooks = false) }
                    savedBooksCursor = null
                } else {
                    updateState { it.copy(isLoadingMoreSavedBooks = true) }
                }

                val cursor = if (isInitial) null else savedBooksCursor

                bookRepository.getBooks("SAVED", cursor)
                    .onSuccess { response ->
                        if (response != null) {
                            val currentList = if (isInitial) emptyList() else _uiState.value.savedBooks
                            val newBooks = response.bookList.map { it.toBookData() }
                            updateState {
                                it.copy(
                                    savedBooks = currentList + newBooks,
                                    isLastSavedBooks = response.isLast
                                )
                            }
                            savedBooksCursor = response.nextCursor
                        } else {
                            updateState { it.copy(isLastSavedBooks = true) }
                        }
                    }
                    .onFailure { exception ->
                        if (isInitial) {
                            updateState { it.copy(savedBooks = emptyList()) }
                        }
                    }
            } finally {
                isLoadingSavedBooks = false
                updateState { 
                    it.copy(
                        isLoadingBooks = if (isInitial && !isLoadingGroupBooks) false else it.isLoadingBooks,
                        isLoadingMoreSavedBooks = false
                    ) 
                }
            }
        }
    }

    fun loadGroupBooks(isInitial: Boolean = false) {
        if (isLoadingGroupBooks) return
        if (!isInitial && _uiState.value.isLastGroupBooks) return

        viewModelScope.launch {
            try {
                isLoadingGroupBooks = true
                
                if (isInitial) {
                    updateState { it.copy(groupBooks = emptyList(), isLastGroupBooks = false) }
                    groupBooksCursor = null
                } else {
                    updateState { it.copy(isLoadingMoreGroupBooks = true) }
                }

                val cursor = if (isInitial) null else groupBooksCursor

                bookRepository.getBooks("JOINING", cursor)
                    .onSuccess { response ->
                        if (response != null) {
                            val currentList = if (isInitial) emptyList() else _uiState.value.groupBooks
                            val newBooks = response.bookList.map { it.toBookData() }
                            updateState {
                                it.copy(
                                    groupBooks = currentList + newBooks,
                                    isLastGroupBooks = response.isLast
                                )
                            }
                            groupBooksCursor = response.nextCursor
                        } else {
                            updateState { it.copy(isLastGroupBooks = true) }
                        }
                    }
                    .onFailure { exception ->
                        if (isInitial) {
                            updateState { it.copy(groupBooks = emptyList()) }
                        }
                    }
            } finally {
                isLoadingGroupBooks = false
                updateState { 
                    it.copy(
                        isLoadingBooks = if (isInitial && !isLoadingSavedBooks) false else it.isLoadingBooks,
                        isLoadingMoreGroupBooks = false
                    ) 
                }
            }
        }
    }

    fun loadMoreSavedBooks() {
        loadSavedBooks(isInitial = false)
    }

    fun loadMoreGroupBooks() {
        loadGroupBooks(isInitial = false)
    }

    private fun BookSavedResponse.toBookData(): BookData {
        return BookData(
            title = this.bookTitle,
            imageUrl = this.bookImageUrl,
            author = this.authorName,
            isbn = this.isbn
        )
    }

    private fun BookUserSaveList.toBookDataFromSaved(): BookData {
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
        loadMoreSearchJob?.cancel()

        if (query.isBlank()) {
            updateState { 
                it.copy(
                    searchResults = emptyList(), 
                    isSearching = false,
                    searchPage = 1,
                    isLastSearchPage = false,
                    currentSearchQuery = ""
                ) 
            }
            return
        }

        searchJob = viewModelScope.launch {
            delay(300) // 디바운싱
            updateState { 
                it.copy(
                    isSearching = true,
                    searchResults = emptyList(),
                    searchPage = 1,
                    isLastSearchPage = false,
                    currentSearchQuery = query
                ) 
            }

            try {
                val result = bookRepository.searchBooks(query, page = 1, isFinalized = false)
                result.onSuccess { response ->
                    if (response != null) {
                        val searchResults = response.searchResult.map { it.toBookData() }
                        updateState {
                            it.copy(
                                searchResults = searchResults,
                                searchPage = response.page,
                                isLastSearchPage = response.last,
                                isSearching = false
                            )
                        }
                    } else {
                        updateState {
                            it.copy(
                                searchResults = emptyList(),
                                isSearching = false,
                                isLastSearchPage = true
                            )
                        }
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

    fun loadMoreSearchResults() {
        val currentState = _uiState.value
        if (currentState.isLoadingMoreSearchResults || 
            currentState.isSearching ||
            currentState.isLastSearchPage || 
            currentState.searchResults.isEmpty() ||
            currentState.currentSearchQuery.isBlank()) {
            return
        }

        loadMoreSearchJob?.cancel()
        loadMoreSearchJob = viewModelScope.launch {
            updateState { it.copy(isLoadingMoreSearchResults = true) }
            
            try {
                val nextPage = currentState.searchPage + 1
                val result = bookRepository.searchBooks(
                    currentState.currentSearchQuery,
                    page = nextPage, 
                    isFinalized = false
                )
                result.onSuccess { response ->
                    if (response != null) {
                        val newResults = response.searchResult.map { it.toBookData() }
                        updateState {
                            it.copy(
                                searchResults = currentState.searchResults + newResults,
                                searchPage = response.page,
                                isLastSearchPage = response.last,
                                isLoadingMoreSearchResults = false
                            )
                        }
                    } else {
                        updateState {
                            it.copy(
                                isLoadingMoreSearchResults = false,
                                isLastSearchPage = true
                            )
                        }
                    }
                }.onFailure {
                    updateState {
                        it.copy(isLoadingMoreSearchResults = false)
                    }
                }
            } catch (e: Exception) {
                updateState {
                    it.copy(isLoadingMoreSearchResults = false)
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

        // 수정 모드에서는 새 이미지 추가 불가
        if (currentState.isEditMode) return

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

    fun removeExistingImage(index: Int) {
        val currentExistingImages = _uiState.value.existingImageUrls.toMutableList()
        if (index in currentExistingImages.indices) {
            currentExistingImages.removeAt(index)
            updateState { it.copy(existingImageUrls = currentExistingImages) }
        }
    }

    fun togglePrivate(isPrivate: Boolean) {
        updateState { it.copy(isPrivate = isPrivate) }
    }

    fun selectCategory(index: Int) {
        updateState {
            it.copy(
                selectedCategoryIndex = index
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

    fun createOrUpdateFeed(onSuccess: (Long) -> Unit, onError: (String) -> Unit) {
        val currentState = _uiState.value

        if (currentState.isEditMode && currentState.editingFeedId != null) {
            updateFeed(currentState.editingFeedId, onSuccess, onError)
        } else {
            createFeed(onSuccess, onError)
        }
    }

    fun createFeed(onSuccess: (Long) -> Unit, onError: (String) -> Unit) {
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
                        onSuccess(feedId.toLong())
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

    private fun updateFeed(feedId: Long, onSuccess: (Long) -> Unit, onError: (String) -> Unit) {
        val currentState = _uiState.value

        if (!currentState.isFormValid) {
            onError(stringResourceProvider.getString(R.string.error_form_validation))
            return
        }

        viewModelScope.launch {
            try {
                updateState { it.copy(isLoading = true, errorMessage = null) }

                val result = feedRepository.updateFeed(
                    feedId = feedId,
                    contentBody = currentState.feedContent.trim(),
                    isPublic = !currentState.isPrivate,
                    tagList = currentState.selectedTags,
                    remainImageUrls = currentState.existingImageUrls
                )

                result.onSuccess { response ->
                    val updatedFeedId = response?.feedId ?: feedId
                    onSuccess(updatedFeedId.toLong())
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

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
        loadMoreSearchJob?.cancel()
    }
}