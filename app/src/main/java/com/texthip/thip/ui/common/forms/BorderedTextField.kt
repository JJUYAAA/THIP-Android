package com.texthip.thip.ui.common.forms

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun BorderedTextField(
    modifier: Modifier = Modifier,
    hint: String,
    text: String,
    canRemove: Boolean = true,
    isEnabled: Boolean = true,
    onTextChange: (String) -> Unit,
    onDelete: () -> Unit,
    onRemoveField: () -> Unit
) {
    val myStyle = typography.menu_r400_s14_h24.copy(lineHeight = 14.sp)
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val iconRes = if (isEnabled) {
        when {
            isFocused && text.isEmpty() -> R.drawable.ic_x_circle_darkgrey
            isFocused && text.isNotEmpty() -> R.drawable.ic_x_circle_grey
            !isFocused && canRemove -> R.drawable.ic_delete
            else -> null
        }
    } else {
        null
    }

    val iconEnabled = when (iconRes) {
        R.drawable.ic_x_circle_grey -> true
        R.drawable.ic_delete -> true
        else -> false
    }

    val iconTint = when (iconRes) {
        R.drawable.ic_delete -> colors.Grey02

        else -> Color.Unspecified
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(width = 1.dp, color = colors.Grey02, shape = RoundedCornerShape(12.dp)),
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            enabled = isEnabled,
            textStyle = myStyle.copy(color = colors.White),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .matchParentSize()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            interactionSource = interactionSource,
            cursorBrush = SolidColor(colors.NeonGreen),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (text.isEmpty()) {
                            Text(
                                text = hint,
                                color = colors.Grey02,
                                style = myStyle
                            )
                        }
                        innerTextField()
                    }

                    iconRes?.let {
                        Icon(
                            painter = painterResource(id = it),
                            contentDescription = "Action Icon",
                            modifier = Modifier
                                .clickable(enabled = iconEnabled) {
                                    when (it) {
                                        R.drawable.ic_x_circle_grey -> onDelete()
                                        R.drawable.ic_delete -> onRemoveField()
                                    }
                                },
                            tint = iconTint
                        )
                    }
                }
            }
        )
    }
}

@Composable
@Preview()
fun BorderedTextFieldPreview() {
    ThipTheme {
        var text by rememberSaveable { mutableStateOf("") }

        BorderedTextField(
            hint = "가이드 텍스트를 입력",
            text = text,
            onTextChange = { newText ->
                text = newText
            },
            onDelete = {
                text = ""
            },
            onRemoveField = {
                // 필드 제거
            },
        )
    }
}