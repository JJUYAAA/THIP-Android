package com.texthip.thip.data.model.rooms.request

import kotlinx.serialization.Serializable

@Serializable
data class RoomsPostsRequestParams(
    val type: String,
    val sort: String? = null,
    val pageStart: Int? = null,
    val pageEnd: Int? = null,
    val isOverview: Boolean? = null,
    val isPageFilter: Boolean? = null,
    val cursor: String? = null
)
