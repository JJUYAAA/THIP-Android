package com.texthip.thip.ui.group.done.mock

data class MyRoomCardData(
    val roomId: Int,
    val bookImageUrl: String?, // API에서 받은 이미지 URL
    val roomName: String,
    val recruitCount: Int,
    val memberCount: Int,
    val endDate: String,
    val type: String // "playingAndRecruiting", "recruiting", "playing", "expired"
)

data class MyRoomsPaginationResult(
    val data: List<MyRoomCardData>,
    val nextCursor: String?,
    val isLast: Boolean
)

// MyRoomCardData를 위한 타입 기반 모집 상태 확인 함수
fun MyRoomCardData.isRecruitingByType(): Boolean {
    return when (type) {
        "recruiting" -> true
        "playingAndRecruiting" -> false
        "playing" -> false
        "expired" -> false
        else -> false // 타입 정보가 없으면 기본값
    }
}

// endDate 문자열을 일수로 변환하는 함수 (예: "25일 뒤" -> 25)
fun MyRoomCardData.getEndDateInDays(): Int {
    return when {
        endDate.contains("일 뒤") -> {
            endDate.replace("일 뒤", "").trim().toIntOrNull() ?: 0
        }
        else -> 0 // 파싱할 수 없는 경우 0 반환
    }
}