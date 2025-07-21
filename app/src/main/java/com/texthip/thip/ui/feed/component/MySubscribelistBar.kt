package com.texthip.thip.ui.feed.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.texthip.thip.R
import com.texthip.thip.ui.feed.mock.MySubscriptionData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun getDisplayNameFor(index: Int): String {
    val nicknames = listOf("aaaaaaaa", "bbbbbbb", "aaaaaaaaaaaa", "bbbbbbb", "aaaaa", "bbb","aaaaa")
    return nicknames.getOrNull(index) ?: ""
}

@Composable
fun MySubscribeBarlist(
    modifier: Modifier = Modifier,
    subscriptions: List<MySubscriptionData>,
    onClick: () -> Unit
) {
// 이미지 + 간격 너비
    val imageWidthWithSpacing = 36.dp + 12.dp
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(82.dp)
            .clickable { onClick() }
    ) {
        val maxWidthPx = with(density) { maxWidth.toPx() }
        val imageWithSpacingPx = with(density) { imageWidthWithSpacing.toPx() }

        val maxVisibleCount = ((maxWidthPx - 36f) / imageWithSpacingPx).toInt()

        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_group),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Text(
                    text = stringResource(R.string.my_subscription),
                    style = typography.smalltitle_sb600_s14_h20,
                    color = Color.White,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .clickable { onClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                subscriptions.take(maxVisibleCount).forEach { profile ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(36.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = profile.profileImageUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        )
                        Text(
                            text = profile.nickname,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = typography.view_r400_s11_h20,
                            color = Color.White,
                            modifier = Modifier.width(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron),
                    contentDescription = null,
                    tint = colors.Grey
                )
            }
        }
    }
}

@Preview
@Composable
private fun MySubscribeBarlistPrev() {
    ThipTheme {
        val previewData = List(10) {
            MySubscriptionData(
                profileImageUrl = "https://example.com/profile$it.jpg",
                nickname = "닉네임$it",
                role = "문학가",
                roleColor = Color.Red,
                subscriberCount = 100 + it
            )
        }

        Column {
            MySubscribeBarlist(
                subscriptions = previewData,
                onClick = {}
            )
        }
    }

}