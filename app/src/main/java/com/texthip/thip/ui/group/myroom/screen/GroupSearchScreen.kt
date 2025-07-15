package com.texthip.thip.ui.group.myroom.screen

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
import androidx.compose.runtime.derivedStateOf
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

    // 샘플 장르, 정렬 옵션
    val genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술")
    val sortOptions = listOf("마감임박순", "최신순", "참여많은순")

    // 샘플 모임방 리스트
    val roomList = listOf(
        GroupCardItemRoomData("aaa", 22, 30, true, 3, R.drawable.bookcover_sample, 0),
        GroupCardItemRoomData("bbb", 15, 20, true, 7, R.drawable.bookcover_sample, 1, true),
        GroupCardItemRoomData("ccc", 10, 15, true, 5, R.drawable.bookcover_sample, 2, true),
        GroupCardItemRoomData("ddd", 8, 12, false, 2, R.drawable.bookcover_sample, 3, true),
        GroupCardItemRoomData("eee", 18, 25, true, 4, R.drawable.bookcover_sample, 4),
        GroupCardItemRoomData("fff", 12, 20, true, 1, R.drawable.bookcover_sample, 0),
        GroupCardItemRoomData("ggg", 10, 14, true, 6, R.drawable.bookcover_sample, 1),
    )

    val liveFilteredRoomList by remember(searchText) {
        derivedStateOf {
            if (searchText.isBlank()) emptyList() else
                roomList.filter { room ->
                    room.title.contains(searchText, ignoreCase = true)
                }
        }
    }

    val filteredRoomList by remember(searchText, selectedGenreIndex, selectedSortOption, isSearched) {
        derivedStateOf {
            if (!isSearched) emptyList()
            else {
                val filtered = roomList.filter { room ->
                    (searchText.isBlank() || room.title.contains(searchText, ignoreCase = true)) &&
                            (selectedGenreIndex == -1 || room.genreIndex == selectedGenreIndex)
                }
                // 정렬
                when (selectedSortOption) {
                    "마감임박순" -> filtered.sortedBy { it.endDate }
                    "최신순" -> filtered // 아직 미구현
                    "참여많은순" -> filtered.sortedByDescending { it.participants }
                    else -> filtered
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
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

                // 아직 검색어를 입력 안했을 때 + 최근 검색어 없을 때 화면
                if (searchText.isBlank() && !isSearched && recentSearches.isEmpty()) {
                    Text(
                        text = stringResource(R.string.group_recent_search),
                        color = colors.White,
                        style = typography.menu_r400_s14_h24
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.group_no_recent_search),
                        color = colors.Grey01,
                        style = typography.menu_r400_s14_h24
                    )
                }
                // 최근 검색어 있을 때
                else if (searchText.isBlank() && !isSearched && recentSearches.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.group_recent_search),
                        color = colors.White,
                        style = typography.menu_r400_s14_h24
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FlowRow(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        maxLines = 2,
                    ) {
                        recentSearches.take(9).forEach { keyword ->
                            GenreChipButton(
                                text = keyword,
                                onClick = {
                                    searchText = keyword
                                    isSearched = true
                                },
                                onCloseClick = {
                                    recentSearches = recentSearches.filterNot { it == keyword }
                                }
                            )
                        }
                    }
                }
                // 검색어 입력 중
                else if (searchText.isNotBlank() && !isSearched) {
                    if (liveFilteredRoomList.isEmpty()) {
                        Column(
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
                            liveFilteredRoomList.forEach { room ->
                                CardItemRoomSmall(
                                    title = room.title,
                                    participants = room.participants,
                                    maxParticipants = room.maxParticipants,
                                    endDate = room.endDate,
                                    imageRes = room.imageRes,
                                    isWide = true,
                                    isSecret = room.isSecret
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
                // 검색 버튼 터치 화면
                else if (isSearched) {
                    // 장르칩 표시
                    GenreChipRow(
                        modifier = Modifier.width(20.dp),
                        genres = genres,
                        selectedIndex = selectedGenreIndex,
                        onSelect = { selectedGenreIndex = it }
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.group_searched_room_size, filteredRoomList.size),
                            color = colors.Grey,
                            style = typography.menu_m500_s14_h24
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 8.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(colors.DarkGrey02)
                    )

                    if (filteredRoomList.isEmpty()) {
                        Column(
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
                                    isWide = true,
                                    isSecret = room.isSecret
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
                    .padding(top = 176.dp, end = 20.dp),
                selectedOption = selectedSortOption,
                options = sortOptions,
                onOptionSelected = { selectedSortOption = it }
            )
        }
    }
}


@Preview
@Composable
fun GroupSearchScreenPreview() {
    ThipTheme {
        GroupSearchScreen()
    }
}
