package com.texthip.thip.ui.group.myroom.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.FilterButton
import com.texthip.thip.ui.common.forms.SearchBookTextField
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.myroom.component.GroupEmptyResult
import com.texthip.thip.ui.group.myroom.component.GroupFilteredSearchResult
import com.texthip.thip.ui.group.myroom.component.GroupLiveSearchResult
import com.texthip.thip.ui.group.myroom.component.GroupRecentSearch
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun GroupSearchScreen(
    modifier: Modifier = Modifier,
    roomList: List<GroupCardItemRoomData>
) {
    var recentSearches by rememberSaveable {
        mutableStateOf(listOf("user.02", "ㅇㅇ", "훽후ㅣㅣ", "검색4", "검색5", "검색6"))
    }
    var searchText by rememberSaveable { mutableStateOf("") }
    var isSearched by rememberSaveable { mutableStateOf(false) }
    var selectedGenreIndex by rememberSaveable { mutableIntStateOf(-1) }
    var selectedSortOptionIndex by rememberSaveable { mutableIntStateOf(0) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val genres = listOf(
        stringResource(R.string.literature),
        stringResource(R.string.science_it),
        stringResource(R.string.social_science),
        stringResource(R.string.humanities),
        stringResource(R.string.art)
    )
    val sortOptions = listOf(
        stringResource(R.string.group_filter_deadline),
        stringResource(R.string.group_filter_popular)
    )

    val liveFilteredRoomList by remember(searchText) {
        derivedStateOf {
            if (searchText.isBlank()) emptyList() else
                roomList.filter { room ->
                    room.title.contains(searchText, ignoreCase = true)
                }
        }
    }

    val filteredRoomList by remember(
        searchText,
        selectedGenreIndex,
        selectedSortOptionIndex,
        isSearched
    ) {
        derivedStateOf {
            if (!isSearched) emptyList()
            else {
                val filtered = roomList.filter { room ->
                    (searchText.isBlank() || room.title.contains(searchText, ignoreCase = true)) &&
                            (selectedGenreIndex == -1 || room.genreIndex == selectedGenreIndex)
                }
                when (selectedSortOptionIndex) {
                    0 -> filtered.sortedBy { it.endDate }             // 마감임박순
                    1 -> filtered.sortedByDescending { it.participants } // 인기순
                    else -> filtered
                }
            }
        }
    }

    LaunchedEffect(isSearched) {
        if (isSearched) {
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
                onLeftClick = {},
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
                    text = searchText,
                    onValueChange = {
                        searchText = it
                        isSearched = false
                    },
                    onSearch = { query ->
                        if (query.isNotBlank() && !recentSearches.contains(query)) {
                            recentSearches = listOf(query) + recentSearches
                        }
                        isSearched = true
                        selectedGenreIndex = -1
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                when {
                    searchText.isBlank() && !isSearched && recentSearches.isEmpty() -> {
                        GroupRecentSearch(
                            recentSearches = emptyList(),
                            onSearchClick = {},
                            onRemove = {}
                        )
                    }

                    searchText.isBlank() && !isSearched && recentSearches.isNotEmpty() -> {
                        GroupRecentSearch(
                            recentSearches = recentSearches,
                            onSearchClick = { keyword ->
                                searchText = keyword
                                isSearched = true
                            },
                            onRemove = { keyword ->
                                recentSearches = recentSearches.filterNot { it == keyword }
                            }
                        )
                    }

                    searchText.isNotBlank() && !isSearched -> {
                        if (liveFilteredRoomList.isEmpty()) {
                            GroupEmptyResult(
                                mainText = stringResource(R.string.group_no_search_result1),
                                subText = stringResource(R.string.group_no_search_result2)
                            )
                        } else {
                            GroupLiveSearchResult(
                                roomList = liveFilteredRoomList
                            )
                        }
                    }

                    isSearched -> {
                        GroupFilteredSearchResult(
                            genres = genres,
                            selectedGenreIndex = selectedGenreIndex,
                            onGenreSelect = { selectedGenreIndex = it },
                            resultCount = filteredRoomList.size,
                            roomList = filteredRoomList,
                        )
                    }
                }
            }
        }

        if (isSearched) {
            FilterButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 176.dp, end = 20.dp),
                selectedOption = sortOptions[selectedSortOptionIndex],
                options = sortOptions,
                onOptionSelected = { selected ->
                    selectedSortOptionIndex = sortOptions.indexOf(selected)
                }
            )
        }
    }
}


@Preview
@Composable
fun PreviewGroupSearchScreen() {
    ThipTheme {
        GroupSearchScreen(
            roomList = listOf(
                GroupCardItemRoomData("aaa", 22, 30, true, 3, R.drawable.bookcover_sample, 0),
                GroupCardItemRoomData("abc", 15, 20, true, 7, R.drawable.bookcover_sample, 1, true),
                GroupCardItemRoomData("abcd", 10, 15, true, 5, R.drawable.bookcover_sample, 2, true),
                GroupCardItemRoomData("abcde", 8, 12, false, 2, R.drawable.bookcover_sample, 3, true),
                GroupCardItemRoomData("abcdef", 18, 25, true, 4, R.drawable.bookcover_sample, 4),
                GroupCardItemRoomData("abcdefg", 12, 20, true, 1, R.drawable.bookcover_sample, 0),
                GroupCardItemRoomData("abcdefgh", 10, 14, true, 6, R.drawable.bookcover_sample, 1)
            )
        )
    }
}
