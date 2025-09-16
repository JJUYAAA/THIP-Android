package com.texthip.thip.ui.group.note.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.forms.BorderedTextField
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun VoteInputSection(
    titleValue: TextFieldValue,
    onTitleChange: (TextFieldValue) -> Unit,
    options: List<String>,
    onOptionChange: (index: Int, newText: String) -> Unit,
    onAddOption: () -> Unit,
    onRemoveOption: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    maxOptionLength: Int = 20,
    maxOptions: Int = 5,
    focusRequester: FocusRequester = remember { FocusRequester() },
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .clickable(
                enabled = isEnabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus() // 바깥 클릭 시 포커스 해제
            },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BasicTextField(
            value = titleValue,
            onValueChange = { if (it.text.length <= maxOptionLength) onTitleChange(it) },
            textStyle = typography.smalltitle_m500_s18_h24.copy(color = colors.White),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            cursorBrush = SolidColor(colors.NeonGreen),
            decorationBox = { innerTextField ->
                if (titleValue.text.isEmpty()) {
                    Text(
                        text = stringResource(R.string.vote_title_placeholder),
                        color = colors.Grey02,
                        style = typography.smalltitle_m500_s18_h24
                    )
                }
                innerTextField()
            }
        )

        options.forEachIndexed { index, option ->
            val canRemove = index >= 2 // 3번째 항목부터 삭제 가능
            BorderedTextField(
                text = option,
                onTextChange = {
                    if (it.length <= maxOptionLength) onOptionChange(index, it)
                },
                onDelete = { onOptionChange(index, "") }, // x 버튼 클릭 시 내용 삭제
                onRemoveField = { if (canRemove) onRemoveOption(index) }, // 쓰레기통 클릭 시 항목 제거
                canRemove = canRemove,
                isEnabled = isEnabled,
                hint = stringResource(R.string.vote_content_placeholder)
            )
        }

        if (options.size < maxOptions && isEnabled) {
            Button(
                onClick = {
                    focusManager.clearFocus() // 항목 추가 시 포커스 해제
                    onAddOption()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.White,
                ),
            ) {
                Text(
                    text = stringResource(R.string.add_content),
                    style = typography.smalltitle_sb600_s16_h24,
                    color = colors.Black
                )
            }
        }
    }
}

@Preview
@Composable
private fun VoteInputSectionPreview() {
    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var options by rememberSaveable { mutableStateOf(mutableListOf("", "")) }

    VoteInputSection(
        titleValue = value,
        onTitleChange = { value = it },
        options = options,
        onOptionChange = { index, newText ->
            options = options.toMutableList().also { it[index] = newText }
        },
        onAddOption = {
            if (options.size < 5) {
                options = options.toMutableList().also { it.add("") }
            }
        },
        onRemoveOption = { index ->
            options = options.toMutableList().also { it.removeAt(index) }
        }
    )
}