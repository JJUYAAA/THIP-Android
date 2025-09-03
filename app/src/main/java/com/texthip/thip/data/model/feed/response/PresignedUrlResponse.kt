package com.texthip.thip.data.model.feed.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresignedUrlInfo(
    @SerialName("presignedUrl")
    val presignedUrl: String,
    @SerialName("fileUrl")
    val fileUrl: String
)

@Serializable
data class PresignedUrlResponse(
    @SerialName("presignedUrls")
    val presignedUrls: List<PresignedUrlInfo>
)