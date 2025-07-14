package com.texthip.thip.ui.common.forms


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography


@Composable
fun BookPageTextField(
    modifier: Modifier = Modifier,
    bookTotalPage: Int,
    enabled: Boolean = true,
    text: String,
    onValueChange: (String) -> Unit,
) {
    var isError by rememberSaveable { mutableStateOf(false) }
    var errorMessageRes by rememberSaveable { mutableStateOf<Int?>(null) }

    Column {
        OutlinedTextField(
            value = text,
            onValueChange = { newText: String ->
                if (newText.isEmpty() || newText.all { it.isDigit() }) {
                    onValueChange(newText)

                    if (newText.isNotEmpty()) {
                        val pageNum = newText.toInt()
                        isError = pageNum > bookTotalPage
                        errorMessageRes = if (isError) {
                            R.string.error_page_over
                        } else {
                            null
                        }
                    } else {
                        isError = false
                        errorMessageRes = null
                    }
                }
            },
            enabled = enabled,
            visualTransformation = if (enabled) {
                SuffixTransformation("/${bookTotalPage}p", colors.Grey02)
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier
                .size(width = 320.dp, height = 48.dp)
                .then(
                    if (isError)
                        Modifier.border(
                            width = 1.dp,
                            color = colors.Red,
                            shape = RoundedCornerShape(12.dp)
                        )
                    else Modifier
                ),
            textStyle = typography.menu_r400_s14_h24.copy(lineHeight = 12.sp),
            maxLines = 1,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = colors.White,
                disabledTextColor = colors.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = colors.DarkGrey02,
                unfocusedContainerColor = colors.DarkGrey02,
                disabledContainerColor = colors.DarkGrey02,
                cursorColor = colors.NeonGreen,
            ),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_x_circle_grey),
                    contentDescription = "Clear text",
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            onValueChange("")
                            isError = false
                            errorMessageRes = null
                        }
                    },
                    tint = Color.Unspecified
                )
            }
        )

        Box(modifier = Modifier.height(22.dp)) {
            if (isError && errorMessageRes != null) {
                Text(
                    modifier = Modifier.padding(start = 4.dp, top = 8.dp),
                    text = stringResource(id = errorMessageRes!!),
                    color = colors.Red,
                    style = typography.menu_r400_s14_h24.copy(lineHeight = 12.sp)
                )
            }
        }
    }
}

class SuffixTransformation(
    private val suffix: String,
    private val suffixColor: Color
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text

        val transformed = buildAnnotatedString {
            append(original)
            pushStyle(SpanStyle(color = suffixColor))
            append(suffix)
            pop()
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                offset.coerceAtMost(original.length)

            override fun transformedToOriginal(offset: Int): Int =
                offset.coerceAtMost(original.length)
        }

        return TransformedText(transformed, offsetMapping)
    }
}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 360, heightDp = 200)
fun BookPageTextFieldPreviewEmpty() {
    var text by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier.size(width = 360.dp, height = 200.dp),
        contentAlignment = Alignment.Center
    ) {
        BookPageTextField(
            bookTotalPage = 456,
            text = text,
            onValueChange = {
                text = it
            }
        )
    }
}