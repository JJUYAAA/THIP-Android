package com.texthip.thip.ui.search.screen

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.texthip.thip.R
import com.texthip.thip.data.model.book.response.BookDetailResponse
import com.texthip.thip.ui.common.buttons.ActionMediumButton
import com.texthip.thip.ui.common.modal.InfoPopup
import com.texthip.thip.ui.common.topappbar.GradationTopAppBar
import com.texthip.thip.ui.mypage.component.SavedFeedCard
import com.texthip.thip.ui.mypage.mock.FeedItem
import com.texthip.thip.ui.search.component.SearchFilterButton
import com.texthip.thip.ui.search.component.SearchFilterDropdownOverlay
import com.texthip.thip.ui.search.viewmodel.BookDetailUiState
import com.texthip.thip.ui.search.viewmodel.BookDetailViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.utils.color.hexToColor


@Composable
fun SearchBookDetailScreen(
    modifier: Modifier = Modifier,
    isbn: String,
    onLeftClick: () -> Unit = {},
    onRecruitingGroupClick: () -> Unit = {},
    onWriteFeedClick: (BookDetailResponse) -> Unit = {},
    onFeedClick: (Long) -> Unit = {},
    onBookmarkClick: (String, Boolean) -> Unit = { _, _ -> },
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // ISBN으로 책 상세 정보 로드
    LaunchedEffect(isbn) {
        viewModel.loadBookDetail(isbn)
    }

    SearchBookDetailScreenContent(
        modifier = modifier,
        isLoading = uiState.isLoading,
        error = uiState.error,
        bookDetail = uiState.bookDetail,
        uiState = uiState,
        onLeftClick = onLeftClick,
        onRecruitingGroupClick = onRecruitingGroupClick,
        onWriteFeedClick = onWriteFeedClick,
        onFeedClick = onFeedClick,
        onBookmarkClick = { isbnParam, newState ->
            viewModel.saveBook(isbnParam, newState)
            onBookmarkClick(isbnParam, newState)
        },
        onSortChange = { sortType ->
            val apiSortType = if (sortType == "인기순") "like" else "latest"
            viewModel.changeSortOrder(isbn, apiSortType)
        },
        onLoadMore = {
            viewModel.loadMoreFeeds(isbn)
        }
    )
}

