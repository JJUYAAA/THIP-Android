package com.texthip.thip.ui.group.note.mock

data class CommentItem(
    val commentId: Int,
    val userId: Int,
    val nickName: String,
    val genreName: String,
    val profileImageUrl: String,
    val content: String,
    val postDate: String,
    val isWriter: Boolean,
    val isLiked: Boolean,
    val likeCount: Int,
    val replyList: List<ReplyItem> = emptyList()
)

data class ReplyItem(
    val replyId: Int,
    val userId: Int,
    val nickName: String,
    val genreName: String,
    val profileImageUrl: String,
    val parentNickname: String,
    val content: String,
    val postDate: String,
    val isWriter: Boolean,
    val isLiked: Boolean,
    val likeCount: Int
)

data class CommentResponse(
    val commentList: List<CommentItem>
)

val mockComment = CommentItem(
    commentId = 1,
    userId = 1,
    nickName = "user.01",
    genreName = "칭호칭호",
    profileImageUrl = "https://example.com/profile.jpg",
    content = "입력하세요. 댓글 내용을 입력하세요오. 댓글 내용을 입력하세요. 댓글 내용을 입력하세요. 댓글 내용을 입력하세요. ",
    postDate = "2025.01.12",
    isWriter = false,
    isLiked = true,
    likeCount = 123,
    replyList = listOf(
        ReplyItem(
            replyId = 1,
            userId = 2,
            nickName = "user.02",
            genreName = "칭호칭호",
            profileImageUrl = "https://example.com/profile2.jpg",
            parentNickname = "user.01",
            content = "답글 내용입니다.",
            postDate = "12시간 전",
            isWriter = false,
            isLiked = false,
            likeCount = 123
        ),
        ReplyItem(
            replyId = 1,
            userId = 2,
            nickName = "user.02",
            genreName = "칭호칭호",
            profileImageUrl = "https://example.com/profile2.jpg",
            parentNickname = "user.01",
            content = "답글 내용입니다.",
            postDate = "2025.01.13",
            isWriter = false,
            isLiked = false,
            likeCount = 2
        )
    )
)

val mockCommentList = CommentResponse(
    commentList = listOf(
        mockComment, mockComment
    )
)
