package com.texthip.thip.ui.navigator.data

import androidx.annotation.DrawableRes
import com.texthip.thip.ui.navigator.routes.Routes

data class BarItem(
    val title: String,
    val route: Routes,
    @DrawableRes val iconRes: Int,
    @DrawableRes val selectedIconRes: Int
)
