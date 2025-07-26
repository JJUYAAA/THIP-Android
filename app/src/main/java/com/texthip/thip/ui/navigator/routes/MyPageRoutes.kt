package com.texthip.thip.ui.navigator.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class MyPageRoutes : Routes() {
    // 향후 추가될 MyPage 관련 화면들
    // @Serializable data object Edit : MyPageRoutes
    // @Serializable data object Save : MyPageRoutes
    // @Serializable data object Reaction : MyPageRoutes
    // @Serializable data object NotificationEdit : MyPageRoutes
}