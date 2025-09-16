package com.texthip.thip.ui.feed.usecase

import com.texthip.thip.data.repository.FeedRepository
import javax.inject.Inject

//피드 삭제 기능만 담당하는 usecase
class DeleteFeedUseCase @Inject constructor(
    private val feedRepository: FeedRepository
) {
    suspend operator fun invoke(feedId: Long): Result<String?> {
        return feedRepository.deleteFeed(feedId)
    }
}