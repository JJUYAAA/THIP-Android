package com.texthip.thip.ui.group.myroom.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.theme.ThipTheme

@Composable
fun GroupEmptyResultScreen(
    mainText: String,
    subText: String
) {
    val colors = ThipTheme.colors
    val typography = ThipTheme.typography
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = mainText,
            modifier = Modifier.padding(top = 20.dp),
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
