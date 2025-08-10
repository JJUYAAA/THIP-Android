package com.texthip.thip.ui.navigator.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class GroupRoutes : Routes() {
    @Serializable
    data object MakeRoom : GroupRoutes()

    @Serializable
    data object Done : GroupRoutes()

    @Serializable
    data object Search : GroupRoutes()

    @Serializable
    data object My : GroupRoutes()

    @Serializable
    data class Recruit(val roomId: Int) : GroupRoutes()

    @Serializable
    data class Room(val roomId: Int) : GroupRoutes()

    @Serializable
    data class RoomMates(val roomId: Int) : GroupRoutes()

    @Serializable
    data class Note(val roomId: Int, val page: Int? = null, val isOverview: Boolean? = null) :
        GroupRoutes()

    @Serializable
    data class NoteCreate(val roomId: Int) : GroupRoutes()
}