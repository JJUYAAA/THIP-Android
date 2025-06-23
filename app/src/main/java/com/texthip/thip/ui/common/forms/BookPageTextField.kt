package com.texthip.thip.ui.common.forms


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
    bookPage: Int
) {
    var text by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }
    var errorMessageRes by rememberSaveable { mutableStateOf<Int?>(null) }
    var errorMessageParam by rememberSaveable { mutableStateOf(0) }

    Column {
        OutlinedTextField(
            value = text,
            onValueChange = { newText: String ->
                if (newText.isEmpty() || newText.all { it.isDigit() }) {
                    text = newText
                    if (newText.isNotEmpty()) {
                        val pageNum = newText.toInt()
                        isError = pageNum > bookPage
                        if (isError) {
                            errorMessageRes = R.string.error_page_over
                            errorMessageParam = bookPage
                        } else {
                            errorMessageRes = null
                        }
                    } else {
                        isError = false
                        errorMessageRes = null
                    }
                }
            },
            visualTransformation = SuffixTransformation(
                suffix = "/${bookPage}p",
                suffixColor = colors.Grey02
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier.size(width = 320.dp, height = 48.dp),
            textStyle = typography.menu_r400_s14_h24.copy(lineHeight = 12.sp),
            maxLines = 1,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = colors.White,
                focusedIndicatorColor = if (isError) colors.Red else Color.Transparent,
                unfocusedIndicatorColor = if (isError) colors.Red else Color.Transparent,
                focusedContainerColor = colors.Black,
                unfocusedContainerColor = colors.Black,
                cursorColor = colors.NeonGreen
            ),
            trailingIcon = {
                if (text.isNotEmpty()) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_x_circle_white),
                        contentDescription = "Clear text",
                        modifier = Modifier.clickable {
                            text = ""
                            isError = false
                            errorMessageRes = null
                        },
                        tint = Color.Unspecified
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_x_circle),
                        contentDescription = "Clear text"
                    )
                }
            }
        )

        if (isError && errorMessageRes != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = errorMessageRes!!, errorMessageParam),
                color = colors.Red,
                style = typography.menu_r400_s14_h24.copy(lineHeight = 12.sp)
            )
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
    Box(
        modifier = Modifier.size(width = 360.dp, height = 200.dp),
        contentAlignment = Alignment.Center
    ) {
        BookPageTextField(
            bookPage = 456
        )
    }
}