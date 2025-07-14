package com.texthip.thip.ui.common.forms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onTextChange: (String) -> Unit,
    onDelete: () -> Unit,
) {
    val myStyle = typography.menu_r400_s14_h24.copy(lineHeight = 14.sp)

    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        placeholder = {
            Text(
                text = hint,
                color = colors.Grey02,
                style = myStyle
            )
        },
        textStyle = myStyle,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = colors.White,
            focusedIndicatorColor = colors.Grey02,
            unfocusedIndicatorColor = colors.Grey02,
            focusedContainerColor = colors.Black00,
            unfocusedContainerColor = colors.Black00,
            cursorColor = colors.NeonGreen,
        ),

        trailingIcon = {
            Icon(
                painter = painterResource(
                    if (text.isEmpty()) R.drawable.ic_x_circle_darkgrey
                    else R.drawable.ic_x_circle_grey
                ),
                contentDescription = "Clear",
                modifier = Modifier.clickable {
                    onDelete()
                },
                tint = Color.Unspecified
            )
        }
    )
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
        )
    }
}