package com.texthip.thip.ui.common.bottomsheet

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import kotlinx.coroutines.launch

data class MenuBottomSheetItem(
    val text: String,
    val color: Color = Color.White,
    val onClick: () -> Unit,
)

@Composable
fun MenuBottomSheet(
    items: List<MenuBottomSheetItem>,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val animatableOffset = remember { Animatable(300f) } // 시작 위치 아래
    var offsetY by remember { mutableFloatStateOf(0f) }
    var isDismissing by remember { mutableStateOf(false) }

    // 등장 애니메이션
    LaunchedEffect(Unit) {
        animatableOffset.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 300)
        )
    }

    // 바깥 클릭 감지
    Box(
        modifier = Modifier
            .fillMaxSize()
            .blur(5.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (!isDismissing) {
                    isDismissing = true
                    scope.launch {
                        animatableOffset.animateTo(300f, tween(300))
                        onDismiss()
                    }
                }
            }
            .zIndex(1f) // 다른 컴포넌트 위에 뜨도록
    )


    // BottomSheet 본체
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(2f), // 시트가 배경보다 위에 뜨도록
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (offsetY + animatableOffset.value).dp)
                .background(
                    color = colors.DarkGrey,
                    shape = RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp)
                )
                .padding(20.dp)
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { _, dragAmount ->
                            if (dragAmount > 0) {
                                offsetY += dragAmount / 2  // 아래로 드래그할 때만 적용
                            }
                        },
                        onDragEnd = {
                            if (offsetY > 100f && !isDismissing) {
                                isDismissing = true
                                scope.launch {
                                    animatableOffset.animateTo(300f, tween(300))
                                    onDismiss()
                                }
                            } else {
                                offsetY = 0f
                            }
                        }
                    )
                }
                .clickable(enabled = true) {} // 내부 클릭 무시되지 않도록
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                items.forEachIndexed { index, item ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(modifier = Modifier.height(1.dp), color = colors.Grey03)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Column(
                        modifier = Modifier
                            .height(50.dp)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = item.text,
                            style = typography.menu_m500_s16_h24,
                            color = item.color,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    item.onClick()
                                    onDismiss()
                                }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MenuBottomSheetPreview() {
    MenuBottomSheet(
        items = listOf(
            MenuBottomSheetItem(
                text = stringResource(R.string.leave_room),
                color = colors.White,
                onClick = { }
            ),
            MenuBottomSheetItem(
                text = stringResource(R.string.report_room),
                color = colors.Red,
                onClick = { }
            ),
            MenuBottomSheetItem(
                text = stringResource(R.string.delete_room),
                color = colors.Red,
                onClick = { }
            )
        ),
        onDismiss = {}
    )
}