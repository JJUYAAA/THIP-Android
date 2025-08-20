package com.texthip.thip.ui.group.room.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import coil.compose.AsyncImage
import com.texthip.thip.R
import com.texthip.thip.data.model.rooms.response.RecommendRoomResponse
import com.texthip.thip.data.model.rooms.response.RoomRecruitingResponse
import com.texthip.thip.ui.common.cards.CardItemRoomSmall
import com.texthip.thip.ui.common.cards.CardRoomBook
import com.texthip.thip.ui.common.modal.DialogPopup
import com.texthip.thip.ui.common.modal.ToastWithDate
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.myroom.mock.GroupBottomButtonType
import com.texthip.thip.ui.group.room.viewmodel.GroupRoomRecruitUiState
import com.texthip.thip.ui.group.room.viewmodel.GroupRoomRecruitViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.utils.color.hexToColor
import kotlinx.coroutines.delay

@Composable
fun GroupRoomRecruitScreen(
    roomId: Int,
    onRecommendationClick: (RecommendRoomResponse) -> Unit = {},
    onNavigateToGroupScreen: (String) -> Unit = {}, // GroupScreen으로 네비게이션 + 토스트 메시지
    onBackClick: () -> Unit = {}, // 뒤로가기
    onBookDetailClick: (String) -> Unit = {}, // 책 상세 화면으로 이동
    onNavigateToPasswordScreen: (Int) -> Unit = {}, // 비밀번호 입력 화면으로 이동
    onNavigateToRoomPlayingScreen: (Int) -> Unit = {}, // 기록장 화면으로 이동
    viewModel: GroupRoomRecruitViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // 데이터 로딩
    LaunchedEffect(roomId) {
        viewModel.loadRoomDetail(roomId)
    }

    // GroupScreen으로 네비게이션
    LaunchedEffect(uiState.shouldNavigateToGroupScreen, uiState.toastMessage) {
        if (uiState.shouldNavigateToGroupScreen) {
            onNavigateToGroupScreen(uiState.toastMessage)
            viewModel.onNavigatedToGroupScreen()
        }
    }

    // 기록장 화면으로 네비게이션
    LaunchedEffect(uiState.shouldNavigateToRoomPlayingScreen, uiState.roomId) {
        if (uiState.shouldNavigateToRoomPlayingScreen) {
            val roomIdValue = uiState.roomId
            if (roomIdValue != null) {
                onNavigateToRoomPlayingScreen(roomIdValue)
                viewModel.onNavigatedToRoomPlayingScreen()
            }
        }
    }

    GroupRoomRecruitContent(
        uiState = uiState,
        onRecommendationClick = onRecommendationClick,
        onBackClick = onBackClick,
        onBookDetailClick = onBookDetailClick,
        onParticipationClick = {
            // 비밀방이면 비밀번호 화면으로, 공개방이면 바로 참여
            val detail = uiState.roomDetail
            if (detail != null && !detail.isPublic) {
                onNavigateToPasswordScreen(detail.roomId)
            } else {
                viewModel.onParticipationClick()
            }
        },
        onCancelParticipationClick = { title, description ->
            viewModel.onCancelParticipationClick(
                title,
                description
            )
        },
        onCloseRecruitmentClick = { title, description ->
            viewModel.onCloseRecruitmentClick(
                title,
                description
            )
        },
        onDialogConfirm = { viewModel.onDialogConfirm() },
        onDialogCancel = { viewModel.onDialogCancel() },
        onHideToast = { viewModel.hideToast() }
    )
}

