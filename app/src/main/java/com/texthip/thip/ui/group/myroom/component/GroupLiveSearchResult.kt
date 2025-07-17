package com.texthip.thip.ui.group.myroom.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.common.cards.CardItemRoomSmall
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun GroupLiveSearchResult(
    roomList: List<GroupCardItemRoomData>
) {
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
