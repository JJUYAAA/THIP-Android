package com.texthip.thip.data.model.notification.response

import kotlinx.serialization.Serializable

@Serializable
data class NotificationEnableStateResponse(
    val isEnabled: Boolean
)