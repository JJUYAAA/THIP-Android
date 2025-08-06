package com.texthip.thip.data.repository

import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.service.RoomsService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomsRepository @Inject constructor(
    private val roomsService: RoomsService,
) {
    suspend fun getRoomsPlaying(
        roomId: Int,
        userId: Int
    ) = runCatching {
        roomsService.getRoomsPlaying(
            roomId = roomId,
            userId = userId
        ).handleBaseResponse().getOrThrow()
    }
}