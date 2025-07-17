package com.texthip.thip.ui.common.modal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun ToastWithDate(
    modifier: Modifier = Modifier,
    message: String,
    date: String? = null
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = colors.DarkGrey02)
            .border(
                width = 1.dp,
                color = colors.Grey02,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (date != null) Arrangement.SpaceBetween else Arrangement.Start
        ) {
            Text(
                text = message,
                color = colors.White,
                style = typography.view_m500_s12_h20
            )
            if (date != null) {
                Text(
                    text = date,
                    color = colors.Grey02,
                    style = typography.timedate_r400_s11
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToastPrev() {
    ToastWithDate(
        message = stringResource(R.string.push_off),
        date = "2025년 6월 29일 22시 30분",
        modifier = Modifier.fillMaxWidth()
    )
}