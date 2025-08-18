package com.texthip.thip.utils.type

import androidx.annotation.StringRes
import com.texthip.thip.R

enum class SortType (
    @StringRes val displayNameRes: Int,
    val apiKey: String
) {
    LATEST(R.string.sort_latest, "latest"),
    LIKE(R.string.sort_like, "like"),
    COMMENT(R.string.sort_comment, "comment");

    companion object {
        fun fromApiKey(key: String?): SortType {
            return entries.find { it.apiKey == key } ?: LATEST
        }
    }
}