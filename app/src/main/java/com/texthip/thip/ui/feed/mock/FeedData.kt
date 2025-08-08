package com.texthip.thip.ui.feed.mock

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.texthip.thip.ui.group.makeroom.mock.BookData

data class FeedData(
    val selectedBook: BookData? = null,
    val feedContent: String = "",
    val isPrivate: Boolean = false,
    val selectedGenreIndex: Int = -1,
    val selectedSubGenres: List<String> = emptyList(),
    val imageUris: SnapshotStateList<Uri> = mutableStateListOf()
) {
    val isReadyToSubmit: Boolean
        get() = selectedBook != null &&
                feedContent.isNotBlank() &&
                selectedGenreIndex != -1 &&
                selectedSubGenres.isNotEmpty()
}
