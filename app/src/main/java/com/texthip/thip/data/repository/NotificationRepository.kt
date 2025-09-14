package com.texthip.thip.data.repository

import android.content.Context
import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.notification.request.FcmTokenRequest
import com.texthip.thip.data.model.notification.request.NotificationEnabledRequest
import com.texthip.thip.data.model.notification.response.NotificationEnabledResponse
import com.texthip.thip.data.service.NotificationService
import com.texthip.thip.utils.auth.getAndroidDeviceId
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val notificationService: NotificationService,
    @param:ApplicationContext private val context: Context
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
    
    suspend fun getNotificationEnableState(): Result<NotificationEnabledResponse?> {
        return runCatching {
            val deviceId = context.getAndroidDeviceId()
            val response = notificationService.getNotificationEnableState(deviceId)
            response.handleBaseResponse().getOrNull()
        }
    }
    
    suspend fun updateNotificationEnabled(enabled: Boolean): Result<NotificationEnabledResponse?> {
        return runCatching {
            val deviceId = context.getAndroidDeviceId()
            val request = NotificationEnabledRequest(
                enable = enabled,
                deviceId = deviceId
            )
            val response = notificationService.updateNotificationEnabled(request)
            response.handleBaseResponse().getOrNull()
        }
    }
}