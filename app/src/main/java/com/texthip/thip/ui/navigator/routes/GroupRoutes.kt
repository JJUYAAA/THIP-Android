package com.texthip.thip.ui.navigator.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class GroupRoutes : Routes() {
    @Serializable
    data object MakeRoom : GroupRoutes()
    
    // 향후 추가될 Group 관련 화면들
    // @Serializable data object Room : GroupRoutes
    // @Serializable data object RoomChat : GroupRoutes
    // @Serializable data object Note : GroupRoutes
    // @Serializable data object NoteCreate : GroupRoutes
}