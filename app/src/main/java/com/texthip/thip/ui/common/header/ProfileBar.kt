package com.texthip.thip.ui.common.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.Grey
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun ProfileBar(
    modifier: Modifier = Modifier,
    profileImage: Painter?,
    topText: String,
    bottomText: String,
    bottomTextColor: Color = colors.NeonGreen, // todo: 서버에서 색 보내주는걸로 받기?
    showSubscriberInfo: Boolean,
    subscriberCount: Int = 0,
    hoursAgo: Int = 0,
    onClick: () -> Unit = { }
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (profileImage != null) {
            Image(
                painter = profileImage,
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Grey)
            )
        }
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
                text = stringResource(R.string.hours_ago, hoursAgo),
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
                profileImage = null,
                topText = "user.01",
                bottomText = stringResource(R.string.influencer),
                showSubscriberInfo = true,
                subscriberCount = 77
            )
            ProfileBar(
                profileImage = null,
                topText = "user.04",
                bottomText = stringResource(R.string.influencer),
                showSubscriberInfo = false,
                hoursAgo = 7
            )
        }
    }
}

