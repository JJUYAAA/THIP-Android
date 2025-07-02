package com.texthip.thip.ui.group.room.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.common.header.ProfileBar
import com.texthip.thip.ui.group.room.mock.GroupRoomMateData
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun GroupRoomMatesList(
    members: List<GroupRoomMateData>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        members.forEachIndexed { index, member ->
            ProfileBar(
                profileImage = member.profileImageUrl,
                topText = member.nickname,
                bottomText = member.role,
                bottomTextColor = member.roleColor,
                showSubscriberInfo = true,
                subscriberCount = member.subscriberCount
            ) {}

            if (index != members.lastIndex) {
                HorizontalDivider(
                    color = colors.DarkGrey02,
                    thickness = 1.dp
                )
            }
        }
    }
}

@Preview
@Composable
private fun GroupRoomMatesListPreview() {
    GroupRoomMatesList(
        members = listOf(
            GroupRoomMateData(
                profileImageUrl = null,
                nickname = "Thiper",
                role = "칭호칭호",
                roleColor = colors.Yellow,
                subscriberCount = 100
            ),
            GroupRoomMateData(
                profileImageUrl = null,
                nickname = "thipthip",
                role = "공식 인플루언서",
                roleColor = colors.NeonGreen,
                subscriberCount = 50
            ),
            GroupRoomMateData(
                profileImageUrl = null,
                nickname = "Thiper",
                role = "칭호칭호",
                roleColor = colors.Yellow,
                subscriberCount = 100
            ),
            GroupRoomMateData(
                profileImageUrl = null,
                nickname = "thip01",
                role = "작가",
                roleColor = colors.NeonGreen,
                subscriberCount = 100
            ),
        )
    )
}