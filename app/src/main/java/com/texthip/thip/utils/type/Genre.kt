package com.texthip.thip.utils.type

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.texthip.thip.R
import com.texthip.thip.ui.theme.ThipTheme.colors

enum class Genre(
    val genreName: String,
    @DrawableRes val imageResId: Int,
    val primaryColor: @Composable () -> Color,
    val secondaryColor: @Composable () -> Color
) {
    LITERATURE("문학", R.drawable.img_literature, { colors.Literature }, { colors.LiteratureSub }),
    HUMANITIES("인문학", R.drawable.img_humanities, { colors.Humanities }, { colors.HumanitiesSub }),
    SCIENCE("과학•IT", R.drawable.img_science_it, { colors.ScienceIt }, { colors.ScienceItSub }),
    SOCIAL_SCIENCE("사회과학", R.drawable.img_social_science, { colors.SocialScience }, { colors.SocialScienceSub }),
    ART("예술", R.drawable.img_art, { colors.Art }, { colors.ArtSub });

    companion object {
        fun fromServerValue(serverValue: String?): Genre {
            val genreNameFromServer = serverValue?.substringBefore("_") ?: return LITERATURE
            return entries.find { it.genreName == genreNameFromServer } ?: LITERATURE
        }
    }
}