package com.texthip.thip.ui.group.makeroom.viewmodel

import com.texthip.thip.data.manager.Genre
import com.texthip.thip.ui.group.makeroom.mock.BookData
import com.texthip.thip.ui.group.makeroom.mock.GroupMakeRoomRequest
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class GroupMakeRoomUiState(
    val selectedBook: BookData? = null,
    val showBookSearchSheet: Boolean = false,
    val selectedGenreIndex: Int = -1,
    val roomTitle: String = "",
    val roomDescription: String = "",
    val meetingStartDate: LocalDate = LocalDate.now(),
    val meetingEndDate: LocalDate = LocalDate.now().plusDays(1),
    val memberLimit: Int = 30,
    val isPrivate: Boolean = false,
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val savedBooks: List<BookData> = emptyList(),
    val groupBooks: List<BookData> = emptyList(),
    val isLoadingBooks: Boolean = false,
    val isLoadingMoreSavedBooks: Boolean = false,
    val isLoadingMoreGroupBooks: Boolean = false,
    val isLastSavedBooks: Boolean = false,
    val isLastGroupBooks: Boolean = false,
    val searchResults: List<BookData> = emptyList(),
    val isSearching: Boolean = false,
    val isLoadingMoreSearchResults: Boolean = false,
    val searchPage: Int = 1,
    val isLastSearchPage: Boolean = false,
    val currentSearchQuery: String = "",
    val genres: List<Genre> = emptyList(),
    val isBookPreselected: Boolean = false
) {
    // 유효성 검사 로직
    val isDurationValid: Boolean
        get() {
            val daysBetween = ChronoUnit.DAYS.between(meetingStartDate, meetingEndDate)
            return daysBetween in 1..90
        }

    val isCountValid: Boolean
        get() = memberLimit in 2..30

    val isPasswordValid: Boolean
        get() = !isPrivate || password.length == 4

    val isFormValid: Boolean
        get() = selectedBook != null &&
                selectedGenreIndex >= 0 &&
                roomTitle.isNotBlank() &&
                roomDescription.isNotBlank() &&
                isDurationValid &&
                isCountValid &&
                isPasswordValid

    // 서버 전송용 데이터로 변환
    fun toRequest(): GroupMakeRoomRequest {
        return GroupMakeRoomRequest(
            selectedBook = selectedBook,
            genreIndex = selectedGenreIndex,
            roomTitle = roomTitle.trim(),
            roomDescription = roomDescription.trim(),
            meetingStartDate = meetingStartDate,
            meetingEndDate = meetingEndDate,
            memberLimit = memberLimit,
            isPrivate = isPrivate,
            password = if (isPrivate) password else ""
        )
    }
}