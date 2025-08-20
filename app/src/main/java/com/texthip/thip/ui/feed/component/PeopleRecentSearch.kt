package com.texthip.thip.ui.feed.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.GenreChipButton
import com.texthip.thip.ui.feed.viewmodel.RecentSearchUiItem
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun PeopleRecentSearch(
    modifier: Modifier = Modifier,
    recentSearches: List<RecentSearchUiItem>,
    onSearchClick: (String) -> Unit,
    onRemove: (Long) -> Unit
) {
    Column (modifier = modifier){
        Text(
            text = stringResource(R.string.group_recent_search),
            color = colors.White,
            style = typography.smalltitle_sb600_s18_h24
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (recentSearches.isEmpty()) {
            Text(
                text = stringResource(R.string.group_no_recent_search),
                color = colors.Grey01,
                style = typography.menu_r400_s14_h24
            )
        } else {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                maxLines = 2,
            ) {
                recentSearches.take(5).forEach { item ->
                    GenreChipButton(
                        text = item.term,
                        onClick = { onSearchClick(item.term) },
                        onCloseClick = { onRemove(item.id) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PeopleRecentSearchPrev() {
    ThipTheme {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            var searches by remember {
                mutableStateOf(
                    listOf(
                        RecentSearchUiItem(id = 1, term = "thip"),
                        RecentSearchUiItem(id = 2, term = "걔누구더라"),
                        RecentSearchUiItem(id = 3, term = "으아아아"),
                        RecentSearchUiItem(id = 4, term = "ㅇㅇ"),
                        RecentSearchUiItem(id = 5, term = "user.02")
                    )
                )
            }

            PeopleRecentSearch(
                recentSearches = searches,
                onSearchClick = { searchTerm ->
                },
                onRemove = { searchId ->
                    searches = searches.filterNot { it.id == searchId }
                }
            )
        }
    }
}

@Preview
@Composable
fun PeopleRecentSearchEmptyPrev() {
    ThipTheme {
        Box(
            modifier = Modifier
                .background(colors.Black)
                .padding(16.dp)
        ) {
            PeopleRecentSearch(
                recentSearches = emptyList(),
                onSearchClick = { },
                onRemove = { }
            )
        }
    }
}
