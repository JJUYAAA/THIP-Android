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

    fun loadFeedForEdit(feedId: Int) {
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
        feedId: Int,
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

    // 현재 로드된 내 피드 목록을 가져오는 헬퍼 함수 (FeedViewModel과 연동 필요)
    private fun getCurrentMyFeeds(): List<com.texthip.thip.data.model.feed.response.MyFeedItem> {
        // TODO: FeedViewModel에서 현재 내 피드 목록을 가져오는 방법 구현 필요
        // 임시로 빈 리스트 반환
        return emptyList()
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

    fun createOrUpdateFeed(onSuccess: (Int) -> Unit, onError: (String) -> Unit) {
        val currentState = _uiState.value

        if (currentState.isEditMode && currentState.editingFeedId != null) {
            updateFeed(currentState.editingFeedId, onSuccess, onError)
        } else {
            createFeed(onSuccess, onError)
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

    private fun updateFeed(feedId: Int, onSuccess: (Int) -> Unit, onError: (String) -> Unit) {
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
                    onSuccess(updatedFeedId)
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