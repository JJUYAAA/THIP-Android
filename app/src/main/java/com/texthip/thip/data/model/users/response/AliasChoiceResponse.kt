package com.texthip.thip.data.model.users.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AliasChoiceResponse(
    @SerializedName("aliasChoices") val aliasChoices: List<AliasChoice>
)

@Serializable
data class AliasChoice(
    @SerializedName("aliasName") val aliasName: String,
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("aliasColor") val aliasColor: String
)