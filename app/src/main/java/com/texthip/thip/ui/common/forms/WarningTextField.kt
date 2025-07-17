package com.texthip.thip.ui.common.forms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun WarningTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String,
    warningMessage: String = "",
    showWarning: Boolean = false,
    showLimit: Boolean = true,
    maxLength: Int = Int.MAX_VALUE,
    showIcon: Boolean = false,
    containerColor: Color = colors.Black,
    isNumberOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val myStyle = typography.menu_r400_s14_h24.copy(lineHeight = 14.sp)

    Column {
        Box(
            modifier = modifier
                .height(48.dp)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = { input ->
                    var filtered = input
                    if (isNumberOnly) filtered = filtered.filter { it.isDigit() }
                    if (filtered.length > maxLength) filtered = filtered.take(maxLength)
                    onValueChange(filtered)
                },
                placeholder = {
                    Text(
                        text = hint,
                        color = colors.Grey02,
                        style = myStyle
                    )
                },
                textStyle = myStyle,
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colors.White,
                    focusedIndicatorColor = if (showWarning) colors.Red else Color.Transparent,
                    unfocusedIndicatorColor = if (showWarning) colors.Red else Color.Transparent,
                    focusedContainerColor = containerColor,
                    unfocusedContainerColor = containerColor,
                    cursorColor = colors.NeonGreen
                ),
                trailingIcon = {
                    if (showIcon) {
                        if (value.isNotEmpty()) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_x_circle_white),
                                contentDescription = "Clear text",
                                modifier = Modifier.clickable { onValueChange("")},
                                tint = Color.Unspecified
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_x_circle),
                                contentDescription = "Clear text"
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType)

            )

            if (showLimit && maxLength != Int.MAX_VALUE) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 14.dp)
                ) {
                    Text(
                        text = "${value.length}/$maxLength",
                        color = colors.White,
                        style = typography.info_r400_s12_h24
                    )
                }
            }
        }
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
    var text by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier.size(width = 360.dp, height = 200.dp),
        contentAlignment = Alignment.Center
    ) {
        WarningTextField(
            value = text,
            onValueChange = { text = it },
            hint = "인풋 텍스트",
            showWarning = true,
            showIcon = false,
            showLimit = true,
            maxLength = 10,
            warningMessage = "경고 메시지를 입력해주세요."
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 360, heightDp = 200)
fun WarningTextFieldPreviewNormal() {
    var text by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier.size(width = 360.dp, height = 200.dp),
        contentAlignment = Alignment.Center
    ) {
        WarningTextField(

            value = text,
            onValueChange  = { text = it },
            hint = "인풋 텍스트",
            showWarning = false,
            showIcon = true,
            showLimit = false
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 360, heightDp = 200)
fun WarningTextFieldPreviewNormal_numberonly() {
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.size(width = 360.dp, height = 200.dp),
        contentAlignment = Alignment.Center
    ) {
        WarningTextField(
            value = password,
            onValueChange = { password = it },
            hint = "4자리 숫자로 입장 비밀번호를 설정",
            showWarning = false,
            warningMessage = "4자리 숫자를 입력해주세요.",
            maxLength = 4,
            isNumberOnly = true,
            keyboardType = KeyboardType.NumberPassword
        )
    }
}
