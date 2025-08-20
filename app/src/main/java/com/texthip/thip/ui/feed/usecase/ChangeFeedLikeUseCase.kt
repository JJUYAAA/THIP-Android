package com.texthip.thip.ui.feed.usecase

import com.texthip.thip.data.repository.FeedRepository
import javax.inject.Inject

class ChangeFeedLikeUseCase @Inject constructor(
    private val feedRepository: FeedRepository
) {
    suspend operator fun invoke(
        feedId: Long, newLikeStatus: Boolean, currentLikeCount: Int,
        currentIsSaved: Boolean
    ) =
        feedRepository.changeFeedLike(
            feedId, newLikeStatus,
            currentLikeCount,
            currentIsSaved
        )
}