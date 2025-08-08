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
import com.texthip.thip.data.model.rooms.response.RoomsUsersResponse
import com.texthip.thip.data.model.rooms.response.UserList
import com.texthip.thip.ui.common.header.ProfileBar
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun GroupRoomMatesList(
    members: RoomsUsersResponse,
    onUserClick: (Int) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        val userList = members.userList
        userList.forEachIndexed { index, member ->
            ProfileBar(
                profileImage = member.imageUrl,
                topText = member.nickname,
                bottomText = member.alias,
//                bottomTextColor = member.aliasColor,
                bottomTextColor = colors.ScienceIt, // TODO: 서버에서 보내주는 색상으로 수정
                showSubscriberInfo = true,
                subscriberCount = member.followerCount
            ) { onUserClick(member.userId) }

            if (index != userList.lastIndex) {
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
        members =
            RoomsUsersResponse(
                listOf(
                    UserList(
                        userId = 1,
                        nickname = "김희용",
                        alias = "문학가",
                        imageUrl = "https://example.com/image1.jpg",
                        followerCount = 100
                    ),
                    UserList(
                        userId = 2,
                        nickname = "노성준",
                        alias = "문학가",
                        imageUrl = "https://example.com/image1.jpg",
                        followerCount = 100
                    ),
                )
            )
    )
}