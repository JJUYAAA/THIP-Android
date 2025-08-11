package com.texthip.thip.utils.rooms


object DateUtils {
    fun extractDaysFromDeadline(dateString: String): Int {
        return when {
            dateString.contains("일 뒤") -> {
                dateString.replace("일 뒤", "").trim().toIntOrNull() ?: 0
            }
            else -> 0
        }
    }
}