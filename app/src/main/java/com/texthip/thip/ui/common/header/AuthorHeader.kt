package com.texthip.thip.ui.common.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.R
import com.texthip.thip.ui.theme.Grey02

@Composable
fun AuthorHeader(
    modifier: Modifier = Modifier,
    profileImage: Painter?,
    nickname: String,
    badgeText: String,
    onSubscribeClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (profileImage != null) {
            Image(
                painter = profileImage,
                contentDescription = "작성자 장르이미지",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(colors.Grey)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = nickname,
                style = typography.smalltitle_sb600_s18_h24,
                color = colors.White,
                maxLines = 1
            )
            Text(
                text = badgeText,
                style = typography.feedcopy_r400_s14_h20,
                color = colors.NeonGreen,
                maxLines = 1
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, Grey02, RoundedCornerShape(20.dp))
                .background(Color.Transparent)
                .clickable { onSubscribeClick() }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.subscribe),
                style = typography.menu_m500_s14_h24,
                color = colors.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAuthorHeader() {
    ThipTheme {
        AuthorHeader(
            profileImage = null,
            nickname = "열자자제한열열자제한",
            badgeText = "칭호칭호칭호",
            onSubscribeClick = { println("구독") }
        )
    }
}