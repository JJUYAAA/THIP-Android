package com.texthip.thip.ui.feed.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.texthip.thip.R
import com.texthip.thip.data.model.feed.response.FeedList
import com.texthip.thip.ui.common.buttons.ActionBarButton
import com.texthip.thip.ui.common.buttons.ActionBookButton
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun OthersFeedCard(
    modifier: Modifier = Modifier,
    feedItem: FeedList,
    onLikeClick: () -> Unit = {},
    onContentClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {}
) {
    val images = feedItem.contentUrls
    val hasImages = images.isNotEmpty()
    val maxLines = if (hasImages) 3 else 8

    var isTextTruncated by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onContentClick() }
    ) {
        ActionBookButton(
            bookTitle = feedItem.bookTitle,
            bookAuthor = feedItem.bookAuthor,
            onClick = {}
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
          .fillMaxWidth()
                .padding(vertical = 16.dp)
                .clickable { onContentClick() }
        ) {

            Text(
                text = feedItem.contentBody,
                style = typography.feedcopy_r400_s14_h20,
                color = colors.White,
                maxLines = maxLines,
                modifier = Modifier
                    .fillMaxWidth(),
                //.clickable { onContentClick() }
                onTextLayout = { textLayoutResult ->
                    isTextTruncated = textLayoutResult.hasVisualOverflow
                }
            )
            if (isTextTruncated) {
                Image(
                    painter = painterResource(id = R.drawable.ic_text_more),
                    contentDescription = null, // decorative image
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .width(80.dp)
                        .height(24.dp)
                )
            }
        }
               
        if (hasImages) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                images.take(3).forEach { image ->
                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(100.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        ActionBarButton(
            isLiked = feedItem.isLiked,
            likeCount = feedItem.likeCount,
            commentCount = feedItem.commentCount,
            isSaveVisible = true,
            isSaved = feedItem.isSaved,
            onLikeClick = onLikeClick,
            onCommentClick = onContentClick,
            onBookmarkClick = onBookmarkClick
        )
    }
}


@Preview
@Composable
private fun OthersFeedCardPreview() {
    val feed = FeedList(
        feedId = 1,
        postDate = "3시간 전",
        isbn = "12345", bookTitle = "미드나이트 라이브러리", bookAuthor = "매트 헤이그",
        contentBody = "피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.피드 내용입니다. 정말 재미있게 읽은 책이에요. 여러분도 꼭 읽어보세요.",
        contentUrls = listOf("https://picsum.photos/100"),
        likeCount = 10, commentCount = 5, isPublic = true,
        isSaved = true, isLiked = false, isWriter = false
    )
    ThipTheme {
        Column {
            OthersFeedCard(
                feedItem = feed
            )
            OthersFeedCard(
                feedItem = feed
            )
        }
    }


}