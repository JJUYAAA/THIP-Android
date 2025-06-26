package com.texthip.thip.ui.common.topappbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTopAppBar(
    title: String = "페이지명",
    isRightButtonEnabled: Boolean = false,
    rightButtonName: String = stringResource(R.string.finish),
    isLeftIconVisible: Boolean = true,
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            if (isLeftIconVisible) {
                IconButton(onClick = onLeftClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "Back Button",
                        tint = Color.Unspecified
                    )
                }
            }
        },
        title = {
            Text(
                text = title,
                color = colors.White,
                style = typography.bigtitle_b700_s22_h24
            )
        },
        actions = {
            HeaderButton(
                text = rightButtonName,
                enabled = isRightButtonEnabled,
                onClick = {
                    onRightClick()
                },
                modifier = Modifier.padding(
                    end = 18.dp
                )
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
    )
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