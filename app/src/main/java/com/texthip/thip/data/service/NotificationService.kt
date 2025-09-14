package com.texthip.thip.data.service

import com.texthip.thip.data.model.notification.request.FcmTokenRequest
import com.texthip.thip.data.model.notification.response.NotificationEnableStateResponse
import com.texthip.thip.data.model.base.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NotificationService {
    @POST("notifications/fcm-tokens")
    suspend fun registerFcmToken(
        @Body request: FcmTokenRequest
    ): BaseResponse<Unit>
    
    @GET("users/notification-settings")
    suspend fun getNotificationEnableState(
        @Query("deviceId") deviceId: String
    ): BaseResponse<NotificationEnableStateResponse>
}