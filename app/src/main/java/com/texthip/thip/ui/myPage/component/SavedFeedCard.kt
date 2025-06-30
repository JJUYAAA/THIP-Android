package com.texthip.thip.ui.myPage.component

import android.R.attr.contentDescription
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ActionBookButton
import com.texthip.thip.ui.common.header.ProfileBar
import com.texthip.thip.ui.theme.Red
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.ui.theme.White

@Composable
fun SavedFeedCard(
    modifier: Modifier = Modifier,
    user_name: String,
    user_role: String,
    user_profile_image: Painter? = null,
    book_title: String,
    auth_name: String,
    imageRes: Painter? = null,
    time_ago: Int,
    content: String,
    like_count: Int,
    comment_count: Int,
    is_like: Boolean,
    is_saved: Boolean = true,
    onBookmarkClick: () -> Unit = {},
    onLikeClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        ProfileBar(
            profileImage = user_profile_image,
            topText = user_name,
            bottomText = user_role,
            showSubscriberInfo = false,
            hoursAgo = time_ago
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            ActionBookButton(
                bookTitle = book_title,
                bookAuthor = auth_name,
                onClick = {})
        }
        if (imageRes != null) {
            Image(
                painter = imageRes,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = content,
            style = typography.feedcopy_r400_s14_h20,
            color = White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.clickable { onLikeClick() },
                painter = painterResource(R.drawable.ic_heart),
                contentDescription = null,
                tint = if (is_like) Red else colors.White
            )
            Text(
                text = like_count.toString(),
                style = typography.feedcopy_r400_s14_h20,
                color = White,
                modifier = Modifier.padding(start = 5.dp, end = 12.dp)
            )
            Icon(
                painter = painterResource(R.drawable.ic_comment),
                contentDescription = null,
                tint = colors.White
            )
            Text(
                text = comment_count.toString(),
                style = typography.feedcopy_r400_s14_h20,
                color = White,
                modifier = Modifier.padding(start = 5.dp, end = 12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.clickable { onBookmarkClick() },
                painter = painterResource(if (is_saved) R.drawable.ic_save_filled else R.drawable.ic_save),
                contentDescription = null,
                tint = colors.White
            )
        }

    }
}

@Preview
@Composable
private fun SavedFeedCardPrev() {
    Column {
        SavedFeedCard(
            user_name = "user.01",
            user_role = stringResource(R.string.influencer),
            book_title = "책 제목",
            auth_name = "한강",
            time_ago = 3,
            content = "무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷",
            like_count = 10,
            comment_count = 5,
            is_like = false,
            user_profile_image = R.drawable.character_literature.let { painterResource(it) }
        )
        SavedFeedCard(
            user_name = "user.01",
            user_role = stringResource(R.string.influencer),
            book_title = "책 제목",
            auth_name = "한강",
            time_ago = 3,
            content = "한줄만 입력 가능",
            like_count = 10,
            comment_count = 5,
            is_like = false,
            imageRes = R.drawable.bookcover_sample.let { painterResource(it) },
            user_profile_image = R.drawable.character_art.let { painterResource(it) }
        )
    }

}