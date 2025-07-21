package com.texthip.thip.ui.feed.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun FeedSubscribeBarlist(
    modifier: Modifier = Modifier,
    followerProfileImageUrls: List<String>,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_group),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = colors.White
                    )
                ) {
                    append("${followerProfileImageUrls.size}명")
                }
                withStyle(
                    style = SpanStyle(
                        color = colors.Grey
                    )
                ) {
                    append("이 띱 하는중")
                }
            },
            style = typography.info_r400_s12,
            modifier = Modifier.padding(start = 2.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            followerProfileImageUrls.take(5).reversed().forEachIndexed { index, imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )

                val isLast = index == followerProfileImageUrls.take(5).lastIndex
                Spacer(modifier = Modifier.width(if (isLast) 15.dp else 4.dp))
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_chevron),
                contentDescription = null,
                tint = colors.Grey
            )
        }
    }
}

@Preview
@Composable
private fun FeedSubscribelistBarPreview() {
    ThipTheme {
        Column {
            // Case 1: 팔로워 0
            FeedSubscribeBarlist(
                followerProfileImageUrls = emptyList(),
                onClick = {}
            )

            // Case 2: 팔로워 3
            FeedSubscribeBarlist(
                followerProfileImageUrls = listOf(
                    "https://example.com/image1.jpg",
                    "https://example.com/image2.jpg",
                    "https://example.com/image3.jpg"
                ),
                onClick = {}
            )

            // Case 3: 팔로워 6
            FeedSubscribeBarlist(
                followerProfileImageUrls = List(6) {
                    "https://example.com/profile$it.jpg"
                },
                onClick = {}
            )
        }
    }

}