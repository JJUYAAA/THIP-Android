package com.texthip.thip.ui.navigator

import com.texthip.thip.R

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "피드",
            route = Routes.Feed.route,
            IconRes = R.drawable.ic_feed,
            SelectedIconRes = R.drawable.ic_feed_selected
        ),
        BarItem(
            title = "모임",
            route = Routes.Group.route,
            IconRes = R.drawable.ic_group,
            SelectedIconRes = R.drawable.ic_group_selected
        ),
        BarItem(
            title = "검색",
            route = Routes.BookSearch.route,
            IconRes = R.drawable.ic_booksearch,
            SelectedIconRes = R.drawable.ic_booksearch_selected
        ),
        BarItem(
            title = "내 정보",
            route = Routes.MyPage.route,
            IconRes = R.drawable.ic_mypage,
            SelectedIconRes = R.drawable.ic_mypage_selected
        )
    )
}