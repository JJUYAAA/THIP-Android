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

val mockGroupNoteItems: List<GroupNoteItem> = listOf(
    GroupNoteRecord(
        page = 132,
        postDate = 12,
        userId = 1,
        nickName = "user.01",
        profileImageUrl = "https://example.com/profile.jpg",
        content = "내 생각에 이 부분이 가장 어려운 것 같다. 비유도 난해하고 잘 이해가 가지 않는데 다른 메이트들은 어떻게 읽었나요?",
        likeCount = 123,
        commentCount = 123,
        isLiked = true,
        isWriter = false,
        recordId = 1
    ),
    GroupNoteVote(
        page = 12,
        postDate = 12,
        userId = 1,
        nickName = "user.01",
        profileImageUrl = "https://example.com/profile.jpg",
        content = "3연에 나오는 심장은 무엇을 의미하는 걸까요?",
        likeCount = 123,
        commentCount = 123,
        isLiked = false,
        isWriter = false,
        voteId = 1,
        voteItems = listOf(
            VoteItem(1, "김땡땡", 90, false),
            VoteItem(2, "김땡땡", 10, false)
        )
    ),
    GroupNoteRecord(
        page = 132,
        postDate = 12,
        userId = 1,
        nickName = "user.01",
        profileImageUrl = "https://example.com/profile.jpg",
        content = "공백 포함 글자 입력입니다.",
        likeCount = 123,
        commentCount = 123,
        isLiked = false,
        isWriter = true,
        recordId = 1
    ),
    GroupNoteVote(
        page = 12,
        postDate = 12,
        userId = 1,
        nickName = "user.01",
        profileImageUrl = "https://example.com/profile.jpg",
        content = "3연에 나오는 심장은 무엇을 의미하는 걸까요?",
        likeCount = 123,
        commentCount = 123,
        isLiked = true,
        isWriter = true,
        voteId = 1,
        voteItems = listOf(
            VoteItem(1, "김땡땡", 90, false),
            VoteItem(2, "김땡땡", 10, false)
        )
    )
)