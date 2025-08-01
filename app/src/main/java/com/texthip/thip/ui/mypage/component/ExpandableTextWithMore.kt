package com.texthip.thip.ui.mypage.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

//TODO 수정 후 SavedFeedCard에 적용 예정
@Composable
fun ExpandableTextWithMore(
    maxLinesWhenCollapsed: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var truncatedText by remember { mutableStateOf("") }
    var shouldShowMore by remember { mutableStateOf(false) }

    val overflowTag = "MORE"

    if (isExpanded) {
        Text(
            text = text,
            style = typography.feedcopy_r400_s14_h20,
            color = colors.White,
            modifier = modifier
        )
    } else {
        Text(
            text = buildAnnotatedString {
                append(truncatedText)
                if (shouldShowMore) {
                    append(" ")
                    pushStringAnnotation(tag = overflowTag, annotation = "more")
                    withStyle(style = SpanStyle(color = colors.White)) {
                        append("…더보기")
                    }
                    pop()
                }
            },
            maxLines = maxLinesWhenCollapsed,
            overflow = TextOverflow.Clip, //직접 컨트롤
            style = typography.feedcopy_r400_s14_h20,
            color = colors.White,
            modifier = modifier.clickable {
                if (shouldShowMore) isExpanded = true
            },
            onTextLayout = { result ->
                val isOverflowing = result.lineCount > maxLinesWhenCollapsed
                if (isOverflowing && !shouldShowMore) {
                    shouldShowMore = true
                    val end = result.getLineEnd(maxLinesWhenCollapsed - 1, visibleEnd = true)
                    truncatedText = text.take(end).trimEnd()
                } else if (!isOverflowing && !shouldShowMore) {
                    // 줄 수 안 넘는 경우 → 본문 그대로
                    truncatedText = text
                }
            }
        )
    }
}