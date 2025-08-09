package com.texthip.thip.utils.rooms


object RoomUtils {
    fun isRecruitingByType(type: String): Boolean {
        return when (type) {
            "recruiting" -> true
            "playingAndRecruiting" -> false
            "playing" -> false
            "expired" -> false
            else -> false
        }
    }
    
    fun getEndDateInDays(endDate: String): Int {
        return DateUtils.extractDaysFromDeadline(endDate)
    }
}