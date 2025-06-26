package com.texthip.thip.ui.common.topappbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeftNameTopAppBar(
    title: String = stringResource(R.string.page_name),
    isLeftIconVisible: Boolean = false,
    isRightIconVisible: Boolean = false,
    leftIcon: Painter,
    rightIcon: Painter,
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {}
) {
    TopAppBar(
        navigationIcon = {
            Text(
                text = title,
                color = colors.White,
                style = typography.bigtitle_b700_s22_h24,
                modifier = Modifier.padding(start = 18.dp)
            )
        },
        title = {
            Spacer(modifier = Modifier)
        },
        actions = {
            if (isLeftIconVisible) {
                IconButton(onClick = onLeftClick) {
                    Icon(
                        painter = leftIcon,
                        contentDescription = "Left Icon",
                        tint = Color.Unspecified
                    )
                }
            }
            if (isRightIconVisible) {
                IconButton(onClick = onRightClick) {
                    Icon(
                        painter = rightIcon,
                        contentDescription = "Right Icon",
                        tint = Color.Unspecified
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        )
    )
}

@Preview
@Composable
private fun LeftNameTopAppBarPreview() {
    Column {
        LeftNameTopAppBar(
            leftIcon = painterResource(R.drawable.ic_search),
            rightIcon = painterResource(R.drawable.ic_plus)
        )
        LeftNameTopAppBar(
            leftIcon = painterResource(R.drawable.ic_search),
            rightIcon = painterResource(R.drawable.ic_plus),
            isLeftIconVisible = true,
            isRightIconVisible = true
        )
    }
}
