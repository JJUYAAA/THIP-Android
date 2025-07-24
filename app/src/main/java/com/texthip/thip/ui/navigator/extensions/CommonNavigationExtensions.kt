package com.texthip.thip.ui.navigator.extensions

import androidx.navigation.NavHostController
import com.texthip.thip.ui.navigator.routes.Routes


// 공통 네비게이션 확장 함수들

// 뒤로가기
fun NavHostController.navigateBack() {
    popBackStack()
}

// Bottom Navigation용 Tab 이동
fun NavHostController.navigateToTab(route: Routes) {
    navigate(route) {
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}