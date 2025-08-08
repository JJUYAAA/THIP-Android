package com.texthip.thip.ui.group.room.screen

import androidx.compose.foundation.Image
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.texthip.thip.R
import com.texthip.thip.ui.common.cards.CardItemRoomSmall
import com.texthip.thip.ui.common.cards.CardRoomBook
import com.texthip.thip.ui.common.modal.DialogPopup
import com.texthip.thip.ui.common.modal.ToastWithDate
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.myroom.mock.GroupBookData
import com.texthip.thip.ui.group.myroom.mock.GroupBottomButtonType
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomData
import com.texthip.thip.ui.group.room.viewmodel.GroupRoomRecruitViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import kotlinx.coroutines.delay

@Composable
fun GroupRoomRecruitScreen(
    roomId: Int,
    viewModel: GroupRoomRecruitViewModel = hiltViewModel(),
    mockRoomDetail: GroupRoomData? = null, // Preview용 mock 데이터
    onRecommendationClick: (GroupCardItemRoomData) -> Unit = {},
    onNavigateToGroupScreen: (String) -> Unit = {}, // GroupScreen으로 네비게이션 + 토스트 메시지
    onBackClick: () -> Unit = {} // 뒤로가기
) {
    val context = LocalContext.current
    
    val uiState by viewModel.uiState.collectAsState()
    
    // 데이터 로딩
    LaunchedEffect(roomId) {
        if (mockRoomDetail == null) {
            viewModel.loadRoomDetail(roomId)
        }
    }
    
    // GroupScreen으로 네비게이션
    LaunchedEffect(uiState.shouldNavigateToGroupScreen, uiState.toastMessage) {
        if (uiState.shouldNavigateToGroupScreen) {
            onNavigateToGroupScreen(uiState.toastMessage)
            viewModel.onNavigatedToGroupScreen()
        }
    }

    Box(Modifier.fillMaxSize()) {
        // 로딩 상태
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colors.White)
            }
            return@Box
        }
        
        // 데이터가 없는 경우 (Preview에서는 mock 데이터 사용)
        val detail = uiState.roomDetail ?: mockRoomDetail ?: return@Box
        
        Image(
            painter = painterResource(id = R.drawable.group_room_recruiting),
            contentDescription = "배경 이미지",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        // 그라데이션 페이드 오버레이 (상단과 하단이 더 어두움)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colors.Black.copy(alpha = 1f),
                            colors.Black.copy(alpha = 0.3f),
                            colors.Black.copy(alpha = 1f),
                            colors.Black.copy(alpha = 1f),
                            colors.Black.copy(alpha = 1f),
                            colors.Black.copy(alpha = 1f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column {
                DefaultTopAppBar(
                    isRightIconVisible = false,
                    isTitleVisible = false,
                    onLeftClick = onBackClick,
                )

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                ) {
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

                    Text(
                        modifier = Modifier.padding(top = 40.dp),
                        text = stringResource(R.string.group_room_desc),
                        style = typography.menu_sb600_s14_h24,
                        color = colors.White,
                    )

                    Text(
                        text = detail.description,
                        style = typography.copy_r400_s12_h20,
                        color = colors.Grey,
                        modifier = Modifier
                            .padding(top = 5.dp, bottom = 20.dp)
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

                            Text(
                                modifier = Modifier.padding(top = 12.dp),
                                text = stringResource(
                                    R.string.group_room_period,
                                    detail.startDate,
                                    detail.endDate
                                ),
                                style = typography.timedate_r400_s11,
                                color = colors.Grey
                            )
                        }

                        //참여 인원
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(end = 18.dp)
                        ) {
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
                            Row(
                                modifier = Modifier.padding(top = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.group_room_screen_participant_count,
                                        detail.members
                                    ),
                                    style = typography.menu_sb600_s12,
                                    color = colors.White
                                )
                                Spacer(Modifier.width(2.dp))
                                Text(
                                    text = stringResource(
                                        R.string.group_room_screen_participant_count_max,
                                        detail.maxMembers
                                    ),
                                    style = typography.info_m500_s12,
                                    color = colors.Grey
                                )
                            }
                        }
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 22.dp, bottom = 30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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

                    //읽을 책 정보
                    CardRoomBook(
                        title = detail.bookData.title,
                        author = detail.bookData.author,
                        publisher = detail.bookData.publisher,
                        description = detail.bookData.description,
                        imageUrl = detail.bookData.imageUrl
                    )

                    // 추천 모임방이 있을 때만 표시
                    if (detail.recommendations.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(top = 40.dp),
                            text = stringResource(R.string.group_recommend),
                            style = typography.smalltitle_sb600_s18_h24,
                            color = colors.White
                        )

                        //추천 모임방
                        LazyRow(
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            items(detail.recommendations) { rec ->
                                CardItemRoomSmall(
                                    title = rec.title,
                                    participants = rec.participants,
                                    maxParticipants = rec.maxParticipants,
                                    endDate = rec.endDate,
                                    imageUrl = rec.imageUrl,
                                    onClick = { onRecommendationClick(rec) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // 하단 버튼 (Preview에서는 mockRoomDetail의 buttonType 사용)
        val buttonType = uiState.currentButtonType ?: mockRoomDetail?.buttonType
        if (buttonType != null) {
            val buttonText = when (buttonType) {
                GroupBottomButtonType.JOIN -> stringResource(R.string.group_room_screen_participant)
                GroupBottomButtonType.CANCEL -> stringResource(R.string.group_room_screen_cancel)
                GroupBottomButtonType.CLOSE -> stringResource(R.string.group_room_screen_end)
            }

            Button(
                onClick = {
                    when (buttonType) {
                        GroupBottomButtonType.JOIN -> {
                            viewModel.onParticipationClick()
                        }

                        GroupBottomButtonType.CANCEL -> {
                            viewModel.onCancelParticipationClick(
                                dialogTitle = context.getString(R.string.group_participant_cancel_popup),
                                dialogDescription = context.getString(R.string.group_participant_cancel_comment)
                            )
                        }

                        GroupBottomButtonType.CLOSE -> {
                            viewModel.onCloseRecruitmentClick(
                                dialogTitle = context.getString(R.string.group_participant_close_popup),
                                dialogDescription = context.getString(R.string.group_participant_close_comment)
                            )
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.Purple
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

        // 토스트 팝업
        if (uiState.showToast && !uiState.shouldNavigateToGroupScreen) {
            ToastWithDate(
                message = uiState.toastMessage,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .zIndex(2f)
            )
        }

        if (uiState.showDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.Black.copy(alpha = 0.5f))
                    .zIndex(3f),
                contentAlignment = Alignment.Center
            ) {
                DialogPopup(
                    title = uiState.dialogTitle,
                    description = uiState.dialogDescription,
                    onConfirm = {
                        viewModel.onDialogConfirm()
                    },
                    onCancel = {
                        viewModel.onDialogCancel()
                    }
                )
            }
        }
    }

    // 토스트 3초 후 자동 숨김 (GroupScreen으로 네비게이션 시에는 GroupScreen에서 관리)
    LaunchedEffect(uiState.showToast, uiState.shouldNavigateToGroupScreen) {
        if (uiState.showToast && !uiState.shouldNavigateToGroupScreen) {
            delay(3000)
            viewModel.hideToast()
        }
    }
}

@Preview(name = "참여 버튼 상태")
@Composable
fun GroupRoomRecruitScreenPreview() {
    ThipTheme {
        val recommendations = listOf(
            GroupCardItemRoomData(
                id = 1,
                title = "일본 소설 좋아하는 사람들",
                participants = 19,
                maxParticipants = 25,
                isRecruiting = true,
                endDate = 2,
            )
        )

        val bookData = GroupBookData(
            title = "심장보다 단단한 토마토 한 알",
            author = "고선지",
            publisher = "푸른출판사",
            description = "'시집만 읽는 사람들' 3월 모임에서 읽는 시집.",
            imageUrl = null
        )

        val detailJoin = GroupRoomData(
            id = 1,
            title = "시집만 읽는 사람들 3월",
            isSecret = true,
            description = "'시집만 읽는 사람들' 3월 모임입니다.",
            startDate = "2025.01.12",
            endDate = "2025.02.12",
            members = 22,
            maxMembers = 30,
            daysLeft = 4,
            genre = "문학",
            bookData = bookData,
            recommendations = recommendations,
            buttonType = GroupBottomButtonType.JOIN
        )

        GroupRoomRecruitScreen(
            roomId = 1,
            mockRoomDetail = detailJoin,
            onRecommendationClick = {},
            onNavigateToGroupScreen = {},
            onBackClick = {}
        )
    }
}