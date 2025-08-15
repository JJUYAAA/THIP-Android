package com.texthip.thip.data.repository

import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.feed.response.FeedWriteInfoResponse
import com.texthip.thip.data.service.FeedService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(
    private val feedService: FeedService
) {

    /** 피드 작성에 필요한 카테고리 및 태그 목록 조회 */
    suspend fun getFeedWriteInfo(): Result<FeedWriteInfoResponse?> = runCatching {
        val response = feedService.getFeedWriteInfo()
            .handleBaseResponse()
            .getOrThrow()
        
        // 카테고리 순서 조정
        val orderedCategories = response?.categoryList?.sortedBy { category ->
            when (category.category) {
                "문학" -> 0
                "과학·IT" -> 1
                "사회과학" -> 2
                "인문학" -> 3
                "예술" -> 4
                else -> 999
            }
        } ?: emptyList()
        
        response?.copy(categoryList = orderedCategories)
    }
}