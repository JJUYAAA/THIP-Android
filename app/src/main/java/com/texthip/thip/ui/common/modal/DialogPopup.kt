package com.texthip.thip.ui.common.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ActionMediumButton
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun DialogPopup(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = modifier
            .size(width = 320.dp, height = 182.dp)
            .background(
                color = colors.DarkGrey,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            color = colors.White,
            style = typography.smalltitle_m500_s18_h24,
        )
        Text(
            text = description,
            color = colors.White,
            style = typography.copy_r400_s14,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ActionMediumButton(
                text = stringResource(R.string.no),
                contentColor = colors.White,
                backgroundColor = colors.Grey02,
                modifier = Modifier.weight(1f),
                onClick = onCancel,
            )
            ActionMediumButton(
                text = stringResource(R.string.yes),
                contentColor = colors.White,
                backgroundColor = colors.Purple,
                modifier = Modifier.weight(1f),
                onClick = onConfirm,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDialogPopup() {
    DialogPopup(
        title = "대화창의 폐쇄형 질문입니다?",
        description = "대화창의 디스크립션을 입력합니다. 대화창의 디스크립션을 입력합니다.",
        onConfirm = {},
        onCancel = {}
    )
}