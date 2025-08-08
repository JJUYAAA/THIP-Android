package com.texthip.thip.ui.feed.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.common.header.AuthorHeader
import com.texthip.thip.ui.feed.mock.MySubscriptionData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun SearchPeopleResult(
    peopleList: List<MySubscriptionData>,
    onThipNumClick: (MySubscriptionData) -> Unit = {}
) {
    LazyColumn {
        itemsIndexed(peopleList) { index, user ->
            AuthorHeader(
                profileImage = user.profileImageUrl,
                nickname = user.nickname,
                badgeText = user.role,
                profileImageSize = 36.dp,
                showButton = false,
                showThipNum = true,
                thipNum = user.subscriberCount,
                onThipNumClick = { onThipNumClick(user) }
            )
            if (index < peopleList.size - 1) {
                Spacer(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colors.DarkGrey02)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPeopleResultPreview() {
    ThipTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SearchPeopleResult(
                peopleList = listOf(
                    MySubscriptionData(
                        profileImageUrl = null,
                        nickname = "Thiper_Official",
                        role = "공식 인플루언서",
                        roleColor = colors.NeonGreen,
                        subscriberCount = 50,
                        isSubscribed = false
                    ),
                    MySubscriptionData(
                        profileImageUrl = null,
                        nickname = "Thiper_Writer",
                        role = "작가",
                        roleColor = colors.Yellow,
                        subscriberCount = 120,
                        isSubscribed = true
                    ),
                    MySubscriptionData(
                        profileImageUrl = null,
                        nickname = "Thiper_Newbie",
                        role = "칭호칭호",
                        roleColor = colors.White,
                        subscriberCount = 0,
                        isSubscribed = false
                    )
                )
            )
        }
    }
}