@Composable
private fun SearchBookDetailScreenContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null,
    bookDetail: BookDetailResponse? = null,
    uiState: BookDetailUiState? = null,
    onLeftClick: () -> Unit = {},
    onRecruitingGroupClick: () -> Unit = {},
    onWriteFeedClick: (BookDetailResponse) -> Unit = {},
    onFeedClick: (Long) -> Unit = {},
    onBookmarkClick: (String, Boolean) -> Unit = { _, _ -> },
    onSortChange: (String) -> Unit = {},
    onLoadMore: () -> Unit = {}
) {
    var isIntroductionPopupVisible by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(bookDetail?.isSaved ?: false) }
    var isFilterDropdownVisible by remember { mutableStateOf(false) }
    var filterButtonPosition by remember { mutableStateOf(IntOffset.Zero) }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    
    // 화면 크기에 따른 최대 높이 설정 (태블릿 대응)
    val maxImageHeight = remember(configuration.screenHeightDp) {
        (configuration.screenHeightDp * 0.6f).dp.coerceAtMost(620.dp)
    }
    val filterOptions = listOf(
        stringResource(R.string.sort_like),
        stringResource(R.string.sort_latest)
    )

    // UI 상태를 ViewModel의 currentSort와 동기화
    val currentSortFromViewModel = uiState?.currentSort ?: "like"
    val selectedFilterOption = if (currentSortFromViewModel == "like") 0 else 1

    val lazyListState = rememberLazyListState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex > (totalItemsNumber - 3)
        }
    }

    // 무한 스크롤 로직
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && uiState?.isLoadingMore == false && !uiState.isLast) {
            onLoadMore()
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.Black)
                        .then(
                            if (isIntroductionPopupVisible) {
                                Modifier.blur(4.dp)
                            } else {
                                Modifier
                            }
                        )
                ) {
                    // 전체 스크롤 가능한 컨텐츠
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // 상단 책 이미지 배경 영역
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                AsyncImage(
                                    model = bookDetail.imageUrl,
                                    contentDescription = bookDetail.title,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 420.dp, max = maxImageHeight)
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
                                                    colors.Black.copy(alpha = 0.2f),
                                                    colors.Black.copy(alpha = 0.5f),
                                                    colors.Black.copy(alpha = 0.8f),
                                                    colors.Black.copy(alpha = 0.95f),
                                                    colors.Black
                                                ),
                                                startY = 0f,
                                                endY = Float.POSITIVE_INFINITY
                                            )
                                        )
                                )

                                // 책 정보 컨텐츠를 이미지 위에 배치
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp)
                                ) {
                                    Spacer(modifier = Modifier.height(96.dp)) // TopAppBar 공간

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
                                                onClick = { onWriteFeedClick(bookDetail) }
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
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )

                                    SearchFilterButton(
                                        modifier = Modifier
                                            .onGloballyPositioned { coordinates ->
                                                filterButtonPosition = IntOffset(
                                                    coordinates.positionInRoot().x.toInt(),
                                                    coordinates.positionInRoot().y.toInt()
                                                )
                                            },
                                        selectedOption = filterOptions[selectedFilterOption],
                                        isExpanded = isFilterDropdownVisible,
                                        onClick = {
                                            isFilterDropdownVisible = !isFilterDropdownVisible
                                        }
                                    )
                                    // 구분선
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(colors.DarkGrey02)
                                    )
                                }
                            }
                        }

                        // 피드 섹션 전환을 위한 추가 그라데이션
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                colors.Black,
                                                colors.Black.copy(alpha = 0.95f),
                                                colors.Black.copy(alpha = 0.9f)
                                            )
                                        )
                                    )
                            )
                        }

                        // 피드 목록 (ViewModel에서 변환된 데이터 사용)
                        val feedItems = uiState?.feedItems ?: emptyList()

                        if (feedItems.isEmpty() && uiState?.isLoadingFeeds != true) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp),
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
                            }
                        }

                        if (uiState?.isLoadingFeeds == true && feedItems.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = colors.White)
                                }
                            }
                        }

                        // 피드 아이템들
                        itemsIndexed(
                            items = feedItems,
                            key = { _, feedItem -> feedItem.id }
                        ) { index, feedItem ->
                            val relatedFeedItem = uiState?.relatedFeeds?.getOrNull(index)

                            Spacer(modifier = Modifier.height(if (index == 0) 0.dp else 40.dp))

                            SavedFeedCard(
                                feedItem = feedItem,
                                bottomTextColor = relatedFeedItem?.aliasColor?.let { hexToColor(it) } ?: colors.NeonGreen,
                                onContentClick = { onFeedClick(feedItem.id) },
                                onBookClick = { /* TODO: Book navigation */ }
                            )

                            Spacer(modifier = Modifier.height(40.dp))

                            if (index != feedItems.lastIndex) {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .background(colors.DarkGrey02)
                                )
                            }
                        }

                        // 무한 스크롤 로딩 인디케이터
                        if (uiState?.isLoadingMore == true) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = colors.White
                                    )
                                }
                            }
                        }
                    }
                }

                // TopAppBar 오버레이 (고정)
                GradationTopAppBar(
                    count = bookDetail.readCount,
                    autoHideCount = true,
                    countDisplayDurationMs = 5000L,
                    onLeftClick = onLeftClick,
                    onRightClick = {}
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

                // FilterDropdown 오버레이
                SearchFilterDropdownOverlay(
                    isVisible = isFilterDropdownVisible,
                    options = filterOptions,
                    selectedOption = filterOptions[selectedFilterOption],
                    filterButtonPosition = filterButtonPosition,
                    density = density,
                    onOptionSelected = { option ->
                        onSortChange(option)
                    },
                    onDismiss = { isFilterDropdownVisible = false }
                )
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
            bookDetail = mockBookDetail
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBookDetailScreenContentSavedPreview() {
    ThipTheme {
        SearchBookDetailScreenContent(
            bookDetail = mockBookDetailSaved
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBookDetailScreenContentWithFeedsPreview() {
    val mockFeedItems = listOf(
        FeedItem(
            id = 1L,
            userProfileImage = "https://example.com/profile1.jpg",
            userName = "책읽는사람",
            userRole = "문학 애호가",
            bookTitle = "데미안",
            authName = "헤르만 헤세",
            timeAgo = "2시간 전",
            content = "이 책을 읽으면서 진정한 자아를 찾아가는 과정에 대해 많은 생각을 하게 되었습니다. 싱클레어의 성장 과정이 현재의 나와 많이 닮아있다고 느꼈어요.",
            likeCount = 24,
            commentCount = 8,
            isLiked = true,
            isSaved = false,
            imageUrls = listOf("https://example.com/image1.jpg")
        ),
        FeedItem(
            id = 2L,
            userProfileImage = "https://example.com/profile2.jpg",
            userName = "철학독서가",
            userRole = "인문학 탐구자",
            bookTitle = "데미안",
            authName = "헤르만 헤세",
            timeAgo = "5시간 전",
            content = "헤세의 작품 중에서도 가장 깊이 있는 성찰을 담고 있는 작품이라고 생각합니다. 선악을 넘어선 인간 내면의 복잡성을 이해하는 데 큰 도움이 되었습니다.",
            likeCount = 18,
            commentCount = 12,
            isLiked = false,
            isSaved = true,
            imageUrls = emptyList()
        ),
        FeedItem(
            id = 3L,
            userProfileImage = "https://example.com/profile3.jpg",
            userName = "문학소녀",
            userRole = "소설 리뷰어",
            bookTitle = "데미안",
            authName = "헤르만 헤세",
            timeAgo = "1일 전",
            content = "청소년기에 읽었을 때와 성인이 되어 다시 읽었을 때의 감상이 완전히 달랐습니다. 나이가 들수록 더 깊이 이해되는 작품이네요.\n\n특히 데미안이라는 인물이 주는 메시지가 인상 깊었어요.",
            likeCount = 31,
            commentCount = 15,
            isLiked = true,
            isSaved = true,
            imageUrls = listOf(
                "https://example.com/image2.jpg",
                "https://example.com/image3.jpg"
            )
        )
    )

    val mockUiState = BookDetailUiState(
        bookDetail = mockBookDetail,
        relatedFeeds = emptyList(), // feedItems로 변환되므로 빈 리스트
        isLoadingFeeds = false,
        isLoadingMore = false,
        currentSort = "like"
    ).copy(
        // feedItems를 직접 설정하기 위해 relatedFeeds를 임시로 설정
        relatedFeeds = mockFeedItems.map { feedItem ->
            com.texthip.thip.data.model.feed.response.RelatedFeedItem(
                feedId = feedItem.id.toInt(),
                creatorId = feedItem.id.toInt(),
                creatorNickname = feedItem.userName,
                creatorProfileImageUrl = feedItem.userProfileImage,
                aliasName = feedItem.userRole,
                aliasColor = "#FF6B9D",
                postDate = feedItem.timeAgo,
                isbn = mockBookDetail.isbn,
                bookTitle = feedItem.bookTitle,
                bookAuthor = feedItem.authName,
                contentBody = feedItem.content,
                contentUrls = feedItem.imageUrls,
                likeCount = feedItem.likeCount,
                commentCount = feedItem.commentCount,
                isSaved = feedItem.isSaved,
                isLiked = feedItem.isLiked,
                isWriter = false
            )
        }
    )

    ThipTheme {
        SearchBookDetailScreenContent(
            bookDetail = mockBookDetail,
            uiState = mockUiState
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