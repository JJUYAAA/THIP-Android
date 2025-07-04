package com.texthip.thip.ui.group.note.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ActionBarButton
import com.texthip.thip.ui.common.header.ProfileBar
import com.texthip.thip.ui.group.note.mock.GroupNoteRecord
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun TextCommentCard(
    data: GroupNoteRecord,
) {
    var isLiked by remember { mutableStateOf(data.isLiked) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProfileBar(
//            profileImage = data.profileImageUrl,
            profileImage = painterResource(R.drawable.character_literature),
            topText = data.nickName,
            bottomText = data.page.toString() + stringResource(R.string.page),
            bottomTextColor = colors.Purple,
            showSubscriberInfo = false,
//            hoursAgo = data.postDate
            hoursAgo = data.postDate
        )

        Text(
            text = data.content,
            style = typography.feedcopy_r400_s14_h20,
            color = colors.Grey,
        )

        ActionBarButton(
            isLiked = isLiked,
            likeCount = data.likeCount,
            commentCount = data.commentCount,
            onLikeClick = {
                isLiked = !isLiked
            },
            onCommentClick = {  },
        )
    }
}

@Preview
@Composable
fun TextCommentCardPreview() {
    TextCommentCard(
        data = GroupNoteRecord(
            page = 132,
            postDate = 12,
            userId = 1,
            nickName = "user.01",
            profileImageUrl = "https://example.com/profile.jpg",
            content = "내 생각에 이 부분이 가장 어려운 것 같다. 비유도 난해하고 잘 이해가 가지 않는데 다른 메이트들은 어떻게 읽었나요?",
            likeCount = 123,
            commentCount = 123,
            isLiked = true,
            isWriter = false,
            recordId = 1
        )
    )
}