@Composable
fun GroupRoomRecruitContent(
    uiState: GroupRoomRecruitUiState,
    onRecommendationClick: (RecommendRoomResponse) -> Unit = {},
    onBackClick: () -> Unit = {},
    onBookDetailClick: (String) -> Unit = {},
    onParticipationClick: () -> Unit = {},
    onCancelParticipationClick: (String, String) -> Unit = { _, _ -> },
    onCloseRecruitmentClick: (String, String) -> Unit = { _, _ -> },
    onDialogConfirm: () -> Unit = {},
    onDialogCancel: () -> Unit = {},
    onHideToast: () -> Unit = {}
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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

        // 데이터가 없는 경우
        val detail = uiState.roomDetail ?: return@Box

        AsyncImage(
            model = detail.roomImageUrl,
            contentDescription = "모임방 배경 이미지",
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
                        .verticalScroll(scrollState),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp).padding(top = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = detail.roomName,
                            style = typography.bigtitle_b700_s22_h24,
                            color = colors.White
                        )
                        if (!detail.isPublic) {
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
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 40.dp),
                        text = stringResource(R.string.group_room_desc),
                        style = typography.menu_sb600_s14_h24,
                        color = colors.White,
                    )

                    Text(
                        text = detail.roomDescription,
                        style = typography.copy_r400_s12_h20,
                        color = colors.Grey,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 5.dp, bottom = 20.dp)
                    )

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
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
                                    detail.progressStartDate,
                                    detail.progressEndDate
                                ),
                                style = typography.timedate_r400_s11,
                                color = colors.Grey
                            )
                        }

                        //참여 인원
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(start = 50.dp)
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
                                modifier = Modifier
                                    .padding(top = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.group_room_screen_participant_count,
                                        detail.memberCount
                                    ),
                                    style = typography.menu_sb600_s12,
                                    color = colors.White
                                )
                                Spacer(Modifier.width(2.dp))
                                Text(
                                    text = stringResource(
                                        R.string.group_room_screen_participant_count_max,
                                        detail.recruitCount
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
                            .padding(horizontal = 20.dp)
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
                                    text = detail.recruitEndDate,
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
                                    text = detail.category,
                                    style = typography.info_m500_s12,
                                    color = hexToColor(detail.categoryColor)
                                )
                            }
                        }
                    }

                    //읽을 책 정보
                    CardRoomBook(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        title = detail.bookTitle,
                        author = detail.authorName,
                        publisher = detail.publisher,
                        description = detail.bookDescription,
                        imageUrl = detail.bookImageUrl,
                        onClick = { onBookDetailClick(detail.isbn) }
                    )

                    // 추천 모임방이 있을 때만 표시
                    if (detail.recommendRooms.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(top = 40.dp, start = 20.dp),
                            text = stringResource(R.string.group_recommend),
                            style = typography.smalltitle_sb600_s18_h24,
                            color = colors.White
                        )

                        //추천 모임방
                        LazyRow(
                            modifier = Modifier
                                .padding(top = 24.dp, start = 20.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            items(detail.recommendRooms) { rec ->
                                CardItemRoomSmall(
                                    title = rec.roomName,
                                    participants = rec.memberCount,
                                    maxParticipants = rec.recruitCount,
                                    endDate = rec.recruitEndDate,
                                    imageUrl = rec.bookImageUrl,
                                    onClick = { onRecommendationClick(rec) }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(70.dp))
                }
            }
        }

        // 하단 버튼
        val buttonType = uiState.currentButtonType
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
                            onParticipationClick()
                        }

                        GroupBottomButtonType.CANCEL -> {
                            onCancelParticipationClick(
                                context.getString(R.string.group_participant_cancel_popup),
                                context.getString(R.string.group_participant_cancel_comment)
                            )
                        }

                        GroupBottomButtonType.CLOSE -> {
                            onCloseRecruitmentClick(
                                context.getString(R.string.group_participant_close_popup),
                                context.getString(R.string.group_participant_close_comment)
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
        AnimatedVisibility(
            visible = uiState.showToast && !uiState.shouldNavigateToGroupScreen,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(durationMillis = 2000)
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(durationMillis = 2000)
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .zIndex(2f)
        ) {
            ToastWithDate(
                message = uiState.toastMessage
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
                    onConfirm = onDialogConfirm,
                    onCancel = onDialogCancel
                )
            }
        }
    }

    // 토스트 3초 후 자동 숨김 (GroupScreen으로 네비게이션 시에는 GroupScreen에서 관리)
    LaunchedEffect(uiState.showToast, uiState.shouldNavigateToGroupScreen) {
        if (uiState.showToast && !uiState.shouldNavigateToGroupScreen) {
            delay(3000)
            onHideToast()
        }
    }
}

@Preview(name = "참여 버튼 상태")
@Composable
fun GroupRoomRecruitScreenPreview() {
    ThipTheme {
        GroupRoomRecruitContent(
            uiState = GroupRoomRecruitUiState(
                isLoading = false,
                roomDetail = RoomRecruitingResponse(
                    isHost = false,
                    isJoining = false,
                    roomId = 1,
                    roomName = "🌙 미드나이트 라이브러리 함께읽기",
                    roomImageUrl = "https://picsum.photos/400/600?1",
                    isPublic = false,
                    progressStartDate = "2025.02.01",
                    progressEndDate = "2025.02.28",
                    recruitEndDate = "D-5",
                    category = "문학",
                    categoryColor = "#8B7CF6",
                    roomDescription = "매트 헤이그의 미드나이트 라이브러리를 함께 읽으며 인생의 가능성과 선택에 대해 이야기해요. 각자의 삶에서 후회했던 순간들을 공유하고, 서로 위로하며 성장하는 시간을 가져보아요. 따뜻한 마음으로 서로의 이야기를 들어주실 분들과 함께하고 싶습니다.",
                    memberCount = 18,
                    recruitCount = 20,
                    isbn = "9788937477263",
                    bookImageUrl = "https://picsum.photos/300/400?book1",
                    bookTitle = "미드나이트 라이브러리",
                    authorName = "매트 헤이그",
                    bookDescription = "삶과 죽음 사이, 후회와 가능성 사이에서 펼쳐지는 놀라운 이야기. 인생의 무한한 가능성을 탐험하는 감동적인 소설",
                    publisher = "인플루엔셜",
                    recommendRooms = listOf(
                        RecommendRoomResponse(
                            roomId = 2,
                            bookImageUrl = "https://picsum.photos/300/400?rec1",
                            roomName = "📚 현대문학 깊이 탐구하기",
                            memberCount = 12,
                            recruitCount = 15,
                            recruitEndDate = "D-3"
                        ),
                        RecommendRoomResponse(
                            roomId = 3,
                            bookImageUrl = "https://picsum.photos/300/400?rec2",
                            roomName = "✨ 철학 소설로 삶을 되돌아보기",
                            memberCount = 8,
                            recruitCount = 12,
                            recruitEndDate = "D-7"
                        ),
                        RecommendRoomResponse(
                            roomId = 4,
                            bookImageUrl = "https://picsum.photos/300/400?rec3",
                            roomName = "🎭 인간 심리를 다룬 소설 읽기",
                            memberCount = 15,
                            recruitCount = 18,
                            recruitEndDate = "D-2"
                        )
                    )
                ),
                currentButtonType = GroupBottomButtonType.JOIN,
                showDialog = false,
                showToast = false,
                toastMessage = "",
                dialogTitle = "",
                dialogDescription = "",
                shouldNavigateToGroupScreen = false
            )
        )
    }
}
