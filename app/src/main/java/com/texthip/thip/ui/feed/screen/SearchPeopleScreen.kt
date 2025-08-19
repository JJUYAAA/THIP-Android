package com.texthip.thip.ui.feed.screen

import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.texthip.thip.R
import com.texthip.thip.ui.common.forms.SearchBookTextField
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.feed.component.PeopleRecentSearch
import com.texthip.thip.ui.feed.component.SearchPeopleEmptyResult
import com.texthip.thip.ui.feed.component.SearchPeopleResult
import com.texthip.thip.ui.feed.mock.MySubscriptionData
import com.texthip.thip.ui.feed.viewmodel.SearchPeopleUiState
import com.texthip.thip.ui.feed.viewmodel.SearchPeopleViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun SearchPeopleScreen(
    viewModel: SearchPeopleViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onUserClick: (Long) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState.isSearched) {
        if (uiState.isSearched) {
            focusManager.clearFocus()
        }
    }

    SearchPeopleContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onSearchTextChanged = viewModel::onSearchTextChanged,
        onFinalSearch = viewModel::onFinalSearch,
        onRecentSearchClick = { keyword -> viewModel.onFinalSearch(keyword) },
        onRecentSearchRemove = viewModel::removeRecentSearch,
        onUserClick = { user -> onUserClick(user.userId!!.toLong())}
    )
}

@Composable
fun SearchPeopleContent(
    uiState: SearchPeopleUiState,
    onNavigateBack: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onFinalSearch: (String) -> Unit,
    onRecentSearchClick: (String) -> Unit,
    onRecentSearchRemove: (String) -> Unit,
    onUserClick: (MySubscriptionData) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DefaultTopAppBar(
            title = stringResource(R.string.search_user),
            onLeftClick = onNavigateBack,
        )
        Spacer(modifier = Modifier.height(16.dp))

        SearchBookTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            hint = stringResource(R.string.search_user_you_look_for),
            text = uiState.searchText,
            onValueChange = onSearchTextChanged,
            onSearch = onFinalSearch
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            uiState.isSearched && uiState.searchResults.isNotEmpty() -> { //검색했는데 결과 있음
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(
                            R.string.group_searched_room_size,
                            uiState.searchResults.size
                        ),
                        color = colors.Grey,
                        style = typography.menu_m500_s14_h24
                    )
                }
                Spacer(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 16.dp)
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colors.DarkGrey02)
                )
                SearchPeopleResult(peopleList = uiState.searchResults,onThipNumClick = onUserClick)
            }

            uiState.isSearched && uiState.searchResults.isEmpty() -> { //검색했는데 결과 없음
                SearchPeopleEmptyResult(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    mainText = stringResource(R.string.no_user_you_look_for)
                )
            }

            uiState.searchText.isNotBlank() && !uiState.isSearched  -> { //검색중
                SearchPeopleResult(peopleList = uiState.searchResults,onThipNumClick = onUserClick)
            }

            else -> { //최근검색어 보여주기
                PeopleRecentSearch(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    recentSearches = uiState.recentSearches,
                    onSearchClick = onRecentSearchClick,
                    onRemove = onRecentSearchRemove
                )
            }
        }


    }
}


@Preview
@Composable
private fun SearchPeopleContentPreview_Recent() {
    ThipTheme {
        SearchPeopleContent(
            uiState = SearchPeopleUiState(
                recentSearches = listOf("메롱", "메메롱", "메메메롱")
            ),
            onSearchTextChanged = {},
            onFinalSearch = {},
            onRecentSearchClick = {},
            onRecentSearchRemove = {},
            onUserClick = {},
            onNavigateBack = {}
        )
    }
}

@Preview
@Composable
private fun SearchPeopleContentPreview_Typing() {
    val dummyResults = listOf(
        MySubscriptionData(1L,null, "메롱이", "인플루언서", colors.NeonGreen, 12, false),
        MySubscriptionData(1L,null, "메메롱이", "칭호", colors.NeonGreen, 1, false),
    )
    ThipTheme {
        SearchPeopleContent(
            uiState = SearchPeopleUiState(
                searchText = "메롱",
                searchResults = dummyResults
            ),
            onSearchTextChanged = {},
            onFinalSearch = {},
            onRecentSearchClick = {},
            onRecentSearchRemove = {},
            onUserClick = {},
            onNavigateBack = {}
        )
    }
}

@Preview
@Composable
private fun SearchPeopleContentPreview_Result() {
    val dummyResults = listOf(
        MySubscriptionData(1L, null, "Thip_Official", "인플루언서", colors.NeonGreen, 111, false),
        MySubscriptionData(1L, null, "thip01", "작가", colors.NeonGreen, 0, false)
    )
    ThipTheme {
        SearchPeopleContent(
            uiState = SearchPeopleUiState(
                searchText = "thip",
                isSearched = true,
                searchResults = dummyResults
            ),
            onSearchTextChanged = {},
            onFinalSearch = {},
            onRecentSearchClick = {},
            onRecentSearchRemove = {},
            onUserClick = {},
            onNavigateBack = {}
        )
    }
}

@Preview
@Composable
private fun SearchPeopleContentPreview_Empty() {
    ThipTheme {
        SearchPeopleContent(
            uiState = SearchPeopleUiState(
                searchText = "없는사용자",
                isSearched = true,
                searchResults = emptyList()
            ),
            onSearchTextChanged = {},
            onFinalSearch = {},
            onRecentSearchClick = {},
            onRecentSearchRemove = {},
            onUserClick = {},
            onNavigateBack = {}
        )
    }
}