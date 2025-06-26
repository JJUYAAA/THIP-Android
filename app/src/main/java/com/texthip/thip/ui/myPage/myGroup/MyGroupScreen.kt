package com.texthip.thip.ui.myPage.myGroup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.cards.CardItemRoom
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import androidx.compose.foundation.lazy.items

@Composable
fun MyGroupListScreen(
    dataList: List<CardItemRoomData>,
    onCardClick: (CardItemRoomData) -> Unit = {}
) {
    Column(
        Modifier
            .background(colors.Black)
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 상단 타이틀 (필요 시 아이콘 등 추가)
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = colors.White
            )
            Spacer(modifier = Modifier.width(95.dp))
            Text(
                text = stringResource(R.string.myGroupRoom),
                color = colors.White,
                style = typography.bigtitle_b700_s22_h24
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        // 리스트
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(dataList) { item ->
                CardItemRoom(
                    title = item.title,
                    participants = item.participants,
                    maxParticipants = item.maxParticipants,
                    isRecruiting = item.isRecruiting,
                    endDate = item.endDate,
                    imageRes = item.imageRes,
                    onClick = { onCardClick(item) }
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 360, heightDp = 800)
@Composable
fun MyGroupListScreenPreview() {
    val dataList = mutableListOf(
        CardItemRoomData(
            title = "모임방 이름입니다. 모임방...",
            participants = 22,
            maxParticipants = 30,
            isRecruiting = true,
            endDate = 3
        ),
        CardItemRoomData(
            title = "모임방 이름입니다. 모임방...",
            participants = 22,
            maxParticipants = 30,
            isRecruiting = false,
            endDate = 3
        ),
        CardItemRoomData(
            title = "모임방 이름입니다. 모임방...",
            participants = 22,
            maxParticipants = 30,
            isRecruiting = true,
            endDate = 1
        )
    )
    MyGroupListScreen(dataList)
}