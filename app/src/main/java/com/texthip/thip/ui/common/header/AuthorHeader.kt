package com.texthip.thip.ui.common.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.texthip.thip.ui.common.buttons.OutlinedButton
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun AuthorHeader(
    modifier: Modifier = Modifier,
    profileImage: String?,
    nickname: String,
    badgeText: String,
    buttonText: String = "",
    buttonWidth: Dp = 60.dp,
    showButton: Boolean = true,
    profileImageSize: Dp = 54.dp,
    onButtonClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (profileImage != null) {
            AsyncImage(
                model = profileImage,
                contentDescription = null,
                modifier = Modifier
                    .size(profileImageSize)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(profileImageSize)
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
        if (showButton) {
            OutlinedButton(
            modifier = Modifier
                .then(
                    if (buttonWidth != null)
                        Modifier
                            .width(buttonWidth)
                            .height(33.dp)
                    else Modifier
                ),
            text = buttonText,
            textStyle = typography.view_m500_s14,
            onClick = onButtonClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAuthorHeader() {
    ThipTheme {
        Column {
            AuthorHeader(
                profileImage = null,
                nickname = "열자자제한열열자제한",
                badgeText = "칭호칭호칭호",
                buttonText = "구독",
                modifier = Modifier.padding(bottom = 20.dp)
            )
            AuthorHeader(
                profileImage = null,
                nickname = "열자자제한열열자제한",
                badgeText = "칭호칭호칭호",
                buttonWidth = 60.dp,
                showButton = false
            )
        }
    }
}