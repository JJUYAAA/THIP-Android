package com.texthip.thip.ui.group.makeroom.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.group.makeroom.util.WheelPickerUtils
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun <T> GroupWheelPicker(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    displayText: (T) -> String = { it.toString() },
    selectedBackgroundColor: Color = colors.DarkGrey,
    itemHeight: Int = 20,
    isCircular: Boolean = true
) {
    if (items.isEmpty()) return

    val isScrollEnabled = items.size > 1
    val circular = isCircular && items.size > 2

    val selectedIndex = items.indexOf(selectedItem)
    val animatableOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    var isDragging by remember { mutableStateOf(false) }
    var velocity by remember { mutableFloatStateOf(0f) }

    val density = LocalDensity.current
    val itemHeightPx = with(density) { itemHeight.dp.toPx() }
    val spacingPx = with(density) { 9.dp.toPx() }
    val itemSpacing = itemHeightPx + spacingPx

    fun getCircularIndex(index: Int): Int {
        return WheelPickerUtils.getCircularIndex(index, items.size)
    }

    fun normalizeOffset(offset: Float): Float {
        return WheelPickerUtils.normalizeOffset(offset, itemSpacing, items.size, circular)
    }

    fun offsetToIndex(offset: Float): Int {
        return WheelPickerUtils.offsetToIndex(offset, itemSpacing, items.size, circular)
    }

    // 선택 아이템이 바뀌면 중앙에 오도록 offset 이동
    LaunchedEffect(selectedItem) {
        if (!isDragging && isScrollEnabled) {
            val targetOffset = -selectedIndex * itemSpacing
            animatableOffset.animateTo(
                if (circular) normalizeOffset(targetOffset) else targetOffset,
                animationSpec = spring()
            )
        }
    }

    // 오프셋이 바뀔 때 마다 선택 아이템을 갱신
    LaunchedEffect(animatableOffset.value) {
        if (!isDragging && isScrollEnabled) {
            val newSelectedIndex = offsetToIndex(animatableOffset.value)
            if (items[newSelectedIndex] != selectedItem) {
                onItemSelected(items[newSelectedIndex])
            }
        }
    }

    Box(
        modifier = modifier
            .height((itemHeight * 3 + 36).dp)
    ) {
        // 중앙 고정 박스
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .background(
                    selectedBackgroundColor,
                    RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 9.dp)
        ) {
            Text(
                text = displayText(selectedItem),
                style = typography.info_r400_s12,
                color = Color.Transparent,
                textAlign = TextAlign.Center
            )
        }

        // 아이템들
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isScrollEnabled) {
                        Modifier.pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = {
                                    isDragging = true
                                    velocity = 0f
                                },
                                onDragEnd = {
                                    isDragging = false
                                    coroutineScope.launch {
                                        // 관성 스크롤
                                        if (abs(velocity) > 100f) {
                                            animatableOffset.animateDecay(
                                                initialVelocity = velocity,
                                                animationSpec = exponentialDecay(
                                                    frictionMultiplier = 0.9f,
                                                    absVelocityThreshold = 0.1f
                                                )
                                            )
                                        }

                                        // offset 스냅
                                        val currOffset = animatableOffset.value
                                        val normalized =
                                            if (circular) normalizeOffset(currOffset) else currOffset
                                        val snapIndex = (-normalized / itemSpacing).roundToInt()
                                        val snapOffset = -snapIndex * itemSpacing
                                        animatableOffset.animateTo(
                                            if (circular) normalizeOffset(snapOffset) else snapOffset,
                                            animationSpec = spring(
                                                dampingRatio = 0.8f,
                                                stiffness = 400f
                                            )
                                        )
                                    }
                                }
                            ) { _, dragAmount ->
                                velocity = dragAmount.y
                                coroutineScope.launch {
                                    val newOffset = animatableOffset.value + dragAmount.y
                                    if (circular) {
                                        animatableOffset.snapTo(normalizeOffset(newOffset))
                                    } else {
                                        val maxOffset = itemSpacing + spacingPx
                                        val minOffset =
                                            -(items.size - 1) * itemSpacing - itemSpacing - spacingPx
                                        animatableOffset.snapTo(
                                            newOffset.coerceIn(
                                                minOffset,
                                                maxOffset
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Modifier
                    }
                )
        ) {
            val currOffset =
                if (circular && isScrollEnabled) normalizeOffset(animatableOffset.value) else animatableOffset.value
            val centerIndex = if (isScrollEnabled) (-currOffset / itemSpacing).roundToInt() else 0

            // 중앙 + 위 아래 한 개만 보이도록!
            val visibleRange = if (isScrollEnabled) -1..1 else 0..0

            visibleRange.forEach { relIdx ->
                val displayIndex = centerIndex + relIdx
                val actualIndex =
                    if (circular && isScrollEnabled) getCircularIndex(displayIndex) else {
                        if (displayIndex in 0 until items.size) displayIndex else return@forEach
                    }
                val item = items[actualIndex]
                val itemOffset =
                    if (isScrollEnabled) currOffset + (displayIndex * itemSpacing) else 0f
                val itemY = itemOffset + itemSpacing + spacingPx

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight.dp)
                        .offset { IntOffset(0, itemY.roundToInt()) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = displayText(item),
                        style = typography.info_r400_s12,
                        color = colors.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // 그라데이션 오버레이 (아이템이 여러 개일 때만 표시)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colors.Black.copy(alpha = 0.8f),
                            Color.Transparent,
                            Color.Transparent,
                            colors.Black.copy(alpha = 0.8f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun WheelPickerPreview() {
    var selectedYear by remember { mutableStateOf(2025) }
    val years = (2020..2030).toList() // 11개

    var selectedSingleItem by remember { mutableStateOf("Only Item") }
    val singleItemList = listOf("Only Item") // 1개

    Box(
        modifier = Modifier
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 순환식 년 선택기 (4개 이상이므로 순환)
            GroupWheelPicker(
                modifier = Modifier.width(60.dp),
                items = years,
                selectedItem = selectedYear,
                onItemSelected = { selectedYear = it },
                displayText = { it.toString() },
                isCircular = true
            )

            // 단일 아이템 선택기 (스크롤 비활성화)
            GroupWheelPicker(
                modifier = Modifier.width(60.dp),
                items = singleItemList,
                selectedItem = selectedSingleItem,
                onItemSelected = { selectedSingleItem = it },
                displayText = { it },
                isCircular = false
            )
        }
    }
}