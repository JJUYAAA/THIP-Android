package com.texthip.thip.ui.group.myroom.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.cards.CardItemRoomSmall
import com.texthip.thip.ui.common.cards.CardRoomBook
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.myroom.mock.GroupBookData
import com.texthip.thip.ui.group.myroom.mock.GroupBottomButtonType
import com.texthip.thip.ui.group.myroom.mock.GroupRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun GroupRoomScreen(
    detail: GroupRoomData,
    buttonType: GroupBottomButtonType,
    onBottomButtonClick: () -> Unit = {},
    onRecommendationClick: (GroupCardItemRoomData) -> Unit = {}
) {
    Box(Modifier.fillMaxSize()) {
        Column {
            DefaultTopAppBar(
                isRightIconVisible = false,
                isTitleVisible = false,
                onLeftClick = {},
            )

            Column(
                Modifier
                    .background(colors.Black)
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                // TODO: 배경 이미지 추가
                Spacer(modifier = Modifier.height(20.dp))

                //타이틀
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = detail.title,
                        style = typography.bigtitle_b700_s22_h24,
                        color = colors.White
                    )
                    if (detail.isSecret) {
                        Spacer(Modifier.width(2.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lock),
                            contentDescription = "비밀방",
                            tint = colors.White
                        )
                    } else {
                        Spacer(Modifier.width(2.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_unlock),
                            contentDescription = "오픈방",
                            tint = colors.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))

                //소개글
                Text(
                    text = stringResource(R.string.group_room_desc),
                    style = typography.menu_sb600_s14_h24,
                    color = colors.White,
                )

                Text(
                    text = detail.description,
                    style = typography.copy_r400_s12_h20,
                    color = colors.Grey,
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 18.dp)
                )


                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //모집 기간
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_calendar),
                                contentDescription = "모임 활동기간",
                                tint = colors.White
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                text = stringResource(R.string.group_period),
                                style = typography.view_m500_s12_h20,
                                color = colors.White
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = stringResource(
                                R.string.group_room_period,
                                detail.startDate,
                                detail.endDate
                            ),
                            style = typography.info_r400_s12,
                            color = colors.Grey
                        )
                    }

                    //참여 인원
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_group),
                                contentDescription = "참여 중인 독서 메이트",
                                tint = colors.White
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                text = stringResource(R.string.group_mate),
                                style = typography.view_m500_s12_h20,
                                color = colors.White
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.group_room_screen_participant_count,
                                    detail.members
                                ),
                                style = typography.info_r400_s12,
                                color = colors.White
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                text = stringResource(
                                    R.string.card_item_participant_count_max,
                                    detail.maxMembers
                                ),
                                style = typography.info_m500_s12,
                                color = colors.Grey
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 모집 마감/장르
                    Box(
                        Modifier
                            .background(colors.Grey03, shape = RoundedCornerShape(14.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row {
                            Text(
                                text = stringResource(R.string.group_recruiting),
                                style = typography.info_m500_s12,
                                color = colors.White
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = stringResource(
                                    R.string.group_room_screen_end_date,
                                    detail.daysLeft
                                ),
                                style = typography.info_m500_s12,
                                color = colors.NeonGreen
                            )
                        }

                    }
                    Spacer(Modifier.width(12.dp))
                    Box(
                        Modifier
                            .background(colors.Grey03, shape = RoundedCornerShape(14.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row {
                            Text(
                                text = stringResource(R.string.group_genre),
                                style = typography.info_m500_s12,
                                color = colors.White
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = detail.genre,
                                style = typography.info_m500_s12,
                                color = colors.genreColor
                            )
                        }

                    }
                }

                Spacer(Modifier.height(30.dp))

                //읽을 책 정보
                CardRoomBook(
                    title = detail.bookData.title,
                    author = detail.bookData.author,
                    publisher = detail.bookData.publisher,
                    description = detail.bookData.description,
                    imageRes = detail.bookData.imageRes
                )

                Spacer(Modifier.height(40.dp))
                Text(
                    text = stringResource(R.string.group_recommend),
                    style = typography.smalltitle_sb600_s18_h24,
                    color = colors.White
                )
                Spacer(Modifier.height(24.dp))

                //추천 모임방
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(detail.recommendations) { rec ->
                        CardItemRoomSmall(
                            title = rec.title,
                            participants = rec.participants,
                            maxParticipants = rec.maxParticipants,
                            endDate = rec.endDate,
                            imageRes = rec.imageRes,
                            onClick = { onRecommendationClick(rec) }
                        )
                    }
                }

                Spacer(Modifier.weight(1f))
            }
        }

        // 하단 버튼
        val buttonText = when (buttonType) {
            GroupBottomButtonType.JOIN -> stringResource(R.string.group_room_screen_participant)
            GroupBottomButtonType.CANCEL -> stringResource(R.string.group_room_screen_cancel)
            GroupBottomButtonType.CLOSE -> stringResource(R.string.group_room_screen_end)
        }
        val buttonColor = when (buttonType) {
            GroupBottomButtonType.JOIN -> colors.Purple
            GroupBottomButtonType.CANCEL -> colors.Red
            GroupBottomButtonType.CLOSE -> colors.Grey02
        }

        Button(
            onClick = onBottomButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(
                text = buttonText,
                style = typography.smalltitle_sb600_s18_h24,
                color = colors.White
            )
        }
    }
}

