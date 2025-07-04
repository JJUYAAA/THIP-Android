package com.texthip.thip.ui.common.forms

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun PageTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
) {
    Box(
        modifier = modifier
            .size(width = 36.dp, height = 26.dp)
            .border(width = 1.dp, color = colors.White, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            textStyle = typography.copy_r400_s14.copy(color = colors.White),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            cursorBrush = SolidColor(colors.NeonGreen),
        )
    }
}

@Preview
@Composable
private fun PageTextFieldPreview() {
    var text by rememberSaveable { mutableStateOf("") }

    PageTextField(
        text = text,
        onTextChange = { text = it },
        modifier = Modifier
    )
}