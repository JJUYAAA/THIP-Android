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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.texthip.thip.R
import com.texthip.thip.ui.common.forms.SearchBookTextField
import com.texthip.thip.ui.common.topappbar.LeftNameTopAppBar
import com.texthip.thip.ui.search.component.SearchActiveField
import com.texthip.thip.ui.search.component.SearchBookFilteredResult
import com.texthip.thip.ui.search.component.SearchEmptyResult
import com.texthip.thip.ui.search.component.SearchRecentBook
import com.texthip.thip.ui.search.mock.BookData
import com.texthip.thip.ui.search.viewmodel.SearchBookViewModel
import com.texthip.thip.ui.search.viewmodel.SearchMode
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun SearchBookScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchBookViewModel = hiltViewModel(),
    onBookClick: (String) -> Unit = {},
    onRequestBook: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val lifecycleOwner = LocalLifecycleOwner.current

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

                when (uiState.searchMode) {
                    SearchMode.Initial -> {
                        SearchRecentBook(
                            recentSearches = uiState.recentSearches.map { it.searchTerm },
                            popularBooks = uiState.popularBooks.map { item ->
                                BookData(
                                    title = item.title,
                                    author = "",
                                    publisher = "",
                                    imageUrl = item.imageUrl,
                                    isbn = item.isbn
                                )
                            },
                            popularBookDate = SimpleDateFormat("MM.dd", Locale.getDefault()).format(Date()),
                            onSearchClick = { keyword ->
                                viewModel.updateSearchQuery(keyword)
                                viewModel.onSearchButtonClick()
                            },
                            onRemove = { keyword ->
                                val recentSearchItem = uiState.recentSearches.find { it.searchTerm == keyword }
                                recentSearchItem?.let {
                                    viewModel.deleteRecentSearch(it.recentSearchId)
                                }
                            },
                            onBookClick = { book ->
                                onBookClick(book.isbn)
                            }
                        )
                    }

                    SearchMode.LiveSearch -> {
                        if (uiState.hasResults) {
                            SearchActiveField(
                                bookList = uiState.searchResults.map { item ->
                                    BookData(
                                        title = item.title,
                                        author = item.authorName,
                                        publisher = item.publisher,
                                        imageUrl = item.imageUrl,
                                        isbn = item.isbn
                                    )
                                },
                                isLoading = uiState.isSearching || uiState.isLoadingMore,
                                hasMore = uiState.canLoadMore,
                                onLoadMore = {
                                    viewModel.loadMoreBooks()
                                },
                                onBookClick = { book ->
                                    onBookClick(book.isbn)
                                }
                            )
                        } else if (uiState.showEmptyState) {
                            SearchEmptyResult(
                                mainText = stringResource(R.string.book_no_search_result1),
                                subText = stringResource(R.string.book_no_search_result2),
                                onRequestBook = onRequestBook
                            )
                        }
                    }

                    SearchMode.CompleteSearch -> {
                        if (uiState.hasResults) {
                            SearchBookFilteredResult(
                                resultCount = uiState.totalElements,
                                bookList = uiState.searchResults.map { item ->
                                    BookData(
                                        title = item.title,
                                        author = item.authorName,
                                        publisher = item.publisher,
                                        imageUrl = item.imageUrl,
                                        isbn = item.isbn
                                    )
                                },
                                isLoading = uiState.isSearching || uiState.isLoadingMore,
                                hasMore = uiState.canLoadMore,
                                onLoadMore = {
                                    viewModel.loadMoreBooks()
                                },
                                onBookClick = { book ->
                                    onBookClick(book.isbn)
                                }
                            )
                        } else if (uiState.showEmptyState) {
                            SearchEmptyResult(
                                mainText = stringResource(R.string.book_no_search_result1),
                                subText = stringResource(R.string.book_no_search_result2),
                                onRequestBook = onRequestBook
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewBookSearchScreen() {
    ThipTheme {
        SearchBookScreen()
    }
}
