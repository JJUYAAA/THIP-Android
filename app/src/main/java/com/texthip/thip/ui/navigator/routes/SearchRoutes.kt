package com.texthip.thip.ui.navigator.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class SearchRoutes : Routes() {
    @Serializable
    data class BookDetail(val isbn: String) : SearchRoutes()
    
    @Serializable
    data class BookGroup(val isbn: String) : SearchRoutes()
}