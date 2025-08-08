package com.texthip.thip.data.mapper

import com.texthip.thip.data.model.group.response.JoinedRoomResponse
import com.texthip.thip.data.model.group.response.MyRoomResponse
import com.texthip.thip.data.model.group.response.RecommendRoomResponse
import com.texthip.thip.data.model.group.response.RoomMainResponse
import com.texthip.thip.data.model.group.response.RoomRecruitingResponse
import com.texthip.thip.ui.group.done.mock.MyRoomCardData
import com.texthip.thip.ui.group.myroom.mock.GroupBookData
import com.texthip.thip.ui.group.myroom.mock.GroupBottomButtonType
import com.texthip.thip.ui.group.myroom.mock.GroupCardData
import com.texthip.thip.ui.group.myroom.mock.GroupCardItemRoomData
import com.texthip.thip.ui.group.myroom.mock.GroupRoomData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupDataMapper @Inject constructor() {
    
    fun toGroupCardData(dto: JoinedRoomResponse, nickname: String): GroupCardData {
        return GroupCardData(
            id = dto.roomId,
            title = dto.bookTitle,
            members = dto.memberCount,
            imageUrl = dto.bookImageUrl,
            progress = dto.userPercentage,
            nickname = nickname
        )
    }
    
    fun toGroupCardItemRoomData(dto: RoomMainResponse, daysLeft: Int): GroupCardItemRoomData {
        return GroupCardItemRoomData(
            id = dto.roomId,
            title = dto.roomName,
            participants = dto.memberCount,
            maxParticipants = dto.recruitCount,
            isRecruiting = true,
            endDate = daysLeft,
            imageUrl = dto.bookImageUrl,
            isSecret = false
        )
    }
    
    fun toGroupRoomData(dto: RoomRecruitingResponse): GroupRoomData {
        val bookData = GroupBookData(
            title = dto.bookTitle,
            author = dto.authorName,
            publisher = dto.publisher,
            description = dto.bookDescription,
            imageUrl = dto.bookImageUrl
        )
        
        val recommendations = dto.recommendRooms.map { recommendDto ->
            toGroupCardItemRoomDataFromRecommend(recommendDto)
        }
        
        return GroupRoomData(
            id = dto.roomId,
            title = dto.roomName,
            isSecret = !dto.isPublic,
            description = dto.roomDescription,
            startDate = dto.progressStartDate,
            endDate = dto.progressEndDate,
            members = dto.memberCount,
            maxMembers = dto.recruitCount,
            daysLeft = extractDaysFromDeadline(dto.recruitEndDate),
            genre = dto.category,
            bookData = bookData,
            recommendations = recommendations,
            buttonType = determineButtonType(dto.isHost, dto.isJoining),
            roomImageUrl = dto.roomImageUrl,
            bookImageUrl = dto.bookImageUrl
        )
    }
    
    private fun toGroupCardItemRoomDataFromRecommend(dto: RecommendRoomResponse): GroupCardItemRoomData {
        return GroupCardItemRoomData(
            id = dto.roomId,
            title = dto.roomName,
            participants = dto.memberCount,
            maxParticipants = dto.recruitCount,
            isRecruiting = true,
            endDate = extractDaysFromDeadline(dto.recruitEndDate),
            imageUrl = dto.roomImageUrl
        )
    }
    
    fun extractDaysFromDeadline(deadlineDate: String): Int {
        return when {
            deadlineDate.contains("일 뒤") -> {
                deadlineDate.replace("일 뒤", "").trim().toIntOrNull() ?: 0
            }
            else -> 0
        }
    }
    
    fun toMyRoomCardData(room: MyRoomResponse): MyRoomCardData {
        return MyRoomCardData(
            roomId = room.roomId,
            bookImageUrl = room.bookImageUrl,
            roomName = room.roomName,
            recruitCount = room.recruitCount,
            memberCount = room.memberCount,
            endDate = room.endDate,
            type = room.type
        )
    }
    
    private fun determineButtonType(isHost: Boolean, isJoining: Boolean): GroupBottomButtonType {
        return when {
            isHost -> GroupBottomButtonType.CLOSE
            isJoining -> GroupBottomButtonType.CANCEL
            else -> GroupBottomButtonType.JOIN
        }
    }
}