package com.texthip.thip.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.R
import com.texthip.thip.data.model.book.response.BookDetailResponse
import com.texthip.thip.data.model.feed.response.RelatedFeedItem
import com.texthip.thip.data.provider.StringResourceProvider
import com.texthip.thip.ui.mypage.mock.FeedItem
import com.texthip.thip.data.repository.BookRepository
import com.texthip.thip.data.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class BookDetailUiState(
    val bookDetail: BookDetailResponse? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val relatedFeeds: List<RelatedFeedItem> = emptyList(),
    val isLoadingFeeds: Boolean = false,
    val isLoadingMore: Boolean = false,
    val nextCursor: String? = null,
    val isLast: Boolean = false,
    val currentSort: String = "like",
    val feedError: String? = null
) {
    val feedItems: List<FeedItem>
        get() = relatedFeeds.map { it.toFeedItem() }
}

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val feedRepository: FeedRepository,
    private val stringResourceProvider: StringResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookDetailUiState())
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()

    private fun updateState(update: (BookDetailUiState) -> BookDetailUiState) {
        _uiState.value = update(_uiState.value)
    }

    fun loadBookDetail(isbn: String) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, error = null) }

            bookRepository.getBookDetail(isbn)
                .onSuccess { bookDetail ->
                    updateState {
                        it.copy(
                            bookDetail = bookDetail,
                            isLoading = false,
                            error = null
                        )
                    }
                    // 책 상세 정보 로드 후 관련 피드도 로드
                    loadRelatedFeeds(isbn, "like")
                }
                .onFailure { exception ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                                ?: stringResourceProvider.getString(R.string.error_book_detail_load_failed)
                        )
                    }
                }
        }
    }

    fun loadRelatedFeeds(isbn: String, sort: String = "like") {
        viewModelScope.launch {
            updateState { it.copy(isLoadingFeeds = true) }

            feedRepository.getRelatedBookFeeds(isbn, sort, null)
                .onSuccess { response ->
                    updateState {
                        it.copy(
                            relatedFeeds = response?.feeds ?: emptyList(),
                            nextCursor = response?.nextCursor,
                            isLast = response?.isLast ?: true,
                            isLoadingFeeds = false,
                            currentSort = sort
                        )
                    }
                }
                .onFailure { exception ->
                    updateState {
                        it.copy(
                            isLoadingFeeds = false,
                            feedError = exception.message
                        )
                    }
                }
        }
    }

    fun loadMoreFeeds(isbn: String) {
        val currentState = _uiState.value
        if (currentState.isLoadingMore || currentState.isLast || currentState.nextCursor == null) return

        viewModelScope.launch {
            updateState { it.copy(isLoadingMore = true) }

            feedRepository.getRelatedBookFeeds(
                isbn,
                currentState.currentSort,
                currentState.nextCursor
            )
                .onSuccess { response ->
                    updateState {
                        it.copy(
                            relatedFeeds = currentState.relatedFeeds + (response?.feeds
                                ?: emptyList()),
                            nextCursor = response?.nextCursor,
                            isLast = response?.isLast ?: true,
                            isLoadingMore = false
                        )
                    }
                }
                .onFailure { exception ->
                    updateState {
                        it.copy(
                            isLoadingMore = false,
                            feedError = exception.message
                        )
                    }
                }
        }
    }

    fun changeSortOrder(isbn: String, sort: String) {
        if (_uiState.value.currentSort != sort) {
            loadRelatedFeeds(isbn, sort)
        }
    }

    fun saveBook(isbn: String, type: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(isSaving = true, error = null) }

            bookRepository.saveBook(isbn, type)
                .onSuccess { saveResponse ->
                    saveResponse?.let { it ->
                        // 책 상세 정보의 isSaved 상태 업데이트
                        val updatedBookDetail =
                            _uiState.value.bookDetail?.copy(isSaved = it.isSaved)
                        updateState {
                            it.copy(
                                bookDetail = updatedBookDetail,
                                isSaving = false,
                                error = null
                            )
                        }
                    }
                }
                .onFailure { exception ->
                    updateState {
                        it.copy(
                            isSaving = false,
                            error = exception.message
                                ?: stringResourceProvider.getString(R.string.error_book_save_failed)
                        )
                    }
                }
        }
    }
}

// RelatedFeedItem을 FeedItem으로 변환하는 확장 함수
private fun RelatedFeedItem.toFeedItem(): FeedItem {
    return FeedItem(
        id = this.feedId,
        userProfileImage = this.creatorProfileImageUrl,
        userName = this.creatorNickname,
        userRole = this.aliasName,
        bookTitle = this.bookTitle,
        authName = this.bookAuthor,
        timeAgo = this.postDate,
        content = this.contentBody,
        likeCount = this.likeCount,
        commentCount = this.commentCount,
        isLiked = this.isLiked,
        isSaved = this.isSaved,
        imageUrls = this.contentUrls
    )
}