package com.texthip.thip.ui.group.makeroom.mock

import java.time.LocalDate

data class GroupMakeRoomRequest(
    val selectedBook: BookData?,
    val genreIndex: Int,
    val roomTitle: String,
    val roomDescription: String,
    val meetingStartDate: LocalDate,
    val meetingEndDate: LocalDate,
    val memberLimit: Int,
    val isPrivate: Boolean,
    val password: String = ""
)
