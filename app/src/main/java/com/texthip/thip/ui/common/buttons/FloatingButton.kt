package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun FloatingButton(
    icon: Painter,
    onClick: () -> Unit = { }
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 20.dp, bottom = 32.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        // 플로팅 버튼
        FloatingActionButton(
            onClick = {
                expanded = !expanded
                onClick()
            },
            containerColor = colors.DarkGrey,
            contentColor = colors.NeonGreen,
            shape = CircleShape,
            modifier = Modifier
                .padding(16.dp)
                .size(56.dp)
                .border(width = 2.dp, color = colors.NeonGreen50, shape = CircleShape)
        ) {
            Icon(
                painter = icon,
                modifier = Modifier.size(24.dp),
                tint = colors.NeonGreen,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun FloatingButtonPreview() {
    FloatingButton(
        icon = painterResource(id = R.drawable.ic_write),
    )
}