package com.texthip.thip.ui.navigator.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    // 메인 탭 Routes
    @Serializable
    data object Feed : Routes()
    
    @Serializable
    data object Group : Routes()
    
    @Serializable
    data object Search : Routes()
    
    @Serializable
    data object MyPage : Routes()
}
