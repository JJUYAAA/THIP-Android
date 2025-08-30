package com.texthip.thip.ui.mypage.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.texthip.thip.R
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.mypage.component.BookContent
import com.texthip.thip.ui.mypage.component.FeedContent
import com.texthip.thip.ui.mypage.mock.BookItem
import com.texthip.thip.ui.mypage.mock.FeedItem
import com.texthip.thip.ui.mypage.viewmodel.SavedBookViewModel
import com.texthip.thip.ui.mypage.viewmodel.SavedFeedViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.ui.theme.White

@Composable
fun MypageSaveScreen(
    onNavigateBack: () -> Unit,
    onBookClick: (isbn: String) -> Unit = {},
    onFeedClick: (feedId: Long) -> Unit = {},
    feedViewModel: SavedFeedViewModel = hiltViewModel(),
    bookViewModel: SavedBookViewModel = hiltViewModel()
) {
    val tabs = listOf(stringResource(R.string.feed), stringResource(R.string.book))
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    val feedList by feedViewModel.feeds.collectAsState()
    val bookList by bookViewModel.books.collectAsState()

    LaunchedEffect(selectedTabIndex) {
        when (selectedTabIndex) {
            0 -> feedViewModel.loadSavedFeeds()
            1 -> bookViewModel.loadSavedBooks()
        }
    }

    MypageSaveContent(
        selectedTabIndex = selectedTabIndex,
        onTabSelected = { selectedTabIndex = it },
        feedList = feedList,
        bookList = bookList,
        onNavigateBack = onNavigateBack,
        onBookClick = onBookClick,
        onFeedClick = onFeedClick,
        feedViewModel = feedViewModel,
        bookViewModel = bookViewModel
    )
}

@Composable
private fun MypageSaveContent(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    feedList: List<FeedItem>,
    bookList: List<BookItem>,
    onNavigateBack: () -> Unit,
    onBookClick: (isbn: String) -> Unit,
    onFeedClick: (feedId: Long) -> Unit,
    feedViewModel: SavedFeedViewModel?,
    bookViewModel: SavedBookViewModel?
) {
    val tabs = listOf(stringResource(R.string.feed), stringResource(R.string.book))
    
    Column(
        Modifier
            .background(colors.Black)
            .fillMaxSize()
    ) {
        DefaultTopAppBar(
            title = stringResource(R.string.saved),
            onLeftClick = onNavigateBack,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .padding(start = 20.dp)
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = colors.White,
                    indicator = { tabPositions ->
                        val tabPosition = tabPositions[selectedTabIndex]

                        Box(
                            modifier = Modifier
                                .tabIndicatorOffset(tabPosition)
                                .width(48.dp)
                                .height(2.dp)
                                .align(Alignment.BottomCenter)
                                .clip(RoundedCornerShape(1.5.dp))
                                .background(White)
                        )
                    },
                    divider = {}) {

                    tabs.forEachIndexed { index, title ->
                        val selected = selectedTabIndex == index
                        Tab(
                            modifier = Modifier.width(60.dp),
                            selected = selected,
                            onClick = { onTabSelected(index) },
                            selectedContentColor = colors.White,
                            unselectedContentColor = colors.Grey02,
                            text = {
                                Text(
                                    textAlign = TextAlign.Center,
                                    text = title,
                                    style = typography.smalltitle_sb600_s18_h24
                                )
                            }
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (selectedTabIndex) {
                    0 -> feedViewModel?.let {
                        FeedContent(
                            feedList = feedList,
                            onFeedClick = onFeedClick,
                            viewModel = it
                        )
                    }

                    1 -> bookViewModel?.let {
                        BookContent(
                            bookList = bookList,
                            onBookClick = onBookClick,
                            viewModel = it
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun MypageSaveContentPreview() {
    ThipTheme {
        MypageSaveContent(
            selectedTabIndex = 0,
            onTabSelected = {},
            feedList = listOf(
                FeedItem(
                    id = 1L,
                    userProfileImage = "",
                    userName = "책벌레",
                    userRole = "소설 마니아",
                    bookTitle = "노르웨이의 숲",
                    authName = "무라카미 하루키",
                    timeAgo = "3시간 전",
                    content = "무라카미 하루키의 대표작 중 하나입니다. 청춘의 아픔과 사랑을 섬세하게 그려낸 작품이에요. 특히 와타나베의 내면 묘사가 인상깊었습니다.",
                    likeCount = 35,
                    commentCount = 12,
                    isLiked = true,
                    isSaved = true,
                    isLocked = false,
                    tags = listOf("일본문학", "청춘", "사랑"),
                    imageUrls = listOf("https://example.com/book1.jpg")
                ),
                FeedItem(
                    id = 2L,
                    userProfileImage = "",
                    userName = "역사애호가",
                    userRole = "한국사 전문가",
                    bookTitle = "총, 균, 쇠",
                    authName = "재레드 다이아몬드",
                    timeAgo = "1일 전",
                    content = "인류 문명의 발전을 지리학적 관점에서 분석한 놀라운 책입니다. 왜 어떤 대륙이 다른 대륙을 정복했는지에 대한 답을 찾을 수 있어요.",
                    likeCount = 67,
                    commentCount = 24,
                    isLiked = false,
                    isSaved = true,
                    isLocked = false,
                    tags = listOf("역사", "문명", "지리학"),
                    imageUrls = emptyList()
                )
            ),
            bookList = listOf(
                BookItem(
                    id = 1,
                    title = "1984",
                    author = "조지 오웰",
                    publisher = "민음사",
                    imageUrl = "",
                    isbn = "9788937460777",
                    isSaved = true
                ),
                BookItem(
                    id = 2,
                    title = "사피엔스",
                    author = "유발 하라리",
                    publisher = "김영사",
                    imageUrl = "",
                    isbn = "9788934972464",
                    isSaved = true
                ),
                BookItem(
                    id = 3,
                    title = "코스모스",
                    author = "칼 세이건",
                    publisher = "사이언스북스",
                    imageUrl = "",
                    isbn = "9788983711892",
                    isSaved = true
                )
            ),
            onNavigateBack = {},
            onBookClick = {},
            onFeedClick = {},
            feedViewModel = null,
            bookViewModel = null
        )
    }
}