package com.texthip.thip.ui.group.note.mock

sealed class GroupNoteItem {
//    abstract val postDate: String
    abstract val postDate: Int // TODO: String으로 바꾸기
    abstract val page: Int
    abstract val userId: Int
    abstract val nickName: String
    abstract val profileImageUrl: String
    abstract val content: String
    abstract val likeCount: Int
    abstract val commentCount: Int
    abstract val isLiked: Boolean
    abstract val isWriter: Boolean
}

data class GroupNoteRecord(
    override val postDate: Int,
    override val page: Int,
    override val userId: Int,
    override val nickName: String,
    override val profileImageUrl: String,
    override val content: String,
    override val likeCount: Int,
    override val commentCount: Int,
    override val isLiked: Boolean,
    override val isWriter: Boolean,
    val recordId: Int
) : GroupNoteItem()

data class GroupNoteVote(
    override val postDate: Int,
    override val page: Int,
    override val userId: Int,
    override val nickName: String,
    override val profileImageUrl: String,
    override val content: String,
    override val likeCount: Int,
    override val commentCount: Int,
    override val isLiked: Boolean,
    override val isWriter: Boolean,
    val voteId: Int,
    val voteItems: List<VoteItem>
) : GroupNoteItem()

data class VoteItem(
    val voteItemId: Int,
    val itemName: String,
    val percentage: Int,
    val isVoted: Boolean
)