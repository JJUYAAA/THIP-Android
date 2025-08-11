package com.texthip.thip.data.model.book.response

import kotlinx.serialization.Serializable

@Serializable
data class RecentSearchResponse(
    val recentSearchList: List<RecentSearchItem>
)

@Serializable
data class RecentSearchItem(
    val recentSearchId: Int,
    val searchTerm: String
)