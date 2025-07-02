package com.texthip.thip.ui.common.topappbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.texthip.thip.ui.common.buttons.HeaderButton
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun InputTopAppBar(
    title: String = stringResource(R.string.page_name),
    isRightButtonEnabled: Boolean = false,
    rightButtonName: String = stringResource(R.string.finish),
    isLeftIconVisible: Boolean = true,
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colors.Black)
            .height(56.dp)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLeftIconVisible) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "Back Button",
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { onLeftClick() }
            )
        }

        Text(
            text = title,
            color = colors.White,
            style = typography.bigtitle_b700_s22_h24,
            modifier = Modifier.align(Alignment.Center)
        )

        HeaderButton(
            text = rightButtonName,
            enabled = isRightButtonEnabled,
            onClick = onRightClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 2.dp)
        )
    }
}

@Preview
@Composable
private fun InputTopAppBarPreview() {
    Column {
        InputTopAppBar(
            isRightButtonEnabled = true,
            onLeftClick = {},
            onRightClick = {}
        )
        InputTopAppBar(
            title = "설정 1/2",
            isRightButtonEnabled = false,
            rightButtonName = stringResource(R.string.next),
            isLeftIconVisible = false,
            onLeftClick = {},
            onRightClick = {}
        )
    }
}