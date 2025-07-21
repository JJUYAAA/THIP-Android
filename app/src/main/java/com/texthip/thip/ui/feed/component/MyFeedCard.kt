package com.texthip.thip.ui.feed.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ActionBookButton
import com.texthip.thip.ui.mypage.mock.FeedItem
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun MyFeedCard(
    modifier: Modifier = Modifier,
    feedItem: FeedItem,
    bookImage: Painter? = null,
    onLikeClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            ActionBookButton(
                bookTitle = feedItem.bookTitle,
                bookAuthor = feedItem.authName,

                onClick = {}
            )
        }
        if (bookImage  != null) {
            Image(
                painter = bookImage ,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = feedItem.content,
            style = typography.feedcopy_r400_s14_h20,
            color = colors.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.clickable { onLikeClick() },
                painter = painterResource(if (feedItem.isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(
                text = feedItem.likeCount.toString(),
                style = typography.feedcopy_r400_s14_h20,
                color = colors.White,
                modifier = Modifier.padding(start = 5.dp, end = 12.dp)
            )
            Icon(
                painter = painterResource(R.drawable.ic_comment),
                contentDescription = null,
                tint = colors.White
            )
            Text(
                text = feedItem.commentCount.toString(),
                style = typography.feedcopy_r400_s14_h20,
                color = colors.White,
                modifier = Modifier.padding(start = 5.dp, end = 12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            if (feedItem.isLocked) {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }

    }
}

@Preview
@Composable
private fun MyFeedCardPrev() {
    val feed1 = FeedItem(
        id = 1,
        userProfileImage = R.drawable.character_literature,
        userName = "user.01",
        userRole = stringResource(R.string.influencer),
        bookTitle = "책 제목",
        authName = "한강",
        timeAgo = "3시간 전",
        content = "무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷",
        likeCount = 10,
        commentCount = 5,
        isLiked = false,
        isSaved = true,
        isLocked = true,
        imageUrl = null
    )

    val feed2 = FeedItem(
        id = 2,
        userProfileImage = R.drawable.character_art,
        userName = "user.01",
        userRole = stringResource(R.string.influencer),
        bookTitle = "책 제목",
        authName = "한강",
        timeAgo = "3시간 전",
        content = "한줄만 입력 가능",
        likeCount = 10,
        commentCount = 5,
        isLiked = false,
        isSaved = true,
        isLocked = false,
        imageUrl = R.drawable.bookcover_sample
    )

    Column {
        MyFeedCard(
            feedItem = feed1,
            bookImage = null
        )
        MyFeedCard(
            feedItem = feed2,
            bookImage = painterResource(feed2.imageUrl!!)
        )
    }


}