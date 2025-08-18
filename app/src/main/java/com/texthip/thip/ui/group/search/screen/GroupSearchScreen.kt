package com.texthip.thip.ui.group.search.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.FilterButton
import com.texthip.thip.ui.common.forms.SearchBookTextField
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.search.component.GroupRecentSearch
import com.texthip.thip.ui.group.search.component.GroupEmptyResult
import com.texthip.thip.ui.group.search.component.GroupFilteredSearchResult
import com.texthip.thip.ui.group.search.component.GroupLiveSearchResult
import com.texthip.thip.ui.group.search.viewmodel.GroupSearchViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.utils.rooms.toDisplayStrings

@Composable
fun GroupSearchScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onRoomClick: (Int) -> Unit = {},
    viewModel: GroupSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val genreDisplayNames = uiState.genres.toDisplayStrings()

    val sortOptions = listOf(
        stringResource(R.string.group_filter_deadline),
        stringResource(R.string.group_filter_popular)
    )

    val selectedGenreIndex = if (uiState.selectedGenre != null) {
        uiState.genres.indexOf(uiState.selectedGenre)
    } else -1

    val selectedSortOptionIndex = when (uiState.selectedSort) {
        "deadline" -> 0
        "memberCount" -> 1
        else -> 0
    }


    LaunchedEffect(uiState.isCompleteSearching) {
        if (uiState.isCompleteSearching) {
            focusManager.clearFocus()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            DefaultTopAppBar(
                title = stringResource(R.string.group_room_search_topappbar),
                onLeftClick = onNavigateBack,
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
                    hint = stringResource(R.string.group_room_search_hint),
                    text = uiState.searchQuery,
                    onValueChange = { query ->
                        viewModel.updateSearchQuery(query)
                    },
                    onSearch = {
                        viewModel.onSearchButtonClick()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                when {
                    uiState.isInitial -> {
                        if (uiState.recentSearches.isEmpty()) {
                            GroupRecentSearch(
                                recentSearches = emptyList(),
                                onSearchClick = {},
                                onRemove = {}
                            )
                        } else {
                            GroupRecentSearch(
                                recentSearches = uiState.recentSearches.map { it.searchTerm },
                                onSearchClick = { keyword ->
                                    viewModel.updateSearchQuery(keyword)
                                    viewModel.onSearchButtonClick()
                                },
                                onRemove = { keyword ->
                                    viewModel.deleteRecentSearchByKeyword(keyword)
                                }
                            )
                        }
                    }

                    uiState.isLiveSearching -> {
                        if (uiState.showEmptyState) {
                            GroupEmptyResult(
                                mainText = stringResource(R.string.group_no_search_result1),
                                subText = stringResource(R.string.group_no_search_result2)
                            )
                        } else if (uiState.hasResults) {
                            GroupLiveSearchResult(
                                roomList = uiState.searchResults,
                                onRoomClick = { room -> onRoomClick(room.roomId) },
                                canLoadMore = uiState.canLoadMore,
                                isLoadingMore = uiState.isLoadingMore,
                                onLoadMore = { viewModel.loadMoreRooms() }
                            )
                        }
                    }

                    uiState.isCompleteSearching -> {
                        GroupFilteredSearchResult(
                            genres = genreDisplayNames,
                            selectedGenreIndex = selectedGenreIndex,
                            onGenreSelect = { index ->
                                val currentSelectedIndex = if (uiState.selectedGenre != null) {
                                    uiState.genres.indexOf(uiState.selectedGenre)
                                } else -1

                                val selectedGenre = if (index == currentSelectedIndex) {
                                    // 같은 장르를 다시 터치하면 선택 해제
                                    null
                                } else if (index >= 0 && index < uiState.genres.size) {
                                    // 새로운 장르 선택
                                    uiState.genres[index]
                                } else {
                                    null
                                }
                                viewModel.updateSelectedGenre(selectedGenre)
                            },
                            resultCount = uiState.searchResults.size,
                            roomList = uiState.searchResults,
                            onRoomClick = { room -> onRoomClick(room.roomId) },
                            canLoadMore = uiState.canLoadMore,
                            isLoadingMore = uiState.isLoadingMore,
                            onLoadMore = { viewModel.loadMoreRooms() }
                        )
                    }
                }
            }
        }

        if (uiState.isCompleteSearching) {
            FilterButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 196.dp, end = 20.dp),
                selectedOption = sortOptions[selectedSortOptionIndex],
                options = sortOptions,
                onOptionSelected = { selected ->
                    val sortType = when (sortOptions.indexOf(selected)) {
                        0 -> "deadline"
                        1 -> "memberCount"
                        else -> "deadline"
                    }
                    viewModel.updateSortType(sortType)
                }
            )
        }
    }
}


@Preview
@Composable
fun PreviewGroupSearchScreen() {
    ThipTheme {
        GroupSearchScreen()
    }
}
