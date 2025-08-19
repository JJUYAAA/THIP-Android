package com.texthip.thip.ui.feed.viewmodel

import android.net.Uri
import com.texthip.thip.data.model.feed.response.FeedCategory
import com.texthip.thip.ui.group.makeroom.mock.BookData

data class FeedWriteUiState(
    val selectedBook: BookData? = null,
    val showBookSearchSheet: Boolean = false,
    val feedContent: String = "",
    val imageUris: List<Uri> = emptyList(),
    val existingImageUrls: List<String> = emptyList(), // 기존 피드 이미지 URL들
    val isPrivate: Boolean = false,
    val selectedCategoryIndex: Int = -1,
    val selectedTags: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val savedBooks: List<BookData> = emptyList(),
    val groupBooks: List<BookData> = emptyList(),
    val isLoadingBooks: Boolean = false,
    val searchResults: List<BookData> = emptyList(),
    val isSearching: Boolean = false,
    val categories: List<FeedCategory> = emptyList(),
    val isBookPreselected: Boolean = false,
    val isLoadingCategories: Boolean = false,
    val isEditMode: Boolean = false,
    val editingFeedId: Int? = null
) {
    // 유효성 검사 로직
    val isContentValid: Boolean
        get() = feedContent.isNotBlank() && feedContent.length <= 2000

    // 현재 모드에 따른 이미지 개수
    val currentImageCount: Int
        get() = if (isEditMode) existingImageUrls.size else imageUris.size

    val isImageCountValid: Boolean
        get() = currentImageCount <= 3

    val isFormValid: Boolean
        get() = selectedBook != null &&
                isContentValid &&
                isImageCountValid &&
                selectedTags.size <= 5  // 태그는 최대 5개까지만

    // 태그 개수 제한 (최대 5개)
    val canAddMoreTags: Boolean
        get() = selectedTags.size < 5

    // 이미지 추가 가능 여부
    val canAddMoreImages: Boolean
        get() = !isEditMode && imageUris.size < 3

    // 현재 선택된 카테고리의 태그 목록
    val availableTags: List<String>
        get() = if (selectedCategoryIndex >= 0 && selectedCategoryIndex < categories.size) {
            categories[selectedCategoryIndex].tagList
        } else {
            categories.flatMap { it.tagList }.distinct()
        }

    // 현재 선택된 카테고리 이름
    val selectedCategoryName: String?
        get() = if (selectedCategoryIndex >= 0 && selectedCategoryIndex < categories.size) {
            categories[selectedCategoryIndex].category
        } else {
            null
        }
}