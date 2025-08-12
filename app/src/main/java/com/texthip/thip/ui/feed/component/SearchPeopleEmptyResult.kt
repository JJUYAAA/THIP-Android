package com.texthip.thip.ui.feed.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun SearchPeopleEmptyResult(
    modifier: Modifier= Modifier,
    mainText: String
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
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPeopleEmptyResultPrev() {
    ThipTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SearchPeopleEmptyResult(
                mainText = "검색 결과가 없습니다"
            )
        }
    }
}
