package com.texthip.thip.data.service

import RoomsPlayingResponse
import com.texthip.thip.data.model.base.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RoomsService {
    @GET("rooms/{roomId}/playing")
    suspend fun getRoomsPlaying(
        @Path("roomId") roomId: Int
    ): BaseResponse<RoomsPlayingResponse>
}