package com.texthip.thip.ui.group.note.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun OpinionInputSection(
    title: String = stringResource(R.string.my_opinion_title),
    textFieldValue: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    hint: String = stringResource(R.string.my_opinion_placeholder),
    maxLength: Int = 500,
    focusRequester: FocusRequester = remember { FocusRequester() }
) {
    val text = textFieldValue.text

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = typography.smalltitle_sb600_s18_h24,
            color = colors.White
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            BasicTextField(
                value = textFieldValue,
                onValueChange = { newTextFieldValue ->
                    if (newTextFieldValue.text.length <= maxLength) {
                        onTextChange(newTextFieldValue)
                    }
                },
                textStyle = typography.menu_r400_s14_h24.copy(color = colors.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                cursorBrush = SolidColor(colors.NeonGreen),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.background(color = colors.Black)
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = hint,
                                color = colors.Grey02,
                                style = typography.menu_r400_s14_h24
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
                text = stringResource(R.string.group_input_count, text.length, maxLength),
                color = colors.NeonGreen,
                style = typography.info_r400_s12
            )
        }
    }
}

@Preview
@Composable
private fun OpinionInputSectionPreview() {
    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    OpinionInputSection(
        textFieldValue = value,
        onTextChange = { value = it }
    )
}