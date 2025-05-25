package com.texthip.thip.ui.navigator

/*
sealed class Routes (val route: String, val label: String, @DrawableRes val iconRes:Int, val isRoot: Boolean= true){
    object Feed : Routes("home", "피드", R.drawable.ic_feed)
    object Group : Routes("group", "모임", R.drawable.ic_group)
    object Search : Routes("search", "검색", R.drawable.ic_booksearch)
    object MyPage : Routes("mypage", "내 정보", R.drawable.ic_mypage)
}*/
sealed class Routes(val route: String) {
    object Feed : Routes("Feed")
    object Group : Routes("Group")
    object BookSearch : Routes("BookSearch")
    object MyPage : Routes("MyPage")
}
