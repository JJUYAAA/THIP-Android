package com.texthip.thip.ui.common.modal

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun InfoPopup(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    onDismiss: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.DarkGrey, shape = RoundedCornerShape(12.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    color = colors.White,
                    style = typography.smalltitle_sb600_s16_h24
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
                    color = colors.White,
                    style = typography.copy_r400_s12_h20
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
        title = stringResource(R.string.introduction),
        content = dummyText,
        onDismiss = {}
    )
}