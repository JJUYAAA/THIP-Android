package com.texthip.thip.ui.navigator.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class MyPageRoutes : Routes() {
    @Serializable data object Edit : MyPageRoutes()
    @Serializable data object Save : MyPageRoutes()
    @Serializable data object Reaction : MyPageRoutes()
    @Serializable data object NotificationEdit : MyPageRoutes()
    @Serializable data object LeaveThip : MyPageRoutes()
}