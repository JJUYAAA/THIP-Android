package com.texthip.thip.ui.theme.common.card

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun DetailedDarkCard(
    modifier: Modifier = Modifier,
    title: String,
    author: String,
    publisher: String,
    description: String,
    imageRes: Int? = R.drawable.bookcover_sample,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = colors.DarkGrey02
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 헤더 행
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = title,
                    style = typography.menu_m500_s16_h24,
                    color = Color.White,
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                )

                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_right),
                    contentDescription = "더보기",
                    tint = Color.White,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 내용 행
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // 책 이미지
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

                // 텍스트 정보
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Spacer(modifier = Modifier.height(7.dp))

                    Text(
                        text = "$author 저 · $publisher",
                        color = colors.White,
                        style = typography.info_m500_s12,
                    )

                    Spacer(modifier = Modifier.height(21.dp))

                    Text(
                        text = "도서 소개",
                        color = colors.White,
                        style = typography.info_m500_s12,
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = description,
                        color = colors.Grey01,
                        style = typography.timedate_r400_s11,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewDetailedDarkCard() {

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DetailedDarkCard(
            title = "도서명을 입력, 예시까지 최대 입력 후...",
            author = "저자명",
            publisher = "출판사",
            description = "세 줄로 내용을 입력합니다. 세 줄로 내용을 입력합니다. 세 줄로 내용을 입력합니다. 세 줄로 내용을 입력합니다. 세 줄로 내용을 입력합니다. 세 줄로 내용을 입력합니다."
        )
    }

}