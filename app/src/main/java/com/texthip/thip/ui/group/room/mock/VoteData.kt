package com.texthip.thip.ui.group.room.mock

import com.texthip.thip.ui.group.note.mock.VoteItem

data class VoteData(
    val description: String,
    val voteItems: List<VoteItem>
)

val mockVoteData = listOf(
    VoteData(
        description = "투표 내용입니다...",
        voteItems = listOf(
            VoteItem(1, "김땡땡", 50, false),
            VoteItem(2, "이땡땡", 10, false),
            VoteItem(3, "박땡땡", 20, false),
            VoteItem(4, "최땡땡", 15, false),
            VoteItem(5, "정땡땡", 5, false)
        )
    ),
    VoteData(
        description = "옆으로 넘긴 다른 투표 01",
        voteItems = listOf(
            VoteItem(1, "어쩌구", 25, false),
            VoteItem(2, "저쩌구", 45, false),
            VoteItem(3, "삼번", 20, false),
            VoteItem(4, "사번", 10, false)
        )
    ),
    VoteData(
        description = "옆으로 넘긴 다른 투표 02",
        voteItems = listOf(
            VoteItem(1, "투표 제목과 항목 버튼이 가로 스크롤되고", 40, false),
            VoteItem(2, "아래 캐러셀 닷은", 35, false),
            VoteItem(3, "위치 그대로, 강조점만 바뀌도록.", 25, false)
        )
    )
)