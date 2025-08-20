package com.texthip.thip.ui.common.topappbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.view.CountingBar
import com.texthip.thip.ui.theme.ThipTheme.colors
import kotlinx.coroutines.delay

@Composable
fun GradationTopAppBar(
    isImageVisible: Boolean = false,
    count: Int = 0,
    autoHideCount: Boolean? = null,
    countDisplayDurationMs: Long = 5000L,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit = {},
) {
    var isCountVisible by remember { mutableStateOf(isImageVisible) }

    // autoHideCount가 null이 아닐 때만 자동 숨김 로직 실행
    LaunchedEffect(autoHideCount, count) {
        when (autoHideCount) {
            true -> {
                if (count > 0) {
                    isCountVisible = true
                    delay(countDisplayDurationMs)
                    isCountVisible = false
                }
            }
            false -> {
                isCountVisible = true
            }
            null -> {
                // 기존 동작 유지: isImageVisible 파라미터 사용
                isCountVisible = isImageVisible
            }
        }
    }

    val bgColor = Brush.verticalGradient(
        colors = listOf(
            colors.Black,
            colors.Black00
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = bgColor)
            .padding(horizontal = 20.dp)
            .height(56.dp),
        contentAlignment = Alignment.Center,
    ) {
        IconButton(
            onClick = onLeftClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "Back Button",
                tint = Color.Unspecified
            )
        }

        Column {
            AnimatedVisibility(
                visible = isCountVisible && count > 0
            ) {
                CountingBar(
                    content = stringResource(R.string.reading_user_num, count)
                )
            }
        }

        /*IconButton(
            onClick = onRightClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_more),
                contentDescription = "More Options",
                tint = Color.Unspecified
            )
        }*/
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