package com.texthip.thip.ui.common.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(Color.Transparent)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(colors.DarkGrey, shape = RoundedCornerShape(12.dp))
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                        modifier = Modifier.padding(end = 8.dp),
                        text = content,
                        color = colors.White,
                        style = typography.copy_r400_s12_h20
                    )
                }
            }
        }

        // 아래쪽 X버튼 (원형)
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .size(50.dp)
                .background(Color.Transparent, shape = RoundedCornerShape(24.dp))
                .border(
                    width = 2.dp,
                    color = colors.Grey02,
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_x_20),
                contentDescription = "닫기",
                tint = colors.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@Preview()
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