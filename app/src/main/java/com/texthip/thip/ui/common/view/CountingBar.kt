package com.texthip.thip.ui.common.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.texthip.thip.R

@Composable
fun CountingBar(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(colorResource(id = R.color.darkgray2), shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = text,
                color = colorResource(id = R.color.point_green),
                fontSize = 14.sp
            )
        }
    }
}

@Preview
@Composable
private fun CountingBarPrev() {
    CountingBar(
        text = stringResource(id = R.string.counting_bar_text),
        modifier = Modifier
            .fillMaxWidth()
    )
}