package com.texthip.thip.ui.feed.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.texthip.thip.R
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.feed.component.MySubscriptionList
import com.texthip.thip.ui.feed.mock.MySubscriptionData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun SubscriptionScreen(
    navController: NavController? = null,
    titleText: String = stringResource(R.string.thip_list)
) {
    val initialmembers = listOf(
        MySubscriptionData(
            profileImageUrl = null,
            nickname = "Thiper",
            role = "칭호칭호",
            roleColor = colors.Yellow,
            subscriberCount = 100
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
            subscriberCount = 0
        ),
        MySubscriptionData(
            profileImageUrl = null,
            nickname = "thip01",
            role = "작가",
            roleColor = colors.NeonGreen,
            subscriberCount = 1000
        ),
        MySubscriptionData(
            profileImageUrl = null,
            nickname = "Thiper",
            role = "칭호칭호",
            roleColor = colors.Yellow,
            subscriberCount = 500
        ),

        MySubscriptionData(
            profileImageUrl = null,
            nickname = "thipthip",
            role = "공식 인플루언서",
            roleColor = colors.NeonGreen,
            subscriberCount = 105
        ),
        MySubscriptionData(
            profileImageUrl = null,
            nickname = "Thiper",
            role = "칭호칭호",
            roleColor = colors.Yellow,
            subscriberCount = 8
        ),
        MySubscriptionData(
            profileImageUrl = null,
            nickname = "thip01",
            role = "작가",
            roleColor = colors.NeonGreen,
            subscriberCount = 100
        ),
        MySubscriptionData(
            profileImageUrl = null,
            nickname = "Thiper",
            role = "칭호칭호",
            roleColor = colors.Yellow,
            subscriberCount = 88
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
            subscriberCount = 1
        ),
    )

    var members by remember { mutableStateOf(initialmembers) }

    Column(
        Modifier
            .fillMaxSize()
    ) {
        DefaultTopAppBar(
            onLeftClick = {},
            title = titleText
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.whole_num, members.size),
                style = typography.menu_m500_s14_h24,
                color = colors.Grey,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, bottom = 4.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = colors.DarkGrey02,
                thickness = 1.dp
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    MySubscriptionList(
                        members = members,
                        isMine = false,
                        onUnsubscribe = {  }
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun SubscriptionListScreenPrev() {
    ThipTheme {
        SubscriptionScreen()
    }
}