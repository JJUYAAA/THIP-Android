package com.texthip.thip.ui.common.header

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun ProfileBar(
    modifier: Modifier = Modifier,
    profileImage: String?,
    topText: String,
    bottomText: String,
    bottomTextColor: Color = colors.NeonGreen,
    showSubscriberInfo: Boolean,
    subscriberCount: Int = 0,
    hoursAgo: String = "",
    onClick: () -> Unit = { }
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = profileImage,
            contentDescription = "프로필 이미지",
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(
                    width = 0.5.dp,
                    color = colors.Grey02,
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = topText,
                style = typography.view_m500_s14,
                color = colors.White
            )
            Text(
                text = bottomText,
                style = typography.info_r400_s12,
                color = bottomTextColor
            )
        }
        if (showSubscriberInfo) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = subscriberCount.toString(),
                    style = typography.timedate_r400_s11,
                    color = colors.Grey01
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = stringResource(R.string.subscriber_num),
                    style = typography.timedate_r400_s11,
                    color = colors.Grey01
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = "화살표",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Unspecified
                )
            }
        } else {
            Text(
                text = hoursAgo,
                style = typography.timedate_r400_s11,
                color = colors.Grey01
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewProfileBar() {
    ThipTheme {
        Column {
            ProfileBar(
                profileImage = "문학_image",
                topText = "user.01",
                bottomText = stringResource(R.string.influencer),
                showSubscriberInfo = true,
                subscriberCount = 77
            )
            ProfileBar(
                profileImage = "문학_image",
                topText = "user.04",
                bottomText = stringResource(R.string.influencer),
                showSubscriberInfo = false,
                hoursAgo = "10시간 전"
            )
        }
    }
}

