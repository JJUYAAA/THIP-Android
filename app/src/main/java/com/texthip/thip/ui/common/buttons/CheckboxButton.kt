package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.theme.ThipTheme.colors

@Composable
fun CheckboxButton(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Checkbox(
        checked = isChecked,
        onCheckedChange = onCheckedChange,
        modifier =
            Modifier
                .border(1.dp, colors.Grey01, RoundedCornerShape(8.dp))
                .size(30.dp),
        colors =
            CheckboxDefaults.colors(
                checkmarkColor = colors.NeonGreen,
                checkedColor = Color.Transparent,
                uncheckedColor = Color.Transparent,
            ),
    )
}

@Preview
@Composable
private fun CheckboxButtonPreview() {
    var isChecked by rememberSaveable { mutableStateOf(false) }

    CheckboxButton(
        isChecked = isChecked,
        onCheckedChange = { isChecked = it }
    )
}