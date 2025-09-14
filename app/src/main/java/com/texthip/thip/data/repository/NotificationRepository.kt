package com.texthip.thip.data.repository

import android.content.Context
import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.notification.request.FcmTokenRequest
import com.texthip.thip.data.model.notification.response.NotificationEnableStateResponse
import com.texthip.thip.data.service.NotificationService
import com.texthip.thip.utils.auth.getAndroidDeviceId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val notificationService: NotificationService,
    private val context: Context
) {
    suspend fun registerFcmToken(
        deviceId: String,
        fcmToken: String
    ): Result<Unit> {
        return runCatching {
            val request = FcmTokenRequest(
                deviceId = deviceId,
                fcmToken = fcmToken,
                platformType = "ANDROID"
            )
            val response = notificationService.registerFcmToken(request)
            response.handleBaseResponse().getOrThrow()
        }
    }
    
    suspend fun getNotificationEnableState(): Result<NotificationEnableStateResponse?> {
        return runCatching {
            val deviceId = context.getAndroidDeviceId()
            val response = notificationService.getNotificationEnableState(deviceId)
            response.handleBaseResponse().getOrNull()
        }
    }
}