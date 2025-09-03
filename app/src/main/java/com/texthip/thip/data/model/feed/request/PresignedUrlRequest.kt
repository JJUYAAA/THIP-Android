package com.texthip.thip.data.model.feed.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageMetadata(
    @SerialName("extension")
    val extension: String,
    @SerialName("size")
    val size: Long
)

typealias PresignedUrlRequest = List<ImageMetadata>