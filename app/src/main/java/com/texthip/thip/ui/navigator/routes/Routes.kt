package com.texthip.thip.ui.navigator.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes

@Serializable
sealed class MainTabRoutes : Routes() {
    @Serializable
    data object Feed : MainTabRoutes()
    
    @Serializable
    data object Group : MainTabRoutes()
    
    @Serializable
    data object Search : MainTabRoutes()
    
    @Serializable
    data object MyPage : MainTabRoutes()
}
