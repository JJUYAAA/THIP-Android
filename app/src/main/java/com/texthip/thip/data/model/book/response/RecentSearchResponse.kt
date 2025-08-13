package com.texthip.thip.data.model.book.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecentSearchResponse(
    @SerialName("recentSearchList") val recentSearchList: List<RecentSearchItem>
)

@Serializable
data class RecentSearchItem(
    @SerialName("recentSearchId") val recentSearchId: Int,
    @SerialName("searchTerm") val searchTerm: String
)