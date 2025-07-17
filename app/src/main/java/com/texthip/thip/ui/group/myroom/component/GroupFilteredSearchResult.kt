package com.texthip.thip.ui.group.myroom.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.GenreChipRow
import com.texthip.thip.ui.common.cards.CardItemRoomSmall
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun GroupFilteredSearchResult(
    genres: List<String>,
    selectedGenreIndex: Int,
    onGenreSelect: (Int) -> Unit,
    resultCount: Int,
    roomList: List<GroupCardItemRoomData>
) {
    val colors = ThipTheme.colors
    val typography = ThipTheme.typography

    GenreChipRow(
        modifier = Modifier.width(20.dp),
        genres = genres,
        selectedIndex = selectedGenreIndex,
        onSelect = onGenreSelect
    )
    Spacer(modifier = Modifier.height(20.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.group_searched_room_size, resultCount),
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

    if (roomList.isEmpty()) {
        GroupEmptyResult(
            mainText = stringResource(R.string.group_no_search_result1),
            subText = stringResource(R.string.group_no_search_result2)
        )
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(roomList) { index, room ->
                CardItemRoomSmall(
                    title = room.title,
                    participants = room.participants,
                    maxParticipants = room.maxParticipants,
                    endDate = room.endDate,
                    imageRes = room.imageRes,
                    isWide = true,
                    isSecret = room.isSecret
                )
                if (index < roomList.size - 1) {
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

