package com.texthip.thip.ui.common.header

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun ProfileBarWithDate(
    profileImage: String,
    nickname: String,
    dateText: String,
    onMenuClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            AsyncImage(
                model = profileImage,
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .border(
                        width = 0.5.dp,
                        color = colors.Grey02,
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = nickname,
                    style = typography.menu_sb600_s12,
                    color = colors.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = dateText,
                    style = typography.timedate_r400_s11,
                    color = colors.Grey01
                )
            }
        }

        Icon(
            painter = painterResource(R.drawable.ic_more),
            contentDescription = "메뉴",
            tint = Color.Unspecified,
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
            profileImage = "https://example.com",
            nickname = "user.01",
            dateText = "2025.01.12"
        )
    }
}