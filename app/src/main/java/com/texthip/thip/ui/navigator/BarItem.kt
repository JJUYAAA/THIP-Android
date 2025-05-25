package com.texthip.thip.ui.navigator

import androidx.annotation.DrawableRes

data class BarItem(
    val title: String,
    val route: String,
    @DrawableRes val IconRes: Int,
    @DrawableRes val SelectedIconRes: Int
)
