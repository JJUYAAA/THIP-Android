package com.texthip.thip.ui.common.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun CardChat(
    title: String,
    subtitle: String,
    onClick: () -> Unit = {},
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
                text = title,
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
            text = subtitle,
            style = typography.info_m500_s12,
            color = colors.Grey,
        )
    }
}

@Preview
@Composable
private fun CardChatPreview() {
    CardChat(
        title = "출석체크",
        subtitle = "모임방 멤버들과 간단한 인사를 나눠보세요!",
    ) {}
}