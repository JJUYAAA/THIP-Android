package com.texthip.thip.ui.navigator

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object Feed : Routes()
    
    @Serializable
    data object Group : Routes()
    
    @Serializable
    data object BookSearch : Routes()
    
    @Serializable
    data object MyPage : Routes()
    
    companion object {
        fun fromRoute(route: String?): Routes? {
            return when {
                route?.contains("Feed") == true -> Feed
                route?.contains("Group") == true -> Group
                route?.contains("BookSearch") == true -> BookSearch
                route?.contains("MyPage") == true -> MyPage
                else -> null
            }
        }
    }
}
