package com.texthip.thip.ui.myPage.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.HeaderButton
import com.texthip.thip.ui.common.cards.NotificationCard
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.myPage.data.ReactionItem
import com.texthip.thip.ui.theme.Black

@Composable
fun ReactionsScreen() {
    //추후 뷰모델로 수정 예정
    val reactions = listOf(
        ReactionItem("@user 1님의 피드 글","어쩌구 저쩌구 콘텐츠 내용입니다.어쩌구 저쩌구 콘텐츠 내용입니다.어쩌구 저쩌구 콘텐츠 내용입니다.", "2",false),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "7", true),
        ReactionItem("@user 3님의 기록", "어쩌구 저쩌구 콘텐츠 내용입니다.", "17", false),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "7", true),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "7", true),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "7", true),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "7", true),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "7", true),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "7", true),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "7", true),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "7", true),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "7", true)
    )
    Scaffold(
        containerColor = Black,
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.reactions),
                onLeftClick = {},
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 20.dp)) {
                HeaderButton(
                    text = stringResource(R.string.likes),
                    enabled = false
                )
                Spacer(modifier = Modifier.width(12.dp))
                HeaderButton(
                    text = stringResource(R.string.comments),
                    enabled = false
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reactions) { item ->
                    NotificationCard(
                        title = item.title,
                        message = item.message,
                        timeAgo = item.timeAgo,
                        isRead = item.isRead,
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ReactionsScreenPrev() {
    ReactionsScreen()
}