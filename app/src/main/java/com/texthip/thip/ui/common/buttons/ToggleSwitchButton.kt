package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
fun ToggleSwitchButton(
    isChecked: Boolean,
    enabled: Boolean = true,
    onToggleChange: (Boolean) -> Unit
) {
    Switch(
        modifier = Modifier.height(30.dp),
        checked = isChecked,
        onCheckedChange = onToggleChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = colors.White,
            disabledCheckedThumbColor = colors.White,
            checkedTrackColor = colors.Purple,
            disabledCheckedTrackColor = colors.Purple,
            uncheckedThumbColor = colors.White,
            disabledUncheckedThumbColor = colors.White,
            uncheckedTrackColor = colors.DarkGrey,
            disabledUncheckedTrackColor = colors.DarkGrey,
            uncheckedBorderColor = Color.Transparent,
            disabledUncheckedBorderColor = Color.Transparent,
        ),
        enabled = enabled,
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
    var isChecked by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        ToggleSwitchButton(
            isChecked = isChecked,
            onToggleChange = { isChecked = it }
        )
    }
}
