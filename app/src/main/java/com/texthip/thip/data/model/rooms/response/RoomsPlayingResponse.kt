import kotlinx.serialization.Serializable

@Serializable
data class RoomsPlayingResponse(
    val isHost: Boolean,
    val roomId: Int,
    val roomName: String,
    val roomImageUrl: String,
    val isPublic: Boolean,
    val progressStartDate: String,
    val progressEndDate: String,
    val category: String,
    val roomDescription: String,
    val memberCount: Int,
    val recruitCount: Int,
    val isbn: String,
    val bookTitle: String,
    val authorName: String,
    val currentPage: Int,
    val userPercentage: Double,
    val currentVotes: List<CurrentVote>
)

@Serializable
data class CurrentVote(
    val content: String,
    val page: Int,
    val isOverview: Boolean,
    val voteItems: List<VoteItem>
)

@Serializable
data class VoteItem(
    val itemName: String
)