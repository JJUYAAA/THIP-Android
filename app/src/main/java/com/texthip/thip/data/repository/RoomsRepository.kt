package com.texthip.thip.data.repository

import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.rooms.request.RoomsCreateVoteRequest
import com.texthip.thip.data.model.rooms.request.RoomsRecordRequest
import com.texthip.thip.data.model.rooms.request.VoteItem
import com.texthip.thip.data.service.RoomsService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomsRepository @Inject constructor(
    private val roomsService: RoomsService,
) {
    suspend fun getRoomsPlaying(
        roomId: Int
    ) = runCatching {
        roomsService.getRoomsPlaying(
            roomId = roomId
        ).handleBaseResponse().getOrThrow()
    }

    suspend fun getRoomsUsers(
        roomId: Int
    ) = runCatching {
        roomsService.getRoomsUsers(
            roomId = roomId
        ).handleBaseResponse().getOrThrow()
    }

    suspend fun getRoomsPosts(
        roomId: Int,
        type: String = "group",
        sort: String? = "latest",
        pageStart: Int? = null,
        pageEnd: Int? = null,
        isOverview: Boolean? = false,
        isPageFilter: Boolean? = false,
        cursor: String? = null,
    ) = runCatching {
        roomsService.getRoomsPosts(
            roomId = roomId,
            type = type,
            sort = sort,
            pageStart = pageStart,
            pageEnd = pageEnd,
            isOverview = isOverview,
            isPageFilter = isPageFilter,
            cursor = cursor
        ).handleBaseResponse().getOrThrow()
    }

    suspend fun postRoomsRecord(
        roomId: Int,
        content: String,
        isOverview: Boolean = false,
        page: Int = 0
    ) = runCatching {
        roomsService.postRoomsRecord(
            roomId = roomId,
            request = RoomsRecordRequest(
                page = page,
                isOverview = isOverview,
                content = content
            )
        ).handleBaseResponse().getOrThrow()
    }

    suspend fun postRoomsCreateVote(
        roomId: Int,
        page: Int,
        isOverview: Boolean,
        content: String,
        voteItemList: List<VoteItem>
    ) = runCatching {
        roomsService.postRoomsCreateVote(
            roomId = roomId,
            request = RoomsCreateVoteRequest(
                page = page,
                isOverview = isOverview,
                content = content,
                voteItemList = voteItemList
            )
        ).handleBaseResponse().getOrThrow()
    }

    suspend fun getRoomsBookPage(
        roomId: Int,
    ) = runCatching {
        roomsService.getRoomsBookPage(
            roomId = roomId
        ).handleBaseResponse().getOrThrow()
    }
}