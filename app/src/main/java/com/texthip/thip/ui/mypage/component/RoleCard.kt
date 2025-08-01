package com.texthip.thip.ui.mypage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.theme.DarkGrey
import com.texthip.thip.ui.theme.NeonGreen
import com.texthip.thip.ui.theme.Pink
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.ui.theme.White

@Composable
fun RoleCard(
    modifier: Modifier = Modifier,
    genre: String,
    role: String,
    imageResId: Int,
    genreColor: Color,
    roleColor: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (selected) White else DarkGrey
    val bgAlpha = if (selected) 1f else 0.3f
    val backgroundBrush = if (selected) {
        colors.Black800
    } else {
        colors.Black700
    }
    val actualGenreColor = if (selected) genreColor else colors.Grey01
    Box(
        modifier = modifier
            .width(162.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = backgroundBrush, shape = RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(80.dp),
            contentScale = ContentScale.Fit,
            alpha = bgAlpha
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = genre,
                style = typography.smalltitle_m500_s18_h24,
                color = actualGenreColor
            )
            Text(
                text = role,
                style = typography.info_r400_s12,
                color = roleColor
            )
        }
    }
}

@Preview
@Composable
fun RoleCardPreview() {
    var selected1 by rememberSaveable { mutableStateOf(true) }
    var selected2 by rememberSaveable { mutableStateOf(false) }

    ThipTheme {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RoleCard(
                genre = "문학",
                role = "문학가",
                imageResId = R.drawable.character_literature,
                genreColor = White,
                roleColor = NeonGreen,
                selected = selected1,
                onClick = { selected1 = !selected1 }
            )

            RoleCard(
                genre = "예술",
                role = "예술가",
                imageResId = R.drawable.character_art,
                genreColor = White,
                roleColor = Pink,
                selected = selected2,
                onClick = { selected2 = !selected2 }
            )
        }
    }
}
