package com.texthip.thip.ui.common.cards

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun CardItemRoomSmall(
    modifier: Modifier = Modifier,
    title: String,
    participants: Int,
    maxParticipants: Int,
    endDate: Int?,
    imageRes: Int? = R.drawable.bookcover_sample_small,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .size(width = 232.dp, height = 104.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = colors.DarkGrey50
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
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
                        .size(width = 60.dp, height = 80.dp)
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

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = title,
                        color = colors.White,
                        style = typography.menu_sb600_s14_h24,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.ic_group),
                            contentDescription = "그룹 아이콘",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.card_item_participant_count,
                                    participants,
                                ),
                                style = typography.menu_sb600_s12,
                                color = colors.White
                            )
                            Spacer(modifier = Modifier.width(2.dp))

                            Text(
                                text = stringResource(
                                    R.string.card_item_participant_count_max,
                                    maxParticipants
                                ),
                                style = typography.info_m500_s12,
                                color = colors.Grey
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                    endDate?.let {
                        Text(
                            text = stringResource(R.string.card_item_end_date_recruit, endDate),
                            color = colors.Red,
                            style = typography.menu_sb600_s12_h20
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 360)
@Composable
fun CardItemRoomSmallPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        CardItemRoomSmall(
            title = "방 제목입니다 방 제목입니다",
            participants = 22,
            maxParticipants = 30,
            endDate = 3,
            imageRes = R.drawable.bookcover_sample
        )

    }
}