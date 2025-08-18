package com.texthip.thip.ui.common.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.data.model.rooms.response.TodayCommentList
import com.texthip.thip.ui.common.header.ProfileBarWithDate
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun CardCommentGroup(
    data: TodayCommentList,
    onMenuClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        ProfileBarWithDate(
            profileImage = data.creatorProfileImageUrl,
            nickname = data.creatorNickname,
            dateText = data.postDate,
            onMenuClick = onMenuClick
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = data.todayComment,
            style = typography.feedcopy_r400_s14_h20,
            color = colors.Grey

        )
    }
}

@Preview
@Composable
private fun CardCommentGroupPreview() {
    CardCommentGroup(
        data = TodayCommentList(
            attendanceCheckId = 1,
            creatorId = 1,
            creatorProfileImageUrl = "",
            creatorNickname = "user.01",
            todayComment = "이것은 그룹 채팅의 댓글입니다. 이곳에 댓글 내용을 작성할 수 있습니다. 여러 줄로 작성해도 됩니다.",
            postDate = "11시간 전",
            date = "2025-08-18",
            isWriter = false
        ),
        onMenuClick = {}
    )
}