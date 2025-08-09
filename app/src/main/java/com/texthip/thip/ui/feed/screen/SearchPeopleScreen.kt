package com.texthip.thip.ui.feed.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import com.texthip.thip.ui.common.forms.SearchBookTextField
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.feed.component.PeopleRecentSearch
import com.texthip.thip.ui.feed.component.SearchPeopleEmptyResult
import com.texthip.thip.ui.feed.component.SearchPeopleResult
import com.texthip.thip.ui.feed.mock.MySubscriptionData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun SearchPeopleScreen(
    modifier: Modifier = Modifier,
    allPeople: List<MySubscriptionData>
) {
    var recentSearches by rememberSaveable {
        mutableStateOf(listOf("메롱", "메메롱", "메메메롱", "메메메", "메메루메루메루"))
    }
    var searchText by rememberSaveable { mutableStateOf("") }
    var isSearched by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val liveSearchResults by remember(searchText, allPeople) {
        derivedStateOf {
            if (searchText.isBlank()) emptyList()
            else allPeople.filter { person ->
                person.nickname.contains(searchText, ignoreCase = true)
            }
        }
    }

    val finalSearchResults by remember(searchText, isSearched, allPeople) {
        derivedStateOf {
            if (isSearched) {
                if (searchText.isNotBlank()) {
                    allPeople.filter { person ->
                        person.nickname.contains(searchText, ignoreCase = true)
                    }
                } else {
                    emptyList()
                }
            } else {
                emptyList()
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
                title = stringResource(R.string.search_user),
                onLeftClick = {},
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                SearchBookTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .padding(horizontal = 20.dp),
                    hint = stringResource(R.string.search_user_you_look_for),
                    text = searchText,
                    onValueChange = {
                        searchText = it
                        isSearched = false
                    },
                    onSearch = { query ->
                        if (query.isNotBlank() && !recentSearches.contains(query)) {
                            recentSearches = (listOf(query) + recentSearches).take(10)
                        }
                        isSearched = true
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                when {
                    isSearched && finalSearchResults.isNotEmpty() -> { //검색했는데 결과 있음
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.group_searched_room_size, finalSearchResults.size),
                                color = colors.Grey,
                                style = typography.menu_m500_s14_h24
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .padding(top = 4.dp, bottom = 16.dp)
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(colors.DarkGrey02)
                                .padding(horizontal = 20.dp)
                        )
                        SearchPeopleResult(
                            modifier = Modifier.weight(1f),
                            peopleList = finalSearchResults,
                            onThipNumClick = { person -> /*프로필로 이동*/ }
                        )
                    }
                    isSearched && finalSearchResults.isEmpty() -> { //검색했는데 결과 없음
                        SearchPeopleEmptyResult(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            mainText = stringResource(R.string.no_user_you_look_for)
                        )
                    }
                    searchText.isNotBlank() && !isSearched -> { //검색중
                        SearchPeopleResult(
                            modifier = Modifier.weight(1f),
                            peopleList = liveSearchResults,
                            onThipNumClick = { person -> /* 프로필 화면으로 이동 */ }
                        )
                    }
                    searchText.isBlank() && !isSearched -> { //최근검색어 보여주기
                        PeopleRecentSearch(
                            modifier = Modifier.padding(horizontal = 20.dp),
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
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewGroupSearchScreen() {
    ThipTheme {
        SearchPeopleScreen(
            allPeople = listOf(
                MySubscriptionData(null, "메롱이", "인플루언서", colors.NeonGreen, 12, false),
                MySubscriptionData(null, "메메롱이", "칭호", colors.NeonGreen, 1, false),
                MySubscriptionData(null, "thip", "칭호칭호", colors.NeonGreen, 11, false),
                MySubscriptionData(null, "Thip", "인플루언서", colors.NeonGreen, 111, false),
                MySubscriptionData(null, "thip01", "작가", colors.NeonGreen, 0, false)
            )
        )
    }
}