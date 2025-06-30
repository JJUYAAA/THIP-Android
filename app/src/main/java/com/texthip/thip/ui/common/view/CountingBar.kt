package com.texthip.thip.ui.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun CountingBar(
    modifier: Modifier = Modifier,
    content: String,
) {
    Row(
        modifier = modifier
            .background(colors.DarkGrey02, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            content,
            color = colors.NeonGreen,
            style = typography.menu_r400_s14_h24,
            maxLines = 1
        )
    }
}

@Preview
@Composable
private fun CountingBarPrev() {
    CountingBar(
        content = stringResource(R.string.reading_user_num, 200),
    )
}