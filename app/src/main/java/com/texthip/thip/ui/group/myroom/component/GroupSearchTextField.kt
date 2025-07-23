package com.texthip.thip.ui.group.myroom.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun GroupSearchTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = stringResource(R.string.group_search_placeholder),
    onValueChange: (String) -> Unit,
    onClick: (() -> Unit)? = null
) {
    val textStyle = typography.menu_r400_s14_h24.copy(
        fontSize = 14.sp,
        lineHeight = 16.sp,
        color = colors.White
    )
    val shape = RoundedCornerShape(12.dp)
    val backgroundColor = colors.DarkGrey50
    val cursorColor = colors.NeonGreen

    Box(
        modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(40.dp)
            .clip(shape)
            .background(backgroundColor),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = textStyle,
                cursorBrush = SolidColor(cursorColor),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp, end = 8.dp),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = colors.Grey02,
                                style = typography.menu_r400_s14_h24.copy(
                                    fontSize = 14.sp,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "검색",
                tint = colors.White,
                modifier = Modifier
                    .padding(end = 12.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 360)
@Composable
fun PreviewGroupSearchTextField() {
    var value by remember { mutableStateOf("") }
    GroupSearchTextField(
        value = value,
        onValueChange = { value = it }
    )
}
