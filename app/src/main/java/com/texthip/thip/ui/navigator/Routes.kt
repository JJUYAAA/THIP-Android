package com.texthip.thip.ui.navigator


sealed class Routes(val route: String) {
    object Feed : Routes("Feed")
    object Group : Routes("Group")
    object BookSearch : Routes("BookSearch")
    object MyPage : Routes("MyPage")
}
