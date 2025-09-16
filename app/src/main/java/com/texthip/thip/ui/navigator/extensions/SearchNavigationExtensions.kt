package com.texthip.thip.ui.navigator.extensions

import androidx.navigation.NavHostController
import com.texthip.thip.ui.navigator.routes.MainTabRoutes
import com.texthip.thip.ui.navigator.routes.SearchRoutes


// Search 관련 네비게이션 확장 함수들
fun NavHostController.navigateToSearch() {
    navigate(MainTabRoutes.Search)
}

fun NavHostController.navigateToBookDetail(isbn: String) {
    navigate(SearchRoutes.BookDetail(isbn = isbn))
}

fun NavHostController.navigateToBookGroup(isbn: String) {
    navigate(SearchRoutes.BookGroup(isbn = isbn))
}