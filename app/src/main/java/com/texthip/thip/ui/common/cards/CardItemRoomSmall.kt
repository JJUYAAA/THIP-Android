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
import coil.compose.AsyncImage
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun CardItemRoomSmall(
    modifier: Modifier = Modifier,
    title: String,
    participants: Int,
    maxParticipants: Int,
    endDate: Int?,
    imageUrl: String? = null, // API에서 받은 이미지 URL
    isWide: Boolean = false,
    isSecret: Boolean = false,
    onClick: () -> Unit = {}
) {
    val cardModifier = if (isWide) {
        modifier
            .fillMaxWidth()
    } else {
        modifier
            .width(232.dp)
    }
    val bgColor = if (isWide) colors.Black else colors.DarkGrey

    Card(
        modifier = cardModifier
            .height(104.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = bgColor
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
                    AsyncImage(
                        model = imageUrl ?: R.drawable.bookcover_sample_small,
                        contentDescription = "책 이미지",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    if (isSecret) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_secret_cover),
                            contentDescription = "비밀방",
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = title,
                        color = colors.White,
                        style = if (isWide) typography.smalltitle_sb600_s18_h24 else typography.menu_sb600_s14_h24,
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
                    Spacer(modifier = Modifier.height(7.dp))

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

@Preview()
@Composable
fun CardItemRoomSmallPreview() {
    ThipTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            CardItemRoomSmall(
                title = "방 제목입니다 방 제목입니다",
                participants = 22,
                maxParticipants = 30,
                endDate = 3
            )

            CardItemRoomSmall(
                title = "와이드 카드 fillMaxWidth",
                participants = 18,
                maxParticipants = 25,
                endDate = 5,
                isWide = true
            )
        }
    }
}
