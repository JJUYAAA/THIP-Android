package com.texthip.thip.ui.feed.usecase

import com.texthip.thip.data.repository.FeedRepository
import javax.inject.Inject

class ChangeFeedSaveUseCase @Inject constructor(
    private val feedRepository: FeedRepository
) {
    suspend operator fun invoke(feedId: Long, newSaveStatus: Boolean) =
        feedRepository.changeFeedLike(feedId, newSaveStatus)
}