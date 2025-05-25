package com.texthip.thip.ui.common.modals

import com.texthip.thip.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SnackBar(
    message: String,
    actionText: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = colorResource(id = R.color.darkgray2),
    messageColor: Color = colorResource(id=R.color.primary_white),
    actionTextColor: Color = colorResource(id=R.color.point_green)
) {
    Box(
        modifier = modifier
            .background(backgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 15.dp, vertical = 15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = message,
                color = messageColor,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.medium))
            )
            Text(
                text = actionText,
                color = actionTextColor,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.semibold)),
                modifier = Modifier.clickable { onActionClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SnackBarPreview() {
    SnackBar(
        message = "댓글 작성이 완료되었습니다.",
        actionText = "보러가기",
        onActionClick = {},
        modifier = Modifier
            .fillMaxWidth()
    )
}