package com.texthip.thip.ui.feed.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun ImageViewerModal(
    images: List<Painter>,
    initialIndex: Int = 0,
    onDismiss: () -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { images.size }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { onDismiss() }
    ) {
        // 닫기 버튼
        Icon(
            painter = painterResource(R.drawable.ic_x),
            contentDescription = "닫기",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
                .size(24.dp)
                .clickable { onDismiss() }
                .zIndex(1f)
        )

        // 이미지 페이저
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .clickable { /* HorizontalPager 내부 클릭 시 모달 닫기 방지 */ }
        ) { page ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = images[page],
                    contentDescription = null,
                    contentScale = ContentScale.Fit, // 원본 비율 유지하면서 화면에 맞춤
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // 페이지 인디케이터 (이미지가 2개 이상일 때만 표시)
        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(images.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == pagerState.currentPage) Color.White
                                else Color.White.copy(alpha = 0.4f)
                            )
                    )
                }
            }
        }

        // 이미지 카운터 (예: 1/3)
        if (images.size > 1) {
            Text(
                text = "${pagerState.currentPage + 1}/${images.size}",
                style = typography.copy_r400_s14,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(20.dp)
                    .background(
                        Color.Black.copy(alpha = 0.5f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}


@Preview
@Composable
private fun ImageViewerModalSingleImagePreview() {
    ThipTheme {
        val mockImages = listOf(
            painterResource(R.drawable.bookcover_sample)
        )

        ImageViewerModal(
            images = mockImages,
            initialIndex = 0,
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun ImageViewerModalMultipleImagesPreview() {
    ThipTheme {
        val mockImages = listOf(
            painterResource(R.drawable.character_art),
            painterResource(R.drawable.character_literature),
            painterResource(R.drawable.character_sociology)
        )

        ImageViewerModal(
            images = mockImages,
            initialIndex = 1,
            onDismiss = { }
        )
    }
}