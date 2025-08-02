package com.texthip.thip.ui.feed.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.header.AuthorHeader
import com.texthip.thip.ui.feed.mock.MySubscriptionData
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun MySubscriptionList(
    members: List<MySubscriptionData>,
    isMine: Boolean=true,
    onUnsubscribe: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        members.forEachIndexed { index, member ->
            AuthorHeader(
                profileImage = member.profileImageUrl,
                nickname = member.nickname,
                badgeText = member.role,
                buttonText = stringResource(if(member.isSubscribed) R.string.thip_cancel else R.string.thip),
                buttonWidth = 64.dp,
                profileImageSize = 36.dp,
                showButton = isMine,
                showThipNum = !isMine,
                thipNum = member.subscriberCount,
                onButtonClick = {
                    onUnsubscribe(member.nickname)
                }
            )

            if (index != members.lastIndex) {
                HorizontalDivider(
                    color = colors.DarkGrey02,
                    thickness = 1.dp
                )
            }else{
                Spacer(modifier = Modifier.height(76.dp))
            }
        }
    }
}

@Preview
@Composable
private fun MySubscriptionListPrev() {
    MySubscriptionList(
        members = listOf(
            MySubscriptionData(
                profileImageUrl = null,
                nickname = "Thiper",
                role = "칭호칭호",
                roleColor = colors.Yellow,
                subscriberCount = 100,
            ),
            MySubscriptionData(
                profileImageUrl = null,
                nickname = "thipthip",
                role = "공식 인플루언서",
                roleColor = colors.NeonGreen,
                subscriberCount = 50
            ),
            MySubscriptionData(
                profileImageUrl = null,
                nickname = "Thiper",
                role = "칭호칭호",
                roleColor = colors.Yellow,
                subscriberCount = 100
            ),
            MySubscriptionData(
                profileImageUrl = null,
                nickname = "thip01",
                role = "작가",
                roleColor = colors.NeonGreen,
                subscriberCount = 100
            ),
        ),
        onUnsubscribe = {}
    )
}

@Preview
@Composable
private fun MySubscriptionListPrev2() {
    MySubscriptionList(
        members = listOf(
            MySubscriptionData(
                profileImageUrl = null,
                nickname = "Thiper",
                role = "칭호칭호",
                roleColor = colors.Yellow,
                subscriberCount = 100,
            ),
            MySubscriptionData(
                profileImageUrl = null,
                nickname = "thipthip",
                role = "공식 인플루언서",
                roleColor = colors.NeonGreen,
                subscriberCount = 50
            ),
            MySubscriptionData(
                profileImageUrl = null,
                nickname = "Thiper",
                role = "칭호칭호",
                roleColor = colors.Yellow,
                subscriberCount = 100
            ),
            MySubscriptionData(
                profileImageUrl = null,
                nickname = "thip01",
                role = "작가",
                roleColor = colors.NeonGreen,
                subscriberCount = 100
            ),
        ),
        isMine = false,
        onUnsubscribe = {}
    )
}