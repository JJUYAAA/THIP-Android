package com.texthip.thip.ui.common.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun SingleDigitBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onBackspace: (() -> Unit)? = null,
    containerColor: Color = colors.darkGray01,
    borderColor: Color = Color.Transparent
) {
    val myStyle = typography.smalltitle_sb600_s18_h24.copy(
        lineHeight = 20.sp,
        textAlign = TextAlign.Center,
        color = colors.White
    )

    Box(
        modifier = modifier
            .size(44.dp)
            .background(containerColor, RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = { input ->
                val filtered = input.filter { it.isDigit() }.take(1)
                onValueChange(filtered)
            },
            textStyle = myStyle,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = modifier
                .background(containerColor, RoundedCornerShape(12.dp))
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .onKeyEvent { keyEvent ->
                    if (keyEvent.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_DEL &&
                        keyEvent.type == androidx.compose.ui.input.key.KeyEventType.KeyDown
                    ) {
                        if (value.isEmpty()) {
                            onBackspace?.invoke()
                            true
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                },
            cursorBrush = SolidColor(colors.NeonGreen),
            decorationBox = { innerTextField ->
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { innerTextField() }
            }
        )
    }
}

@Preview
@Composable
fun PreviewSingleDigitBox() {
    ThipTheme {
        var digit by rememberSaveable { mutableStateOf("") }
        Box(
            modifier = Modifier.size(60.dp),
            contentAlignment = Alignment.Center
        ) {
            SingleDigitBox(
                value = digit,
                onValueChange = { digit = it }
            )
        }
    }
}