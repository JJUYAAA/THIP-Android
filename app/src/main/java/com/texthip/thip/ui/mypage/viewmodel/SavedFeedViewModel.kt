package com.texthip.thip.ui.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.texthip.thip.data.model.feed.response.AllFeedItem
import com.texthip.thip.data.repository.FeedRepository
import com.texthip.thip.ui.mypage.mock.FeedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedFeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository
) : ViewModel() {

    private val _feeds = MutableStateFlow<List<FeedItem>>(emptyList())
    val feeds = _feeds.asStateFlow()

    init {
        viewModelScope.launch {
            feedRepository.feedStateUpdateResult.collect { update ->
                _feeds.value = _feeds.value.map { feedItem ->
                    if (feedItem.id == update.feedId) {
                        feedItem.copy(
                            isLiked = update.isLiked,
                            likeCount = update.likeCount,
                            isSaved = update.isSaved
                        )
                    } else {
                        feedItem
                    }
                }
            }
        }
    }

    fun loadSavedFeeds() {
        viewModelScope.launch {
            feedRepository.getSavedFeeds()
                .onSuccess { response ->
                    _feeds.value = response?.feedList?.map { it.toFeedItem() } ?: emptyList()
                }
                .onFailure {
                    it.printStackTrace()
                }
        }
    }

    fun toggleLike(feedId: Long) {
        viewModelScope.launch {
            val feed = _feeds.value.find { it.id == feedId } ?: return@launch
            feedRepository.changeFeedLike(
                feedId = feed.id,
                newLikeStatus = !feed.isLiked,
                currentLikeCount = feed.likeCount,
                currentIsSaved = feed.isSaved
            )
        }
    }

    fun toggleBookmark(feedId: Long) {
        viewModelScope.launch {
            val feed = _feeds.value.find { it.id == feedId } ?: return@launch
            feedRepository.changeFeedSave(
                feedId = feed.id,
                newSaveStatus = !feed.isSaved,
                currentIsLiked = feed.isLiked,
                currentLikeCount = feed.likeCount
            ).onSuccess {
                it?.isSaved?.let { it1 ->
                    if (!it1) {
                        _feeds.value = _feeds.value.filter { feedItem -> feedItem.id != feedId }
                    }
                }
            }
        }
    }
}

private fun AllFeedItem.toFeedItem(): FeedItem {
    return FeedItem(
        id = this.feedId.toLong(),
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
        isLocked = false,
        imageUrls = this.contentUrls
    )
}