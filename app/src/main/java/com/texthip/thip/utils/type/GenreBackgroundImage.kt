package com.texthip.thip.utils.type

import androidx.annotation.DrawableRes
import com.texthip.thip.R

enum class GenreBackgroundImage(
    val genreName: String,
    @DrawableRes val imageResId: Int
) {
    LITERATURE("문학", R.drawable.img_literature),
    HUMANITIES("인문학", R.drawable.img_humanities),
    SCIENCE("과학•IT", R.drawable.img_science_it),
    SOCIAL_SCIENCE("사회과학", R.drawable.img_social_science),
    ART("예술", R.drawable.img_art);

    companion object {
        fun fromServerValue(serverValue: String?): GenreBackgroundImage {
            val genreNameFromServer = serverValue?.substringBefore("_") ?: return GenreBackgroundImage.LITERATURE
            return entries.find { it.genreName == genreNameFromServer } ?: GenreBackgroundImage.LITERATURE
        }
    }
}