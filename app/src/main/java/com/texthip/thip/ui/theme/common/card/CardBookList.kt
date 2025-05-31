package com.texthip.thip.ui.theme.common.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun CardBookList(
    title: String,
    author: String,
    publisher: String,
    imageRes: Int? = R.drawable.bookcover_sample, // 기본 이미지 리소스
    isBookmarked: Boolean = false,
    onBookmarkClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent),
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

        Spacer(modifier = Modifier.width(12.dp))

        // 텍스트 정보
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = title,
                style = typography.smalltitle_sb600_s16_h20,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = colors.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$author 저 · $publisher",
                style = typography.view_m500_s12_h20,
                color = colors.Grey01
            )
        }

        // TODO: 마크 아이콘 추가
        IconButton(onClick = onBookmarkClick) {
            Icon(
                imageVector = if (isBookmarked) Icons.Default.AddCircle else Icons.Default.Add,
                contentDescription = "북마크",
                tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// 프리뷰들
@Preview
@Composable
fun PreviewBookTitleCard() {
    var isBookmarked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CardBookList(
            title = "책제목입니다.책제목입니다.책제목입니다.책제목입니다.책제목입니다.책제목입니다.",
            author = "리처드 도킨스",
            publisher = "을유문화사",
            isBookmarked = isBookmarked,
            onBookmarkClick = { isBookmarked = !isBookmarked }
        )
    }

}