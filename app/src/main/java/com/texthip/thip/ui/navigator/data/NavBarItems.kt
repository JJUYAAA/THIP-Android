package com.texthip.thip.ui.navigator.data

import com.texthip.thip.R
import com.texthip.thip.ui.navigator.routes.Routes

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "피드",
            route = Routes.Feed,
            iconRes = R.drawable.ic_feed,
            selectedIconRes = R.drawable.ic_feed_selected
        ),
        BarItem(
            title = "모임",
            route = Routes.Group,
            iconRes = R.drawable.ic_group,
            selectedIconRes = R.drawable.ic_group_selected
        ),
        BarItem(
            title = "검색",
            route = Routes.Search,
            iconRes = R.drawable.ic_booksearch,
            selectedIconRes = R.drawable.ic_booksearch_selected
        ),
        BarItem(
            title = "내 정보",
            route = Routes.MyPage,
            iconRes = R.drawable.ic_mypage,
            selectedIconRes = R.drawable.ic_mypage_selected
        )
    )
}