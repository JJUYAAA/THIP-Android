package com.texthip.thip.ui.group.makeroom.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun GroupInputField(
    title: String,
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    maxLength: Int = 75
) {
    val isOverflow = value.length >= maxLength

    Column(
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = typography.smalltitle_sb600_s18_h24,
            color = colors.White
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            BasicTextField(
                value = value,
                onValueChange = { new ->
                    if (new.length <= maxLength) onValueChange(new)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 12.dp),
                textStyle = typography.menu_r400_s14_h24.copy(color = colors.White),
                cursorBrush = SolidColor(colors.NeonGreen),
                decorationBox = { innerTextField ->
                    Box(
                        Modifier.fillMaxWidth()
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                hint,
                                style = typography.menu_r400_s14_h24,
                                color = colors.Grey02
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = stringResource(R.string.group_input_count, value.length, maxLength),
                style = typography.info_r400_s12,
                color = if (isOverflow) colors.Red else colors.NeonGreen
            )
        }
    }
}


@Preview()
@Composable
fun PreviewRoomTitleInputField() {
    var text by remember { mutableStateOf("") }

    ThipTheme {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            GroupInputField(
                title = "방 제목",
                hint = "방 제목을 입력해주세요",
                value = text,
                onValueChange = { text = it },
                maxLength = 15
            )
        }
    }
}
