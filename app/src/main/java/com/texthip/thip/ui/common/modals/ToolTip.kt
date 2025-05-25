package com.texthip.thip.ui.common.modals

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.texthip.thip.R

enum class ArrowPosition {
    LEFT, CENTER, RIGHT
}

@Composable
fun TriangleArrow(
    color: Color = colorResource(id = R.color.darkgray),
    size: Dp = 13.dp,
    modifier: Modifier = Modifier
) {
    val triangleSizePx = with(androidx.compose.ui.platform.LocalDensity.current) { size.toPx() }

    Canvas(modifier = modifier.size(size)) {
        val path = Path().apply {
            moveTo(0f, triangleSizePx)
            lineTo(triangleSizePx / 2f, 0f)
            lineTo(triangleSizePx, triangleSizePx)
            close()
        }
        drawPath(path, color)
    }
}

@Composable
fun PopupModal(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = colorResource(id = R.color.darkgray),
    textColor: Color = colorResource(id = R.color.point_green),
    arrowPosition: ArrowPosition = ArrowPosition.CENTER,
    onClose: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp)
        ) {
            val arrowModifier = when (arrowPosition) {
                ArrowPosition.LEFT -> Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 24.dp)
                ArrowPosition.CENTER -> Modifier
                    .align(Alignment.TopCenter)
                ArrowPosition.RIGHT -> Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 24.dp)
            }

            TriangleArrow(
                color = backgroundColor,
                modifier = arrowModifier
            )
        }

        Box(
            modifier = Modifier
                .background(backgroundColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    color = textColor,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.medium)),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "닫기",
                    tint =  colorResource(id = R.color.primary_white),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onClose() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PopupPrev() {
    PopupModal(
        text = "독서진행도 80%부터 총평을 작성할 수 있습니다.",
        modifier = Modifier
            .width(400.dp)
            .padding(16.dp),
        arrowPosition=ArrowPosition.LEFT,
        onClose = {}
    )
}

