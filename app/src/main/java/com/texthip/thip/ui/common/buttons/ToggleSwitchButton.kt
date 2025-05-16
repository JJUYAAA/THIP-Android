package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun ToggleSwitchButton() {
    var checked by remember { mutableStateOf(true) }

    Switch(
        modifier = Modifier.padding(4.dp),
        checked = checked,
        onCheckedChange = {
            checked = it
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = colors.White,
            checkedTrackColor = colors.Purple,
            uncheckedThumbColor = colors.White,
            uncheckedTrackColor = colors.DarkGrey,
            uncheckedBorderColor = Color.Transparent,
        ),
        thumbContent = {
            Icon(
                painter = painterResource(R.drawable.ic_circle),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }
    )
}

@Preview
@Composable
private fun ToggleSwitchButtonPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        ToggleSwitchButton()
    }
}
