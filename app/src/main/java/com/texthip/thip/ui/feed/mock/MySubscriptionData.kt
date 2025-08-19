package com.texthip.thip.ui.feed.mock

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.texthip.thip.data.model.users.response.UserItem
import com.texthip.thip.utils.color.hexToColor

data class MySubscriptionData(
    val profileImageUrl: String? = null,
    val nickname: String,
    val role: String,
    val roleColor: Color,
    val subscriberCount: Int = 0,
    var isSubscribed: Boolean = true
)

fun UserItem.toMySubscriptionData(): MySubscriptionData {
    return MySubscriptionData(
        profileImageUrl = this.profileImageUrl,
        nickname = this.nickname,
        role = this.aliasName,
        roleColor = hexToColor(this.aliasColor),
        subscriberCount = this.followerCount,
        isSubscribed = false // API 응답에 구독 여부 정보가 없음
    )
}
