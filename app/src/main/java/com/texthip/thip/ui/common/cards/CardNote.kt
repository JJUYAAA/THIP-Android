package com.texthip.thip.ui.common.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun CardNote(
    currentPage: Int,
    percentage: Double,
    onClick: () -> Unit = { }
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colors.DarkGrey02, shape = RoundedCornerShape(12.dp))
            .padding(vertical = 16.dp, horizontal = 12.dp)
            .clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.record_book),
                style = typography.smalltitle_sb600_s16_h24,
                color = colors.White,
            )

            Icon(
                painter = painterResource(R.drawable.ic_chevron),
                contentDescription = "Chevron Icon",
                tint = colors.Grey02,
            )
        }

        Text(
            text = stringResource(R.string.current_page, currentPage),
            style = typography.info_m500_s12,
            color = colors.Grey,
        )

        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = percentage.toString(),
                style = typography.smalltitle_sb600_s16_h20,
                color = colors.Purple,
            )
            Text(
                text = stringResource(R.string.percent),
                style = typography.menu_sb600_s12,
                color = colors.Purple,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(color = colors.Grey02, shape = RoundedCornerShape(12.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (percentage / 100f).toFloat())
                    .fillMaxHeight()
                    .background(color = colors.Purple, shape = RoundedCornerShape(12.dp))
            )
        }
    }
}

@Preview
@Composable
private fun CardNotePreview() {
    CardNote(
        currentPage = 50,
        percentage = 30.0,
        onClick = { }
    )
}