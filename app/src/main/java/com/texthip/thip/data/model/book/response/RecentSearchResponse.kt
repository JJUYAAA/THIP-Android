package com.texthip.thip.data.model.book.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecentSearchResponse(
    @SerialName("recentSearchList") val recentSearchList: List<RecentSearchItem> = emptyList()
)

@Serializable
data class RecentSearchItem(
    @SerialName("recentSearchId") val recentSearchId: Int = 0,
    @SerialName("searchTerm") val searchTerm: String = ""
)