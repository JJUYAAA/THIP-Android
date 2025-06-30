package com.texthip.thip.ui.myPage.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.texthip.thip.R
import com.texthip.thip.ui.common.cards.CardBookList
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.myPage.component.SavedFeedCard
import com.texthip.thip.ui.myPage.viewmodel.SavedBookViewModel
import com.texthip.thip.ui.myPage.viewmodel.SavedFeedViewModel
import com.texthip.thip.ui.theme.Black
import com.texthip.thip.ui.theme.Grey02
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.ui.theme.White

@Composable
fun SavedScreen() {
    val tabs = listOf("피드", "책")
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        containerColor = Black,
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.saved),
                onLeftClick = {},
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Box(modifier = Modifier.width(160.dp).padding(start = 20.dp)) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = White,
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
                            onClick = { selectedTabIndex = index },
                            selectedContentColor = White,
                            unselectedContentColor = Grey02,
                            text = {
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = title,
                                        style = typography.smalltitle_sb600_s18_h24
                                    )
                                }
                            }
                        )
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                when (selectedTabIndex) {
                    0 -> FeedContent()
                    1 -> BookContent()
                }
            }
        }
    }
}

@Composable
fun FeedContent(viewModel: SavedFeedViewModel = viewModel()) {
    val feedList by viewModel.feeds.collectAsState()

    LazyColumn {
        items(feedList, key = { it.id }) { feed ->
            val bookImagePainter = feed.imageUrl?.let { painterResource(it) }
            val profileImagePainter = feed.user_profile_image?.let { painterResource(it) }

            SavedFeedCard(
                user_name = feed.user_name,
                user_role = feed.user_role,
                user_profile_image = profileImagePainter,
                book_title = feed.book_title,
                auth_name = feed.auth_name,
                time_ago = feed.time_ago,
                content = feed.content,
                like_count = feed.like_count,
                comment_count = feed.comment_count,
                is_like = feed.is_liked,
                is_saved = feed.is_saved,
                imageRes = bookImagePainter,
                onBookmarkClick = { viewModel.toggleBookmark(feed.id) },
                onLikeClick = { viewModel.toggleLike(feed.id) }
            )
        }
    }
}

@Composable
fun BookContent(viewModel: SavedBookViewModel = viewModel()) {
    val books = viewModel.bookList

    LazyColumn {
        items(items = books, key = { it.id }) { book ->
            CardBookList(
                title = book.title,
                author = book.author,
                imageRes = null,
                publisher = book.publisher,
                isBookmarked = book.isSaved,
                onBookmarkClick = { viewModel.toggleBookmark(book.id) }
            )
        }
    }
}

@Preview
@Composable
private fun SavedScreenPrev() {
    SavedScreen()
}