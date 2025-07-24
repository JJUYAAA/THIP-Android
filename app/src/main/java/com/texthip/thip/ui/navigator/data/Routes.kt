package com.texthip.thip.ui.navigator.data

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object Feed : Routes()
    
    @Serializable
    data object Group : Routes()
    
    @Serializable
    data object Search : Routes()
    
    @Serializable
    data object MyPage : Routes()
    
    // Group 관련 하위 화면들
    @Serializable
    data object GroupMakeRoom : Routes()
    
}
