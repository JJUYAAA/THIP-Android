package com.texthip.thip.ui.common.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun CommentTextField(
    modifier: Modifier = Modifier,
    hint: String,
    input: String,
    onInputChange: (String) -> Unit,
    onSendClick: () -> Unit,
    replyTo: String? = null,
    onCancelReply: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colors.DarkGrey02)
    ) {
        if (replyTo != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 2.dp)
                    .height(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_reply),
                    contentDescription = "Reply Arrow",
                    tint = colors.Grey01,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = colors.NeonGreen)) {
                            append("@$replyTo")
                        }
                        append(stringResource(R.string.comment_to))
                    },
                    style = typography.info_r400_s12_h24,
                    color = colors.Grey01
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onCancelReply) {
                    Icon(
                        painter = painterResource(R.drawable.ic_x),
                        contentDescription = "Cancel Reply",
                        tint = colors.White
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .height(36.dp)
                .background(colors.DarkGrey, shape = RoundedCornerShape(20.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = input,
                onValueChange = onInputChange,
                singleLine = true,
                textStyle = typography.menu_r400_s14_h24.copy(color = colors.White),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                cursorBrush = SolidColor(colors.NeonGreen),
                decorationBox = { innerTextField ->
                    if (input.isEmpty()) {
                        Text(
                            text = hint,
                            color = colors.Grey02,
                            style = typography.menu_r400_s14_h24
                        )
                    }
                    innerTextField()
                }
            )

            // 전송 버튼
            val isEnabled = input.isNotBlank()
            Box(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(width = 42.dp, height = 28.dp)
                    .background(
                        color = if (isEnabled) colors.Purple else colors.Grey02,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable(enabled = isEnabled) {
                        onSendClick()
                        focusManager.clearFocus()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "Send",
                    tint = colors.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun CommentTextFieldPreview() {
    var input by remember { mutableStateOf("") }
    var replyTo by remember { mutableStateOf<String?>("사용자") }

    ThipTheme {
        CommentTextField(
            input = input,
            hint = "00님에게 댓글을 남겨보세요.",
            onInputChange = { input = it },
            onSendClick = {
                input = ""
                replyTo = null
            },
            replyTo = replyTo,
            onCancelReply = { replyTo = null }
        )
    }
}