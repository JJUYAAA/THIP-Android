package com.texthip.thip.ui.navigator.data

import com.texthip.thip.R
import com.texthip.thip.ui.navigator.routes.MainTabRoutes

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            titleRes = R.string.nav_feed,
            route = MainTabRoutes.Feed,
            iconRes = R.drawable.ic_feed,
            selectedIconRes = R.drawable.ic_feed_selected
        ),
        BarItem(
            titleRes = R.string.nav_group,
            route = MainTabRoutes.Group,
            iconRes = R.drawable.ic_group,
            selectedIconRes = R.drawable.ic_group_selected
        ),
        BarItem(
            titleRes = R.string.nav_search,
            route = MainTabRoutes.Search,
            iconRes = R.drawable.ic_booksearch,
            selectedIconRes = R.drawable.ic_booksearch_selected
        ),
        BarItem(
            titleRes = R.string.nav_mypage,
            route = MainTabRoutes.MyPage,
            iconRes = R.drawable.ic_mypage,
            selectedIconRes = R.drawable.ic_mypage_selected
        )
    )
}