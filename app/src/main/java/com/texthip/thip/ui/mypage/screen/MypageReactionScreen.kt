package com.texthip.thip.ui.mypage.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.OptionChipButton
import com.texthip.thip.ui.common.alarmpage.component.CardAlarm
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.mypage.mock.ReactionItem
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

/*@Composable
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

    Column(
        Modifier
            .background(colors.Black)
            .fillMaxSize()
    ) {
        DefaultTopAppBar(
            title = stringResource(R.string.reactions),
            onLeftClick = {},
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 20.dp)) {
                OptionChipButton(
                    text = stringResource(R.string.likes),
                    isFilled = true,
                    onClick = {isLikesSelected = !isLikesSelected}
                )
                Spacer(modifier = Modifier.width(12.dp))
                OptionChipButton(
                    text = stringResource(R.string.comments),
                    isFilled = true,
                    onClick = {isCommentsSelected = !isCommentsSelected}
                )

            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                itemsIndexed (filteredReactions) { index, item ->
                    val isLastItem = index == filteredReactions.lastIndex
                    CardAlarm(
                        title = item.title,
                        message = item.message,
                        timeAgo = item.timeAgo,
                        isRead = true,
                        badgeText = item.type,
                        onClick = {},
                        modifier = if (isLastItem) {
                            Modifier.padding(bottom = 20.dp)
                        } else {
                            Modifier
                        }
                    )
                }
            }
        }
    }
}*/
@Composable
fun ReactionsScreen(
    reactions : List<ReactionItem>
) {
    var isLikesSelected by remember { mutableStateOf(false) }
    var isCommentsSelected by remember { mutableStateOf(false) }

    val filteredReactions = remember(isLikesSelected, isCommentsSelected, reactions) {
        val showAll = (isLikesSelected && isCommentsSelected) || (!isLikesSelected && !isCommentsSelected)
        if (showAll) {
            reactions
        } else {
            reactions.filter { item ->
                if (isLikesSelected) item.type == "좋아요" else item.type == "댓글"
            }
        }
    }

    Column(
        Modifier
            .background(colors.Black)
            .fillMaxSize()
    ) {
        DefaultTopAppBar(
            title = stringResource(R.string.reactions),
            onLeftClick = {},
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 20.dp)) {
                OptionChipButton(
                    text = stringResource(R.string.likes),
                    isFilled = true,
                    onClick = { isLikesSelected = !isLikesSelected }
                )
                Spacer(modifier = Modifier.width(12.dp))
                OptionChipButton(
                    text = stringResource(R.string.comments),
                    isFilled = true,
                    onClick = { isCommentsSelected = !isCommentsSelected }
                )
            }
            if (filteredReactions.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_saved_reaction),
                        style = typography.smalltitle_sb600_s18_h24,
                        color = colors.White
                    )
                    Text(
                        text = stringResource(R.string.save_first_reaction),
                        style = typography.feedcopy_r400_s14_h20,
                        color = colors.Grey,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    itemsIndexed (filteredReactions) { index, item ->
                        val isLastItem = index == filteredReactions.lastIndex
                        CardAlarm(
                            title = item.title,
                            message = item.message,
                            timeAgo = item.timeAgo,
                            isRead = true,
                            badgeText = item.type,
                            onClick = {},
                            modifier = if (isLastItem) {
                                Modifier.padding(bottom = 20.dp)
                            } else {
                                Modifier
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ReactionsScreenPrev() {
    val mockReactions = listOf(
        ReactionItem("@user 1님의 피드 글","어쩌구 저쩌구 콘텐츠 내용입니다.","좋아요", "2"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.","댓글","7"),
        ReactionItem("@user 3님의 기록", "어쩌구 저쩌구 콘텐츠 내용입니다.","댓글", "17"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "좋아요", "25"),
        ReactionItem("@user 1님의 피드 글","어쩌구 저쩌구 콘텐츠 내용입니다.","좋아요", "2"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.","댓글","7"),
        ReactionItem("@user 3님의 기록", "어쩌구 저쩌구 콘텐츠 내용입니다.","댓글", "17"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "좋아요", "25"),
        ReactionItem("@user 1님의 피드 글","어쩌구 저쩌구 콘텐츠 내용입니다.","좋아요", "2"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.","댓글","7"),
        ReactionItem("@user 3님의 기록", "어쩌구 저쩌구 콘텐츠 내용입니다.","댓글", "17"),
        ReactionItem("@user 1님의 글", "어쩌구 저쩌구 콘텐츠 내용입니다.", "좋아요", "25")
    )
    ThipTheme {
        ReactionsScreen(reactions = mockReactions)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ReactionsScreenEmptyPrev() {
    ThipTheme {
        ReactionsScreen(reactions = emptyList())
    }
}
