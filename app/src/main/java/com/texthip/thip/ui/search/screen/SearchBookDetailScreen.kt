package com.texthip.thip.ui.search.screen

import androidx.compose.animation.AnimatedVisibility
import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.data.model.book.response.BookDetailResponse
import com.texthip.thip.ui.search.viewmodel.BookDetailViewModel
import com.texthip.thip.ui.common.buttons.ActionMediumButton
import com.texthip.thip.ui.common.buttons.FilterButton
import com.texthip.thip.ui.common.modal.InfoPopup
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.common.topappbar.GradationTopAppBar
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import kotlinx.coroutines.delay

@Composable
fun SearchBookDetailScreen(
    modifier: Modifier = Modifier,
    isbn: String,
    feedList: List<String> = emptyList(),
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {},
    onRecruitingGroupClick: () -> Unit = {},
    onWriteFeedClick: () -> Unit = {},
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // ISBN으로 책 상세 정보 로드
    LaunchedEffect(isbn) {
        viewModel.loadBookDetail(isbn)
    }

    when {
        uiState.isLoading -> {
            SearchBookDetailScreenContent(
                modifier = modifier,
                isLoading = true,
                error = null,
                bookDetail = null,
                feedList = feedList,
                onLeftClick = onLeftClick,
                onRightClick = onRightClick,
                onRecruitingGroupClick = onRecruitingGroupClick,
                onWriteFeedClick = onWriteFeedClick,
                onBookmarkClick = { _, _ -> }
            )
        }
        uiState.error != null -> {
            SearchBookDetailScreenContent(
                modifier = modifier,
                isLoading = false,
                error = uiState.error!!,
                bookDetail = null,
                feedList = feedList,
                onLeftClick = onLeftClick,
                onRightClick = onRightClick,
                onRecruitingGroupClick = onRecruitingGroupClick,
                onWriteFeedClick = onWriteFeedClick,
                onBookmarkClick = { _, _ -> }
            )
        }
        uiState.bookDetail != null -> {
            SearchBookDetailScreenContent(
                modifier = modifier,
                isLoading = false,
                error = null,
                bookDetail = uiState.bookDetail!!,
                feedList = feedList,
                onLeftClick = onLeftClick,
                onRightClick = onRightClick,
                onRecruitingGroupClick = onRecruitingGroupClick,
                onWriteFeedClick = onWriteFeedClick,
                onBookmarkClick = { isbn, newState ->
                    viewModel.saveBook(isbn, newState)
                }
            )
        }
        else -> {}
    }
}

