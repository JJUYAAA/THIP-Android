package com.texthip.thip.ui.navigator.extensions

import androidx.navigation.NavHostController
import com.texthip.thip.ui.navigator.routes.MainTabRoutes
import com.texthip.thip.ui.navigator.routes.GroupRoutes


// Group 관련 네비게이션 확장 함수들

fun NavHostController.navigateToGroup() {
    navigate(MainTabRoutes.Group)
}

fun NavHostController.navigateToGroupMakeRoom() {
    navigate(GroupRoutes.MakeRoom)
}