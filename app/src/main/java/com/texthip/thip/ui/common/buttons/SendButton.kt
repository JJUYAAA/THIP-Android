package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun SendButton(
    modifier: Modifier = Modifier,
    icon: Int,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(
                color = if (enabled) colors.Purple else colors.Grey02,
                shape = RoundedCornerShape(20.dp)
            )
            .let {
                if (enabled) it.clickable(onClick = onClick) else it
            }
            .padding(vertical = 2.dp, horizontal = 9.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Send Icon",
            tint = colors.White,
        )
    }
}

@Preview
@Composable
private fun SendButtonPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 비활성 상태
        SendButton(
            icon = R.drawable.ic_send,
            enabled = false,
            onClick = { }
        )

        // 활성 상태
        SendButton(
            icon = R.drawable.ic_send,
            onClick = { }
        )
    }
}
