package com.texthip.thip.ui.common.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun FormTextFieldDefault(
    modifier: Modifier = Modifier,
    hint: String,
    showLimit: Boolean = false,
    limit: Int = 10,
    showIcon: Boolean = true,
    containerColor: Color = colors.Black
) {
    var text by rememberSaveable { mutableStateOf("") }
    val myStyle = typography.menu_r400_s14_h24.copy(lineHeight = 14.sp)

    // 글자수 제한 적용
    val displayText = if (showLimit && text.length > limit) text.substring(0, limit) else text

    Box(modifier = modifier
        .height(48.dp)) {
        BasicTextField(
            value = displayText,
            onValueChange = {
                // 글자수 제한 적용
                text = if (showLimit && it.length > limit) it.substring(0, limit) else it
            },
            textStyle = myStyle.copy(color = colors.White),
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = containerColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 14.dp, vertical = 12.dp),
            singleLine = true,
            cursorBrush = SolidColor(colors.NeonGreen),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (displayText.isEmpty()) {
                        Text(
                            text = hint,
                            color = colors.Grey02,
                            style = myStyle
                        )
                    }
                    innerTextField()
                    
                    if (showIcon) {
                        if (text.isNotEmpty()) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_x_circle_white),
                                contentDescription = "Clear text",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .clickable { text = "" },
                                tint = Color.Unspecified
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_x_circle),
                                contentDescription = "Clear text",
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                }
            }
        )

        // 글자수 제한 표시 (오른쪽 상단)
        if (showLimit) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 14.dp)
            ) {
                Text(
                    text = "${text.length}/$limit",
                    color = colors.White,
                    style = typography.info_r400_s12_h24
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 360, heightDp = 200)
fun FormTextFieldDefaultPreview() {
    Box(
        modifier = Modifier.size(width = 360.dp, height = 200.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            FormTextFieldDefault(
                hint = "이곳에 텍스트를 입력하세요",
                showLimit = true,
                limit = 20,
                showIcon = false
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            FormTextFieldDefault(
                hint = "이곳에 텍스트를 입력하세요",
                showLimit = false,
                limit = 10,
                showIcon = true
            )
        }
    }
}