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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.OptionChipButton
import com.texthip.thip.ui.common.cards.CardAlarm
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.myPage.mock.ReactionItem
import com.texthip.thip.ui.theme.Black

@Composable
fun ReactionsScreen() {
    //추후 뷰모델로 수정 예정
    val reactions = listOf(
        ReactionItem("@user 1님의 피드 글","어쩌구 저쩌구 콘텐츠 내용입니다.어쩌구 저쩌구 콘텐츠 내용입니다.어쩌구 저쩌구 콘텐츠 내용입니다.","좋아요", "2"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.","댓글","7"),
        ReactionItem("@user 3님의 기록", "어쩌구 저쩌구 콘텐츠 내용입니다.","댓글", "17"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "좋아요", "25"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.","댓글", "7"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.","댓글", "7"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "좋아요", "7"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.","댓글", "7"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.","댓글", "7"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "좋아요", "7"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.","댓글", "7"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "좋아요", "7")
    )
    var isLikesSelected by remember { mutableStateOf(false) }
    var isCommentsSelected by remember { mutableStateOf(false) }
    val filteredReactions = reactions.filter { item ->
        when {
            isLikesSelected && isCommentsSelected -> true
            isLikesSelected -> item.type == stringResource(R.string.likes)
            isCommentsSelected -> item.type == stringResource(R.string.comments)
            else -> true
        }
    }

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
            //TODO card 컴포넌트 수정 후 적용 & 필터링 기능
            Row(modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 20.dp)) {
                OptionChipButton(
                    text = stringResource(R.string.likes),
                    isFilled = isLikesSelected,
                    onClick = {isLikesSelected = !isLikesSelected}
                )
                Spacer(modifier = Modifier.width(12.dp))
                OptionChipButton(
                    text = stringResource(R.string.comments),
                    isFilled = isCommentsSelected,
                    onClick = {isCommentsSelected = !isCommentsSelected}
                )

            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(filteredReactions) { item ->
                    CardAlarm(
                        title = item.title,
                        message = item.message,
                        timeAgo = item.timeAgo,
                        isRead = true,
                        badgeText = item.type,
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