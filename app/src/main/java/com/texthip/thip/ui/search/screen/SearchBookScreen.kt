package com.texthip.thip.ui.search.screen

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.serialization.json.Json
import androidx.core.content.edit
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

@Composable
fun SearchBookScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    popularBooks: List<BookData> = emptyList(),
    viewModel: SearchBookViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val sharedPrefs = remember { 
        context.getSharedPreferences("book_search_prefs", Context.MODE_PRIVATE) 
    }

    var recentSearches by remember {
        mutableStateOf(
            try {
                val jsonString = sharedPrefs.getString("recent_book_searches", "[]") ?: "[]"
                Json.decodeFromString<List<String>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        )
    }

    fun saveRecentSearches(searches: List<String>) {
        try {
            val jsonString = Json.encodeToString(ListSerializer(String.serializer()), searches)
            sharedPrefs.edit {
                putString("recent_book_searches", jsonString)
            }
            recentSearches = searches
        } catch (e: Exception) {
            recentSearches = emptyList()
        }
    }
    
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

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
                        if (query.isNotBlank() && !recentSearches.contains(query)) {
                            val newSearches = listOf(query) + recentSearches.take(9) // 최대 10개 유지
                            saveRecentSearches(newSearches)
                        }
                        viewModel.onSearchButtonClick()
                        focusManager.clearFocus()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                when {
                    // 1. 검색어가 없는 상태 - 최근 검색어와 인기 책 표시
                    uiState.searchQuery.isBlank() -> {
                        SearchRecentBook(
                            recentSearches = recentSearches,
                            popularBooks = popularBooks,
                            popularBookDate = "01.12", // TODO: 서버로 날짜를 받아 오게 수정
                            onSearchClick = { keyword ->
                                viewModel.updateSearchQuery(keyword)
                                viewModel.onSearchButtonClick()
                            },
                            onRemove = { keyword ->
                                val updatedSearches = recentSearches.filterNot { it == keyword }
                                saveRecentSearches(updatedSearches)
                            },
                            onBookClick = { book ->
                                // 책 클릭 시 처리 (책 상세 화면으로 이동)
                            }
                        )
                    }

                    // 2. 검색 완료 상태 - 전체 검색 결과 표시 (무한 스크롤 포함)
                    uiState.hasSearchResults -> {
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
        SearchBookScreen(
            popularBooks = listOf(
                BookData(title = "단 한번의 삶", author = "리처드 도킨스", publisher = "을유문화사", imageUrl = null),
                BookData(title = "사랑", author = "마틴 셀리그만", publisher = "물푸레", imageUrl = null),
                BookData(title = "호모 사피엔스", author = "빅터 프랭클", publisher = "청림출판", imageUrl = null),
                BookData(title = "코스모스 실버", author = "칼 융", publisher = "문학과지성사", imageUrl = null),
                BookData(title = "오만과 편견", author = "에릭 프롬", publisher = "까치글방", imageUrl = null),
            )
        )
    }
}

@Preview
@Composable
fun PreviewBookSearchScreen_EmptyPopular() {
    ThipTheme {
        SearchBookScreen(
            popularBooks = emptyList()
        )
    }
}