@Composable
private fun SearchBookDetailScreenContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null,
    bookDetail: BookDetailResponse? = null,
    feedList: List<String> = emptyList(),
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {},
    onRecruitingGroupClick: () -> Unit = {},
    onWriteFeedClick: () -> Unit = {},
    onBookmarkClick: (String, Boolean) -> Unit = { _, _ -> }
) {
    var isAlarmVisible by remember { mutableStateOf(true) }
    var isIntroductionPopupVisible by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(bookDetail?.isSaved ?: false) }
    var selectedFilterOption by remember { mutableIntStateOf(0) }

    val filterOptions = listOf(
        stringResource(R.string.search_filter_popular),
        stringResource(R.string.search_filter_latest)
    )

    // 알림 5초간 노출 (미리보기에서는 항상 보이도록)
    LaunchedEffect(Unit) {
        if (!isLoading && error == null && bookDetail != null) {
            isAlarmVisible = true
            delay(5000)
            isAlarmVisible = false
        }
    }

    // 북마크 상태 동기화
    LaunchedEffect(bookDetail?.isSaved) {
        bookDetail?.let { 
            isBookmarked = it.isSaved
        }
    }

    when {
        isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = colors.White
                )
            }
        }
        error != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error,
                    color = colors.White,
                    style = typography.smalltitle_sb600_s16_h20
                )
            }
        }
        bookDetail != null -> {
            Box(modifier = modifier.fillMaxSize()) {
                // 메인 컨텐츠
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (isIntroductionPopupVisible) {
                                Modifier.blur(4.dp)
                            } else {
                                Modifier
                            }
                        )
                ) {
                    // 실제 책 이미지 사용
                    Box(modifier = Modifier
                        .height(420.dp)
                        .fillMaxWidth()) {
                        AsyncImage(
                            model = bookDetail.imageUrl,
                            contentDescription = bookDetail.title,
                            modifier = Modifier
                                .matchParentSize()
                                .blur(4.dp),
                            contentScale = ContentScale.Crop,
                            fallback = painterResource(R.drawable.img_book_cover_sample),
                            error = painterResource(R.drawable.img_book_cover_sample)
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            colors.Black.copy(alpha = 0.3f),
                                            colors.Black.copy(alpha = 0.6f),
                                            colors.Black.copy(alpha = 0.9f),
                                            colors.Black
                                        ),
                                        startY = 0f,
                                        endY = Float.POSITIVE_INFINITY
                                    )
                                )
                        )
                    }

                    Column(modifier = Modifier.fillMaxSize()) {
                        AnimatedVisibility(visible = isAlarmVisible) {
                            GradationTopAppBar(
                                isImageVisible = true,
                                count = bookDetail.readCount,
                                onLeftClick = onLeftClick,
                                onRightClick = {}
                            )
                        }
                        AnimatedVisibility(visible = !isAlarmVisible) {
                            DefaultTopAppBar(
                                isRightIconVisible = true,
                                isTitleVisible = false,
                                onLeftClick = onLeftClick,
                                onRightClick = onRightClick
                            )
                        }

                        // 상세 정보 영역
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 40.dp),
                                text = bookDetail.title,
                                color = colors.White,
                                style = typography.bigtitle_b700_s22
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = stringResource(
                                    R.string.search_book_author,
                                    bookDetail.authorName,
                                    bookDetail.publisher
                                ),
                                color = colors.Grey,
                                style = typography.menu_sb600_s12_h20
                            )

                            Column(
                                modifier = Modifier
                                    .padding(top = 33.dp)
                                    .fillMaxWidth()
                                    .clickable { isIntroductionPopupVisible = true }
                            ) {
                                Text(
                                    text = stringResource(R.string.search_book_comment),
                                    color = colors.White,
                                    style = typography.menu_sb600_s14_h24,
                                )
                                Spacer(modifier = Modifier.height(5.dp))

                                Text(
                                    text = bookDetail.description,
                                    color = colors.Grey,
                                    style = typography.copy_r400_s12_h20,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.height(40.dp))
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                ActionMediumButton(
                                    text = stringResource(
                                        R.string.search_recruiting_group_count,
                                        bookDetail.recruitingRoomCount
                                    ),
                                    contentColor = colors.Grey,
                                    backgroundColor = Color.Transparent,
                                    hasRightIcon = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            width = 1.dp,
                                            color = colors.Grey,
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    onClick = onRecruitingGroupClick,
                                )
                                Row {
                                    ActionMediumButton(
                                        text = stringResource(R.string.search_write_feed_comment),
                                        contentColor = colors.White,
                                        backgroundColor = colors.Purple,
                                        hasRightIcon = true,
                                        hasRightPlusIcon = true,
                                        modifier = Modifier.weight(1f),
                                        onClick = onWriteFeedClick
                                    )
                                    Box(
                                        modifier = Modifier
                                            .padding(start = 12.dp)
                                            .size(44.dp)
                                            .border(
                                                width = 1.dp,
                                                color = colors.Grey02,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .background(
                                                color = Color.Transparent,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                val newBookmarkState = !isBookmarked
                                                isBookmarked = newBookmarkState
                                                onBookmarkClick(bookDetail.isbn, newBookmarkState)
                                            },
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            painter = painterResource(
                                                if (isBookmarked)
                                                    R.drawable.ic_save_filled
                                                else
                                                    R.drawable.ic_save
                                            ),
                                            contentDescription = null,
                                            tint = Color.Unspecified,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(44.dp))

                            Text(
                                text = stringResource(R.string.search_watch_feed),
                                color = colors.Grey,
                                style = typography.smalltitle_sb600_s18_h24,
                                modifier = Modifier.padding(bottom = 33.dp)
                            )

                            // 피드 리스트
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(colors.DarkGrey02)
                            )
                            if (feedList.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = stringResource(R.string.search_no_feed_comment_1),
                                            color = colors.White,
                                            style = typography.smalltitle_sb600_s18_h24
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = stringResource(R.string.search_no_feed_comment_2),
                                            color = colors.Grey01,
                                            style = typography.feedcopy_r400_s14_h20
                                        )
                                    }
                                }
                            } else {
                                // TODO: 피드 UI 구현 되면 수정
                            }
                        }
                    }
                }

                FilterButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 462.dp, start = 20.dp, end = 20.dp),
                    selectedOption = filterOptions[selectedFilterOption],
                    options = filterOptions,
                    onOptionSelected = { option ->
                        selectedFilterOption = filterOptions.indexOf(option)
                    }
                )

                if (isIntroductionPopupVisible) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 20.dp)
                    ) {
                        InfoPopup(
                            title = stringResource(R.string.introduction),
                            content = bookDetail.description,
                            onDismiss = { isIntroductionPopupVisible = false }
                        )
                    }
                }
            }
        }
        else -> {}
    }
}

// Preview용 Mock 데이터
private val mockBookDetail = BookDetailResponse(
    title = "데미안",
    imageUrl = "https://example.com/demian.jpg",
    authorName = "헤르만 헤세",
    publisher = "민음사",
    isbn = "9788954682152",
    description = "한 소년의 성장 이야기를 통해 인간의 내면 세계를 탐구한 헤르만 헤세의 대표작. 주인공 싱클레어가 겪는 정신적 성장과 자아 발견의 과정을 그린 소설로, 청소년기의 혼란과 성인으로의 성장을 섬세하게 그려낸다. 선악의 이분법을 넘어서서 인간 내면의 복잡성을 인정하고 받아들이는 과정을 통해 진정한 자아를 찾아가는 이야기다.",
    recruitingRoomCount = 8,
    readCount = 1250,
    isSaved = false
)

private val mockBookDetailSaved = mockBookDetail.copy(isSaved = true)

@Preview(showBackground = true)
@Composable
fun SearchBookDetailScreenContentPreview() {
    ThipTheme {
        SearchBookDetailScreenContent(
            bookDetail = mockBookDetail,
            feedList = emptyList()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBookDetailScreenContentSavedPreview() {
    ThipTheme {
        SearchBookDetailScreenContent(
            bookDetail = mockBookDetailSaved,
            feedList = emptyList()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBookDetailScreenContentWithFeedsPreview() {
    ThipTheme {
        SearchBookDetailScreenContent(
            bookDetail = mockBookDetail,
            feedList = listOf("피드 1", "피드 2", "피드 3")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBookDetailScreenContentErrorPreview() {
    ThipTheme {
        SearchBookDetailScreenContent(
            error = "책 정보를 불러오는데 실패했습니다."
        )
    }
}
