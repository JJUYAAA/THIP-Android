package com.texthip.thip.ui.common.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun CardItemRoom(
    modifier: Modifier = Modifier,
    title: String,
    participants: Int,
    maxParticipants: Int,
    isRecruiting: Boolean,
    endDate: Int, // 남은 일 수 (예: 3)
    imageRes: Int? = R.drawable.bookcover_sample,
    hasBorder: Boolean = false,   // <-- 추가
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (hasBorder)
                    Modifier
                        .border(
                            width = 1.dp,
                            color = colors.Grey02,
                            shape = RoundedCornerShape(12.dp)
                        )
                else Modifier
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = colors.DarkGrey50
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // 이미지
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 107.dp)
                ) {
                    imageRes?.let {
                        Image(
                            painter = painterResource(id = it),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = title,
                        color = colors.White,
                        style = typography.smalltitle_sb600_s18_h24,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.ic_group),
                            contentDescription = "그룹 아이콘",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isRecruiting) {
                                "$participants / ${maxParticipants}명"
                            } else {
                                "${participants}명 참여"
                            },
                            color = colors.White,
                            style = typography.info_m500_s12,
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "${endDate}일 뒤 " + if (isRecruiting) "모집 마감" else "종료",
                        color = if (isRecruiting) colors.Red else colors.Grey01,
                        style = typography.menu_sb600_s12_h20,
                        maxLines = 1
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 360)
@Composable
fun CardItemRoomPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        CardItemRoom(
            title = "모임방 이름입니다. 모임방 이름입니다.",
            participants = 22,
            maxParticipants = 30,
            isRecruiting = true,
            endDate = 3,
            imageRes = R.drawable.bookcover_sample
        )
        CardItemRoom(
            title = "모임방 이름입니다. 모임방 이름입니다.",
            participants = 22,
            maxParticipants = 30,
            isRecruiting = false,
            endDate = 3,
            imageRes = R.drawable.bookcover_sample
        )
        CardItemRoom(
            title = "모임방 이름입니다. 모임방 이름입니다.",
            participants = 22,
            maxParticipants = 30,
            isRecruiting = true,
            endDate = 3,
            imageRes = R.drawable.bookcover_sample,
            hasBorder = true
        )
        CardItemRoom(
            title = "모임방 이름입니다. 모임방 이름입니다.",
            participants = 22,
            maxParticipants = 30,
            isRecruiting = false,
            endDate = 3,
            imageRes = R.drawable.bookcover_sample,
            hasBorder = true
        )
    }
}