@Preview()
@Composable
fun GroupRoomDetailScreenPreview_AllCases() {
    ThipTheme {
        val recommendations = listOf(
            GroupCardItemRoomData(
                title = "일본 소설 좋아하는 사람들 일본 소설 좋아하는 사람들",
                participants = 19,
                maxParticipants = 25,
                isRecruiting = true,
                endDate = 2,
                genreIndex = 0
            ),
            GroupCardItemRoomData(
                title = "일본 소설 좋아하는 사람들 일본 소설 좋아하는 사람들",
                participants = 12,
                maxParticipants = 16,
                isRecruiting = true,
                endDate = 6,
                genreIndex = 0
            ),
            GroupCardItemRoomData(
                title = "일본 소설 좋아하는 사람들 일본 소설 좋아하는 사람들",
                participants = 30,
                maxParticipants = 30,
                isRecruiting = false,
                endDate = 0,
                genreIndex = 0
            ),
            GroupCardItemRoomData(
                title = "일본 소설 좋아하는 사람들 일본 소설 좋아하는 사람들",
                participants = 10,
                maxParticipants = 12,
                isRecruiting = true,
                endDate = 8,
                genreIndex = 0
            ),
            GroupCardItemRoomData(
                title = "에세이 나눔방",
                participants = 14,
                maxParticipants = 20,
                isRecruiting = true,
                endDate = 4,
                genreIndex = 0
            )
        )

        val bookData = GroupBookData(
            title = "심장보다 단단한 토마토 한 알",
            author = "고선지",
            publisher = "푸른출판사",
            description = "‘시집만 읽는 사람들’ 3월 모임에서 읽는 시집. 상처받고 단단해진 마음을 담은 감동적인 시와 해설이 어우러진 책으로, 읽는 이로 하여금 자신의 이야기를 투영하게 하는 힘이 있다.",
            imageRes = R.drawable.bookcover_sample
        )

        val detailJoin = GroupRoomData(
            title = "시집만 읽는 사람들 3월",
            isSecret = true,
            description = "‘시집만 읽는 사람들’ 3월 모임입니다. 이번 달 모임에서는 심장보다 단단한 토마토 한 알을 함께 읽어요.",
            startDate = "2025.01.12",
            endDate = "2025.02.12",
            members = 22,
            maxMembers = 30,
            daysLeft = 4,
            genre = "고전 문학",
            bookData = bookData,
            recommendations = recommendations
        )
        val detailCancel = detailJoin.copy(
            title = "참여 중인 독서모임",
            isSecret = false,
            members = 17
        )
        val detailHost = detailJoin.copy(
            title = "내가 호스트인 독서모임",
            isSecret = false,
            members = 30,
            maxMembers = 30
        )

        Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
            // 1. 참여 가능한 경우(참여하기)
            GroupRoomScreen(
                detail = detailJoin,
                buttonType = GroupBottomButtonType.JOIN,
                onBottomButtonClick = {}
            )
            // 2. 참여 중인 경우(참여 취소하기)
            GroupRoomScreen(
                detail = detailCancel,
                buttonType = GroupBottomButtonType.CANCEL,
                onBottomButtonClick = {}
            )
            // 3. 내가 호스트인 경우(모집 마감하기)
            GroupRoomScreen(
                detail = detailHost,
                buttonType = GroupBottomButtonType.CLOSE,
                onBottomButtonClick = {}
            )
        }
    }
}


