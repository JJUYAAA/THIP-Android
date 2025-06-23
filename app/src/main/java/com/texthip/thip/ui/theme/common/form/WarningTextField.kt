package com.texthip.thip.ui.theme.common.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun WarningTextField(
    modifier: Modifier = Modifier,
    hint: String,
    warningMessage: String = "경고 메시지를 입력해주세요.",
    showWarning: Boolean
) {
    var text by rememberSaveable { mutableStateOf("") }
    val myStyle = typography.menu_r400_s14_h24.copy(lineHeight = 14.sp)

    Column {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = {
                Text(
                    text = hint,
                    color = colors.Grey02,
                    style = myStyle
                )
            },
            textStyle = myStyle,
            modifier = modifier.size(width = 320.dp, height = 48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = colors.White,
                focusedIndicatorColor = if (showWarning) colors.Red else Color.Transparent,
                unfocusedIndicatorColor = if (showWarning) colors.Red else Color.Transparent,
                focusedContainerColor = colors.Black,
                unfocusedContainerColor = colors.Black,
                cursorColor = colors.NeonGreen
            ),
            trailingIcon = {
                if (text.isNotEmpty()) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_x_circle_white),
                        contentDescription = "Clear text",
                        modifier = Modifier.clickable { text = "" },
                        tint = Color.Unspecified
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_x_circle),
                        contentDescription = "Clear text"
                    )
                }
            },
            singleLine = true
        )

        if (showWarning) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = warningMessage,
                color = colors.Red,
                style = typography.info_r400_s12.copy(lineHeight = 12.sp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 360, heightDp = 200)
fun WarningTextFieldPreviewEmpty() {
    Box(
        modifier = Modifier.size(width = 360.dp, height = 200.dp),
        contentAlignment = Alignment.Center
    ) {
        WarningTextField(
            hint = "인풋 텍스트",
            showWarning = true,
            warningMessage = "경고 메시지를 입력해주세요."
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 360, heightDp = 200)
fun WarningTextFieldPreviewNormal() {
    Box(
        modifier = Modifier.size(width = 360.dp, height = 200.dp),
        contentAlignment = Alignment.Center
    ) {
        WarningTextField(
            hint = "인풋 텍스트",
            showWarning = false
        )
    }
}