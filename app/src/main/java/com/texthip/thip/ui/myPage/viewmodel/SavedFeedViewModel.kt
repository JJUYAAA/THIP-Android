package com.texthip.thip.ui.myPage.viewmodel

import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import com.texthip.thip.R
import com.texthip.thip.ui.myPage.data.FeedItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SavedFeedViewModel: ViewModel() {
    private val _feeds = MutableStateFlow(
        listOf(
            FeedItem(
                id = 1,
                user_profile_image = R.drawable.character_art,
                user_name = "user",
                user_role = "학생",
                book_title = "라랄ㄹ라라",
                auth_name = "야야야",
                time_ago = 15,
                content = "진짜최공진짜최공진차최공",
                like_count = 25,
                comment_count = 4,
                is_liked = false,
                is_saved = true,
                imageUrl = R.drawable.bookcover_sample
            ),
            FeedItem(
                id = 2,
                user_profile_image = R.drawable.character_art,
                user_name = "user",
                user_role = "학생",
                book_title = "라랄ㄹ라라",
                auth_name = "야야야",
                time_ago = 15,
                content = "진짜최공진짜최공진차최공",
                like_count = 25,
                comment_count = 4,
                is_liked = false,
                is_saved = true,
                imageUrl = R.drawable.bookcover_sample
            ),
            FeedItem(
                id = 3,
                user_profile_image = R.drawable.character_art,
                user_name = "user",
                user_role = "학생",
                book_title = "라랄ㄹ라라",
                auth_name = "야야야",
                time_ago = 15,
                content = "진짜최공진짜최공진차최공",
                like_count = 25,
                comment_count = 4,
                is_liked = false,
                is_saved = true,
                imageUrl = R.drawable.bookcover_sample
            ),
            FeedItem(
                id = 4,
                user_profile_image = R.drawable.character_literature,
                user_name = "user",
                user_role = "학생",
                book_title = "라랄ㄹ라라",
                auth_name = "야야야",
                time_ago = 15,
                content = "진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공진짜최공진짜최공진차최공",
                like_count = 25,
                comment_count = 4,
                is_liked = false,
                is_saved = true
            ),
            FeedItem(
                id = 5,
                user_profile_image = R.drawable.character_sociology,
                user_name = "user",
                user_role = "학생",
                book_title = "라랄ㄹ라라",
                auth_name = "야야야",
                time_ago = 15,
                content = "진짜최공진짜최공진차최공",
                like_count = 25,
                comment_count = 4,
                is_liked = false,
                is_saved = true,
                imageUrl = R.drawable.bookcover_sample
            ),


        )
    )
    val feeds: StateFlow<List<FeedItem>> = _feeds

    fun toggleBookmark(id: Int) {
        _feeds.value = _feeds.value.map {
            if (it.id == id) it.copy(is_saved = !it.is_saved) else it
        }
    }

    fun toggleLike(id: Int) {
        _feeds.value = _feeds.value.map {
            if (it.id == id) it.copy(is_liked = !it.is_liked) else it
        }
    }
}