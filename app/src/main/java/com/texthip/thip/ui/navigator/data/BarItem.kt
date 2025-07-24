package com.texthip.thip.ui.navigator.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.texthip.thip.ui.navigator.routes.MainTabRoutes

data class BarItem(
    @StringRes val titleRes: Int,
    val route: MainTabRoutes,
    @DrawableRes val iconRes: Int,
    @DrawableRes val selectedIconRes: Int
)
