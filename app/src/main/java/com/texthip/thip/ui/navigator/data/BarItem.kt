package com.texthip.thip.ui.navigator.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.texthip.thip.ui.navigator.routes.Routes

data class BarItem(
    @StringRes val titleRes: Int,
    val route: Routes,
    @DrawableRes val iconRes: Int,
    @DrawableRes val selectedIconRes: Int
)
