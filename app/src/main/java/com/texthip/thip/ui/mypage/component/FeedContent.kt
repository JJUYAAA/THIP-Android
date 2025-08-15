package com.texthip.thip.ui.mypage.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.mypage.mock.FeedItem
import com.texthip.thip.ui.mypage.viewmodel.SavedFeedViewModel
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun FeedContent(
    feedList: List<FeedItem>, viewModel: SavedFeedViewModel

) {
    if (feedList.isEmpty()) {
        EmptyFeedContent()
    } else {
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
        ){
            itemsIndexed(feedList, key = { _,feed -> feed.id }) { index,feed ->
                if (index == 0) {
                    Spacer(Modifier.height(32.dp))
                }

                SavedFeedCard(
                    feedItem = feed,
                    onBookmarkClick = { viewModel.toggleBookmark(feed.id) },
                    onLikeClick = { viewModel.toggleLike(feed.id) }
                )

                if (index != feedList.lastIndex) {
                    Spacer(Modifier.height(40.dp))
                    HorizontalDivider(
                        color = colors.DarkGrey03,
                        thickness = 6.dp
                    )
                    Spacer(Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun EmptyFeedContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.no_saved_feed),
            style = typography.smalltitle_sb600_s18_h24,
            color = colors.White
        )
        Text(
            text = stringResource(R.string.do_thip_feed),
            style = typography.feedcopy_r400_s14_h20,
            color = colors.Grey,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}