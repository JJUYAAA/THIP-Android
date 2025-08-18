package com.texthip.thip.ui.feed.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import kotlin.math.roundToInt

@Composable
fun CommentActionPopup(
    text: String,
    textColor: Color = colors.White,
    onClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val density = LocalDensity.current
    val yOffsetPx = with(density) { 45.dp.toPx() }.roundToInt()

    Popup(
        alignment = Alignment.BottomEnd,
        offset = IntOffset(x = 0, yOffsetPx),
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true)
    ) {
        Box(
            modifier = Modifier
                .background(
                    colors.Black,
                    RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = colors.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { onClick() }
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = text,
                style = typography.feedcopy_r400_s14_h20,
                color = textColor
            )
        }
    }
}

@Preview
@Composable
private fun CommentActionPopupPreview() {
    CommentActionPopup(
        text = "삭제하기",
        onClick = { },
        onDismissRequest = { }
    )
}