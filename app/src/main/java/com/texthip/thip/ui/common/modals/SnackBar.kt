package com.texthip.thip.ui.common.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SnackBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.DarkGray,
    messageColor: Color = Color.White,
    actionTextColor: Color = Color(0xFFA8F3AC),
    message: String,
    actionText: String,
    onActionClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(backgroundColor, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = message,
                color = messageColor,
                fontSize = 14.sp
            )
            Text(
                text = actionText,
                color = actionTextColor,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onActionClick() }
            )
        }
    }
}