package com.texthip.thip.ui.search.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.texthip.thip.R
import com.texthip.thip.ui.common.forms.SearchBookTextField
import com.texthip.thip.ui.common.topappbar.LeftNameTopAppBar
import com.texthip.thip.ui.search.component.SearchActiveField
import com.texthip.thip.ui.search.component.SearchBookFilteredResult
import com.texthip.thip.ui.search.component.SearchEmptyResult
import com.texthip.thip.ui.search.component.SearchRecentBook
import com.texthip.thip.ui.search.mock.BookData
import com.texthip.thip.ui.search.viewmodel.SearchBookViewModel
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun SearchBookScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    viewModel: SearchBookViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // 화면 진입 시 무조건 새로고침
    LaunchedEffect(Unit) {
        viewModel.refreshData()
    }

    // 화면 생명주기를 감지하여 새로고침 (뒤로가기 포함)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshData()
            }
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LeftNameTopAppBar(
                title = stringResource(R.string.book_search_topappbar)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                SearchBookTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    hint = stringResource(R.string.book_search_hint),
                    text = uiState.searchQuery,
                    onValueChange = { query ->
                        viewModel.updateSearchQuery(query)
                    },
                    onSearch = { query ->
                        viewModel.onSearchButtonClick()
                        focusManager.clearFocus()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                when {
                    // 1. 검색어가 없는 상태 - 최근 검색어와 인기 책 표시
                    uiState.searchQuery.isBlank() -> {
                        SearchRecentBook(
                            recentSearches = uiState.recentSearches.map { it.searchTerm },
                            popularBooks = uiState.popularBooks.map { item ->
                                BookData(
                                    title = item.title,
                                    author = "",
                                    publisher = "",
                                    imageUrl = item.imageUrl
                                )
                            },
                            popularBookDate = "01.12", // TODO: 서버로 날짜를 받아 오게 수정
                            onSearchClick = { keyword ->
                                viewModel.updateSearchQuery(keyword)
                                viewModel.onSearchButtonClick()
                            },
                            onRemove = { keyword ->
                                // 서버에서 해당 검색어를 찾아서 삭제
                                val recentSearchItem = uiState.recentSearches.find { it.searchTerm == keyword }
                                recentSearchItem?.let {
                                    viewModel.deleteRecentSearch(it.recentSearchId)
                                }
                            },
                            onBookClick = { book ->
                                // 책 클릭 시 처리 (책 상세 화면으로 이동)
                            }
                        )
                    }

                    // 2. 검색 완료 상태 - 전체 검색 결과 표시 (무한 스크롤 포함)
                    uiState.isSearchCompleted -> {
                        SearchBookFilteredResult(
                            resultCount = uiState.totalElements,
                            bookList = uiState.searchResults.map { item ->
                                BookData(
                                    title = item.title,
                                    author = item.authorName,
                                    publisher = item.publisher,
                                    imageUrl = item.imageUrl
                                )
                            },
                            isLoading = uiState.isSearching || uiState.isLoadingMore,
                            hasMore = uiState.canLoadMore,
                            onLoadMore = {
                                viewModel.loadMoreBooks()
                            }
                        )
                    }

                    // 3. Live search 결과가 있는 상태 - SearchActiveField 표시 (무한 스크롤 포함)
                    uiState.hasLiveResults -> {
                        SearchActiveField(
                            bookList = uiState.liveSearchResults.map { item ->
                                BookData(
                                    title = item.title,
                                    author = item.authorName,
                                    publisher = item.publisher,
                                    imageUrl = item.imageUrl
                                )
                            },
                            isLoading = uiState.isLiveSearching || uiState.isLiveLoadingMore,
                            hasMore = uiState.canLiveLoadMore,
                            onLoadMore = {
                                viewModel.loadMoreLiveSearchResults()
                            }
                        )
                    }

                    // 4. 검색어는 있지만 결과가 없는 상태 - Empty 표시
                    uiState.showEmptyState -> {
                        SearchEmptyResult(
                            mainText = stringResource(R.string.book_no_search_result1),
                            subText = stringResource(R.string.book_no_search_result2),
                            onRequestBook = { /*책 요청 처리*/ }
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewBookSearchScreen_Default() {
    ThipTheme {
        SearchBookScreen()
    }
}

@Preview
@Composable
fun PreviewBookSearchScreen_EmptyPopular() {
    ThipTheme {
        SearchBookScreen()
    }
}
