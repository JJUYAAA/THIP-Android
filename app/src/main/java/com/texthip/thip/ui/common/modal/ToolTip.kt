package com.texthip.thip.ui.common.modal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

enum class ArrowPosition {
    LEFT, CENTER, RIGHT
}

@Composable
fun TriangleArrow(
    color: Color = colors.DarkGrey,
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
    arrowPosition: ArrowPosition = ArrowPosition.RIGHT,
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
                color = colors.DarkGrey,
                modifier = arrowModifier
            )
        }

        Box(
            modifier = Modifier
                .background(colors.DarkGrey, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    color = colors.NeonGreen,
                    style = typography.info_m500_s12,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_x),
                    contentDescription = "닫기",
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
    Column {
        PopupModal(
            text = "독서진행도 80%부터 총평을 작성할 수 있습니다.",
            modifier = Modifier
                .width(400.dp)
                .padding(16.dp),
            arrowPosition = ArrowPosition.LEFT,
            onClose = {}
        )
        PopupModal(
            text = "독서진행도 80%부터 총평을 작성할 수 있습니다.",
            modifier = Modifier
                .width(400.dp)
                .padding(16.dp),
            arrowPosition = ArrowPosition.CENTER,
            onClose = {}
        )
        PopupModal(
            text = "독서진행도 80%부터 총평을 작성할 수 있습니다.",
            modifier = Modifier
                .width(400.dp)
                .padding(16.dp),
            arrowPosition = ArrowPosition.RIGHT,
            onClose = {}
        )
    }
}


