package com.texthip.thip.ui.common.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun ActionBarButton(
    modifier: Modifier = Modifier,
    isLiked: Boolean,
    likeCount: Int,
    commentCount: Int,
    isSaveVisible: Boolean = false,
    isSaved: Boolean = false,
    isPinVisible: Boolean = false,
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    onPinClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.clickable { onLikeClick() },
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(if (isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Text(
                    text = likeCount.toString(),
                    style = typography.feedcopy_r400_s14_h20,
                    color = colors.White,
                )
            }

            Row(
                modifier = Modifier.clickable { onCommentClick() },
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_comment),
                    contentDescription = null,
                    tint = colors.White
                )
                Text(
                    text = commentCount.toString(),
                    style = typography.feedcopy_r400_s14_h20,
                    color = colors.White,
                )
            }

            if (isPinVisible) {
                Icon(
                    modifier = Modifier.clickable { onPinClick() },
                    painter = painterResource(R.drawable.ic_pin),
                    contentDescription = null,
                    tint = colors.White
                )
            }
        }

        if (isSaveVisible) {
            Icon(
                modifier = Modifier.clickable { onBookmarkClick() },
                painter = painterResource(if (isSaved) R.drawable.ic_save_filled else R.drawable.ic_save),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@Preview
@Composable
private fun ActionBarButtonPreview() {
    var isLiked by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }

    ActionBarButton(
        isLiked = isLiked,
        likeCount = 123,
        commentCount = 45,
        isSaveVisible = true,
        isSaved = isSaved,
        isPinVisible = true,
        onLikeClick = { isLiked = !isLiked },
        onCommentClick = {},
        onBookmarkClick = { isSaved = !isSaved },
        onPinClick = {}
    )
}