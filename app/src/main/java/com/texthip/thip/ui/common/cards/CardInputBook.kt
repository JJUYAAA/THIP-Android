package com.texthip.thip.ui.common.cards

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
import com.texthip.thip.ui.common.buttons.OutlinedButton
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun CardInputBook(
    modifier: Modifier = Modifier,
    title: String,
    author: String,
    imageRes: Int? = R.drawable.bookcover_sample, // 기본 이미지 리소스
    onChangeClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent),
    ) {
        // 책 이미지
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

        Spacer(modifier = Modifier.width(12.dp))

        // 텍스트 정보
        Column(
            modifier = Modifier.weight(.1f),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = title,
                style = typography.menu_sb600_s14_h24,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = colors.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.card_input_author, author),
                style = typography.view_m500_s12_h20,
                color = colors.Grey01
            )
        }
        // 텍스트 정보와 버튼 사이 19dp 고정 간격
        Spacer(modifier = Modifier.width(19.dp))

        OutlinedButton(
            modifier = Modifier
                .size(width = 49.dp, height = 33.dp)
                .align(Alignment.Bottom),
            text = stringResource(R.string.change),
            textStyle = typography.view_m500_s14,
            onClick = onChangeClick
        )
    }
}

// 프리뷰들
@Preview
@Composable
fun CardInputBookPreview() {

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CardInputBook(
            title = "책제목입니다.책제목입니다.책제목입니다.책제목입니다.책제목입니다.",
            author = "리처드 도킨스",
            onChangeClick = {}
        )
    }

}