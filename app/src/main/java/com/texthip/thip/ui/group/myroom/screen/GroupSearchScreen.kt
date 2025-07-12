package com.texthip.thip.ui.group.myroom.screen

import android.R.attr.top
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.FilterButton
import com.texthip.thip.ui.common.buttons.GenreChipButton
import com.texthip.thip.ui.common.buttons.GenreChipRow
import com.texthip.thip.ui.common.cards.CardItemRoom
import com.texthip.thip.ui.common.cards.CardItemRoomSmall
import com.texthip.thip.ui.common.forms.SearchBookTextField
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun GroupSearchScreen(
    modifier: Modifier = Modifier
) {
    var recentSearches by rememberSaveable {
        mutableStateOf(listOf("user.02", "ㅇㅇ", "훽후ㅣㅣ", "검색4", "검색5", "검색6"))
    }
    var searchText by rememberSaveable { mutableStateOf("") }
    var isSearched by rememberSaveable { mutableStateOf(false) }
    var selectedGenreIndex by rememberSaveable { mutableIntStateOf(-1) }
    var selectedSortOption by rememberSaveable { mutableStateOf("마감임박순") }

    // 임시 데이터 (샘플)
    val genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술")
    val sortOptions = listOf("마감임박순", "최신순", "참여많은순")

    // 임시 샘플 데이터
    val roomList = listOf(
        GroupCardItemRoomData("aaa", 22, 30, true, 3, R.drawable.bookcover_sample, 0),
        GroupCardItemRoomData("bbb", 15, 20, true, 7, R.drawable.bookcover_sample, 1),
        GroupCardItemRoomData("ccc", 10, 15, true, 5, R.drawable.bookcover_sample, 2),
        GroupCardItemRoomData("ddd", 8, 12, false, 2, R.drawable.bookcover_sample, 3),
        GroupCardItemRoomData("eee", 18, 25, true, 4, R.drawable.bookcover_sample, 4),
        GroupCardItemRoomData("fff", 12, 20, true, 1, R.drawable.bookcover_sample, 0),
        GroupCardItemRoomData("aaa", 22, 30, true, 3, R.drawable.bookcover_sample, 0),
        GroupCardItemRoomData("bbb", 15, 20, true, 7, R.drawable.bookcover_sample, 1),
        GroupCardItemRoomData("ccc", 10, 15, true, 5, R.drawable.bookcover_sample, 2),
        GroupCardItemRoomData("ddd", 8, 12, false, 2, R.drawable.bookcover_sample, 3),
        GroupCardItemRoomData("eee", 18, 25, true, 4, R.drawable.bookcover_sample, 4),
        GroupCardItemRoomData("fff", 12, 20, true, 1, R.drawable.bookcover_sample, 0),
        GroupCardItemRoomData("aaa", 22, 30, true, 3, R.drawable.bookcover_sample, 0),
        GroupCardItemRoomData("bbb", 15, 20, true, 7, R.drawable.bookcover_sample, 1),
        GroupCardItemRoomData("ccc", 10, 15, true, 5, R.drawable.bookcover_sample, 2),
        GroupCardItemRoomData("ddd", 8, 12, false, 2, R.drawable.bookcover_sample, 3),
        GroupCardItemRoomData("eee", 18, 25, true, 4, R.drawable.bookcover_sample, 4),
        GroupCardItemRoomData("fff", 12, 20, true, 1, R.drawable.bookcover_sample, 0),
    )

    val filteredRoomList = remember(
        searchText, selectedGenreIndex, isSearched
    ) {
        if (!isSearched) emptyList() else
            roomList.filter { room ->
                (searchText.isBlank() || room.title.contains(searchText, ignoreCase = true)) &&
                        (selectedGenreIndex == -1 || room.genreIndex == selectedGenreIndex)
            }
    }

    Box(
        Modifier.fillMaxSize()
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            DefaultTopAppBar(
                title = stringResource(R.string.group_room_search_topappbar),
                onLeftClick = {},
            )
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                SearchBookTextField(
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
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (!isSearched) {
                    Text(
                        text = stringResource(R.string.group_recent_search),
                        color = colors.White,
                        style = typography.menu_r400_s14_h24
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
                            recentSearches.take(9).forEach { keyword ->
                                GenreChipButton(
                                    text = keyword,
                                    onClick = {
                                        recentSearches = recentSearches.filterNot { it == keyword }
                                    }
                                )
                            }
                        }
                    }
                }

                if (isSearched) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.group_searched_room_size, roomList.size),
                            color = colors.Grey,
                            style = typography.menu_m500_s14_h24
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 12.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(colors.DarkGrey02)
                    )
                    // 장르 Chips
                    GenreChipRow(
                        modifier = Modifier.width(20.dp),
                        genres = genres,
                        selectedIndex = selectedGenreIndex,
                        onSelect = { selectedGenreIndex = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    if (filteredRoomList.isEmpty()) {
                        // 검색 결과가 없는 경우
                        Column (
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.group_no_search_result1),
                                modifier = Modifier.padding(top = 20.dp),
                                color = colors.White,
                                style = typography.smalltitle_sb600_s18_h24
                            )
                            Text(
                                text = stringResource(R.string.group_no_search_result2),
                                modifier = Modifier.padding(top = 8.dp),
                                color = colors.Grey,
                                style = typography.copy_r400_s14
                            )
                        }

                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            filteredRoomList.forEach { room ->
                                CardItemRoomSmall(
                                    title = room.title,
                                    participants = room.participants,
                                    maxParticipants = room.maxParticipants,
                                    endDate = room.endDate,
                                    imageRes = room.imageRes,
                                    isWide = true
                                )
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(colors.DarkGrey02)
                                )
                            }
                        }
                    }
                }
            }
        }
        if (isSearched) {
            FilterButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 144.dp, end = 20.dp), // 필요시 위치 조정
                selectedOption = selectedSortOption,
                options = sortOptions,
                onOptionSelected = { selectedSortOption = it }
            )
        }
    }
}

// 프리뷰 함수
@Preview()
@Composable
fun GroupSearchScreenPreview() {
    ThipTheme {
        GroupSearchScreen()
    }
}

