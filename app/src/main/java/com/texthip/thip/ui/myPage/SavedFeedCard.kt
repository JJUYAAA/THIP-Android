package com.texthip.thip.ui.myPage

import android.R.attr.top
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ActionBookButton
import com.texthip.thip.ui.common.header.ProfileBar
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.ui.theme.White

@Composable
fun SavedFeedCard(
    modifier: Modifier = Modifier,
    user_name: String,
    user_role: String,
    book_title: String,
    auth_name: String,
    imageRes: Painter? = null,
    time_ago: Int,
    content: String,
    like_count: Int,
    comment_count: Int,
    is_like: Boolean,
    is_saved: Boolean = true,
    onBookmarkClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        ProfileBar(
            profileImage = imageRes,
            topText = user_name,
            bottomText = user_role,
            showSubscriberInfo = false,
            hoursAgo = time_ago
        )
        ActionBookButton(
            bookTitle = book_title,
            bookAuthor = auth_name,
            onClick = {}
        )
        Text(
            text = content,
            style = typography.feedcopy_r400_s14_h20,
            color = White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp,bottom = 16.dp)
        )
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                painter = painterResource(R.drawable.ic_heart),
                contentDescription = null,
                tint = colors.White
            )
            Text(
                text = like_count.toString(),
                style = typography.feedcopy_r400_s14_h20,
                color = White,
                modifier = Modifier.padding(start = 5.dp,end = 12.dp)
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
                modifier = Modifier.padding(start = 5.dp,end = 12.dp)
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
    SavedFeedCard(
        user_name = "user.01",
        user_role = stringResource(R.string.influencer),
        book_title = "책 제목",
        auth_name = "한강",
        time_ago = 3,
        content = "무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷무한대로입력가능합니닷",
        like_count = 10,
        comment_count = 5,
        is_like = false
    )
}