package com.texthip.thip.ui.mypage.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.texthip.thip.ui.mypage.viewmodel.SavedFeedViewModel

@Composable
fun FeedContent(viewModel: SavedFeedViewModel = viewModel()) {
    val feedList by viewModel.feeds.collectAsState()

    LazyColumn {
        items(feedList, key = { it.id }) { feed ->
            val bookImagePainter = feed.imageUrl?.let { painterResource(it) }
            val profileImagePainter = feed.userProfileImage?.let { painterResource(it) }

            SavedFeedCard(
                feedItem = feed,
                bookImage = bookImagePainter,
                profileImage = profileImagePainter,
                onBookmarkClick = { viewModel.toggleBookmark(feed.id) },
                onLikeClick = { viewModel.toggleLike(feed.id) }
            )
        }
    }
}