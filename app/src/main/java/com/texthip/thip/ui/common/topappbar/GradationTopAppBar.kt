package com.texthip.thip.ui.common.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.view.CountingBar
import com.texthip.thip.ui.theme.ThipTheme.colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradationTopAppBar(
    isImageVisible: Boolean = false,
    count: Int = 0,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
) {
    val bgColor = Brush.verticalGradient(
        colors = listOf(
            colors.Black,
            colors.Black00
        )
    )

    Box(
        modifier = Modifier
            .background(brush = bgColor)
            .padding(horizontal = 20.dp)
            .height(56.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "Back Button",
                tint = Color.Unspecified,
                modifier = Modifier
                    .clickable { onLeftClick() }
            )

            if (isImageVisible) {
                CountingBar(
                    modifier = Modifier.width(236.dp),
                    count = count,
                )
            }

            Icon(
                painter = painterResource(R.drawable.ic_more),
                contentDescription = "More Options",
                tint = Color.Unspecified,
                modifier = Modifier
                    .clickable { onRightClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GradationTopAppBarPreview() {
    Column {
        GradationTopAppBar(
            onLeftClick = { },
            onRightClick = { },
            count = 210
        )
        GradationTopAppBar(
            isImageVisible = true,
            onLeftClick = { },
            onRightClick = { },
            count = 210
        )
    }
}