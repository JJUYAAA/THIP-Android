package com.texthip.thip.ui.common.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.texthip.thip.R

@Composable
fun InfoPopup(
    title: String,
    content: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.darkgray), shape = RoundedCornerShape(12.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    color = colorResource(id = R.color.primary_white),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.semibold))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(284.dp)
                    .verticalScroll(scrollState)
                    .drawVerticalScrollbar(scrollState)
            ) {
                Text(
                    text = content,
                    color = colorResource(id = R.color.secondary_gray),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.regular)),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInfoPopup() {
    val dummyText = """
        InfoPopup프리뷰입니당!!!InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        InfoPopup프리뷰입니당!!!
        마지막 
    """.trimIndent()

    InfoPopup(
        title = "소개",
        content = dummyText,
        onDismiss = {}
    )
}