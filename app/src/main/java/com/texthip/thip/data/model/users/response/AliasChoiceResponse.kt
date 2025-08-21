package com.texthip.thip.data.model.users.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AliasChoiceResponse(
    @SerialName("aliasChoices") val aliasChoices: List<AliasChoice>
)

@Serializable
data class AliasChoice(
    @SerialName("aliasName") val aliasName: String,
    @SerialName("categoryName") val categoryName: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("aliasColor") val aliasColor: String
)