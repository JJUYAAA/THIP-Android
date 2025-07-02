package com.texthip.thip.ui.group.myroom.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.OptionChipButton

@Composable
fun GroupMyRoomFilterRow(
    selectedStates: BooleanArray,
    onToggle: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OptionChipButton(
            text = stringResource(R.string.on_going),
            isFilled = true,
            onClick = { onToggle(0) }
        )
        OptionChipButton(
            text = stringResource(R.string.recruiting),
            isFilled = true,
            onClick = { onToggle(1) }
        )
    }
}

