package com.texthip.thip.ui.common.header

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun ProfileBarFeed(
    profileImage: String?,
    nickname: String,
    genreName: String,
    genreColor: Color = colors.NeonGreen,
    date: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            if (profileImage != null) {
                AsyncImage(
                    model = profileImage,
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
                        .background(colors.Grey)
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = nickname,
                    style = typography.menu_sb600_s12,
                    color = colors.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = genreName,
                    style = typography.timedate_r400_s11,
                    color = genreColor
                )
            }
        }

        Text(
            text = date,
            style = typography.timedate_r400_s11,
            color = colors.Grey01
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileBarFeedPreview() {
    ThipTheme {
        ProfileBarFeed(
            profileImage = null,
            nickname = "user.01",
            genreName = "칭호칭호",
            genreColor = colors.SocialScience,
            date = "2025.01.12"
        )
    }
}