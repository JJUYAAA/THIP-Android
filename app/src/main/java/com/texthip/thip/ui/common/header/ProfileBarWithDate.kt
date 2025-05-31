package com.texthip.thip.ui.common.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.theme.LocalThipColorsProvider
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun ProfileBarWithDate(
    profileImage: Painter?,
    nickname: String,
    dateText: String,
    onMenuClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (profileImage != null) {
            Image(
                painter = profileImage,
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(LocalThipColorsProvider.current.Grey)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = nickname,
                style = typography.menu_sb600_s12,
                color = colors.White
            )
            Text(
                text = dateText,
                style = typography.timedate_r400_s11,
                color = colors.Grey01
            )
        }

        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "메뉴",
            tint = colors.White,
            modifier = Modifier
                .size(24.dp)
                .clickable { onMenuClick() }
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewProfileBarWithDate() {
    ThipTheme {
        ProfileBarWithDate(
            profileImage = null,
            nickname = "user.01",
            dateText = "2025.01.12"
        )
    }
}