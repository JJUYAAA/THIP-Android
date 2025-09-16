package com.texthip.thip.ui.group.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun GroupEmptyResult(
    mainText: String,
    subText: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = mainText,
            color = colors.White,
            style = typography.smalltitle_sb600_s18_h24
        )
        Text(
            text = subText,
            modifier = Modifier.padding(top = 8.dp),
            color = colors.Grey,
            style = typography.copy_r400_s14
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GroupEmptyResultPreview() {
    ThipTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            GroupEmptyResult(
                mainText = "검색 결과가 없습니다",
                subText = "다른 키워드로 검색해보세요"
            )
        }
    }
}
