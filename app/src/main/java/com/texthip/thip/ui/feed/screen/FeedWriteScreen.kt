package com.texthip.thip.ui.feed.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.texthip.thip.R
import com.texthip.thip.data.model.feed.response.FeedCategory
import com.texthip.thip.ui.common.buttons.GenreChipButton
import com.texthip.thip.ui.common.buttons.GenreChipRow
import com.texthip.thip.ui.common.buttons.SubGenreChipGrid
import com.texthip.thip.ui.common.buttons.ToggleSwitchButton
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.feed.viewmodel.FeedWriteUiState
import com.texthip.thip.ui.feed.viewmodel.FeedWriteViewModel
import com.texthip.thip.ui.group.makeroom.component.GroupBookSearchBottomSheet
import com.texthip.thip.ui.group.makeroom.component.GroupInputField
import com.texthip.thip.ui.group.makeroom.component.GroupSelectBook
import com.texthip.thip.ui.group.makeroom.component.SectionDivider
import com.texthip.thip.ui.group.makeroom.mock.BookData
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun FeedWriteScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onFeedCreated: (Int) -> Unit = {},
    viewModel: FeedWriteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    FeedWriteContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onCreateFeed = {
            viewModel.createOrUpdateFeed(
                onSuccess = { feedId ->
                    onFeedCreated(feedId)
                },
                onError = { errorMessage ->
                }
            )
        },
        onSelectBook = viewModel::selectBook,
        onToggleBookSearchSheet = viewModel::toggleBookSearchSheet,
        onUpdateFeedContent = viewModel::updateFeedContent,
        onAddImages = viewModel::addImages,
        onRemoveImage = viewModel::removeImage,
        onRemoveExistingImage = viewModel::removeExistingImage,
        onTogglePrivate = viewModel::togglePrivate,
        onSelectCategory = viewModel::selectCategory,
        onToggleTag = viewModel::toggleTag,
        onRemoveTag = viewModel::removeTag,
        onSearchBooks = viewModel::searchBooks,
        modifier = modifier
    )
}

@Composable
fun FeedWriteContent(
    modifier: Modifier = Modifier,
    uiState: FeedWriteUiState,
    onNavigateBack: () -> Unit = {},
    onCreateFeed: () -> Unit = {},
    onSelectBook: (BookData) -> Unit = {},
    onToggleBookSearchSheet: (Boolean) -> Unit = {},
    onUpdateFeedContent: (String) -> Unit = {},
    onAddImages: (List<Uri>) -> Unit = {},
    onRemoveImage: (Int) -> Unit = {},
    onRemoveExistingImage: (Int) -> Unit = {},
    onTogglePrivate: (Boolean) -> Unit = {},
    onSelectCategory: (Int) -> Unit = {},
    onToggleTag: (String) -> Unit = {},
    onRemoveTag: (String) -> Unit = {},
    onSearchBooks: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            onAddImages(uris)
        }
    }
    val focusManager = LocalFocusManager.current

    Box {
        Column(
            modifier = modifier
                .fillMaxSize()
                .then(if (uiState.showBookSearchSheet) Modifier.blur(5.dp) else Modifier),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputTopAppBar(
                title = stringResource(
                    if (uiState.isEditMode) R.string.edit_feed_title
                    else R.string.new_feed
                ),
                rightButtonName = stringResource(
                    R.string.registration
                ),
                isRightButtonEnabled = uiState.isFormValid && !uiState.isLoading,
                onLeftClick = onNavigateBack,
                onRightClick = onCreateFeed
            )
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            focusManager.clearFocus()
                        })
                    },
                verticalArrangement = Arrangement.Top,
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                GroupSelectBook(
                    selectedBook = uiState.selectedBook,
                    onChangeBookClick = { onToggleBookSearchSheet(true) },
                    onSelectBookClick = { onToggleBookSearchSheet(true) },
                    isBookPreselected = uiState.isBookPreselected
                )

                SectionDivider()

                GroupInputField(
                    title = stringResource(R.string.write_feed),
                    hint = stringResource(R.string.write_feed_hint),
                    value = uiState.feedContent,
                    maxLength = 2000,
                    onValueChange = onUpdateFeedContent
                )

                SectionDivider()

                Text(
                    text = stringResource(R.string.add_photo),
                    style = typography.smalltitle_sb600_s18_h24,
                    color = colors.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 이미지 추가 버튼 (수정 모드에서는 비활성화)
                    item {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(color = colors.DarkGrey02)
                                .border(
                                    width = 1.dp,
                                    color = if (!uiState.canAddMoreImages) colors.DarkGrey else colors.Grey02,
                                )
                                .let {
                                    if (uiState.canAddMoreImages) it.clickable {
                                        imagePickerLauncher.launch("image/*")
                                    } else it // 클릭 비활성화
                                },

                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_plus),
                                contentDescription = null,
                                tint = if (!uiState.canAddMoreImages) colors.DarkGrey else colors.White
                            )
                        }
                    }
                    // 기존 이미지들 (수정 모드에서만 표시)
                    items(uiState.existingImageUrls.size) { index ->
                        Box(modifier = Modifier.size(80.dp)) {
                            AsyncImage(
                                model = uiState.existingImageUrls[index],
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { onRemoveExistingImage(index) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_x),
                                    contentDescription = null,
                                    tint = colors.White
                                )
                            }
                        }
                    }
                    // 새로 추가한 이미지들
                    items(uiState.imageUris.size) { index ->
                        Box(modifier = Modifier.size(80.dp)) {
                            AsyncImage(
                                model = uiState.imageUris[index],
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { onRemoveImage(index) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_x),
                                    contentDescription = null,
                                    tint = colors.White
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.photo_count,
                            uiState.currentImageCount,
                            3
                        ),
                        style = typography.info_r400_s12,
                        color = colors.NeonGreen,
                    )
                }
                SectionDivider()
                Text(
                    text = stringResource(R.string.group_private_option),
                    style = typography.smalltitle_sb600_s18_h24,
                    color = colors.White
                )
                Spacer(modifier = Modifier.padding(top = 12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.feed_private_comment),
                        style = typography.menu_r400_s14_h24,
                        color = colors.White
                    )
                    ToggleSwitchButton(
                        isChecked = uiState.isPrivate,
                        onToggleChange = onTogglePrivate
                    )
                }
                SectionDivider()

                Text(
                    text = stringResource(R.string.tag),
                    style = typography.smalltitle_sb600_s18_h24,
                    color = colors.White,
                )
                Spacer(modifier = Modifier.padding(top = 12.dp))
                GenreChipRow(
                    modifier = Modifier.width(18.dp),
                    genres = uiState.categories.map { it.category },
                    selectedIndex = uiState.selectedCategoryIndex,
                    onSelect = onSelectCategory
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (uiState.selectedCategoryIndex != -1) {
                    Spacer(modifier = Modifier.height(8.dp))

                    SubGenreChipGrid(
                        subGenres = uiState.availableTags,
                        selectedGenres = uiState.selectedTags,
                        onGenreToggle = onToggleTag
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 12.dp),
                            text = stringResource(
                                id = R.string.tag_count,
                                uiState.selectedTags.size,
                                5
                            ),
                            style = typography.info_r400_s12,
                            color = colors.NeonGreen
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.selected_tags),
                    style = typography.menu_r400_s14_h24,
                    color = colors.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (uiState.selectedTags.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.selectedTags) { tag ->
                            GenreChipButton(
                                text = tag,
                                onClick = {
                                    //해당 칩 눌렀을 때도 태그 삭제
                                    onRemoveTag(tag)
                                },
                                onCloseClick = {
                                    //x버튼 누르면 태그 삭제
                                    onRemoveTag(tag)
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        if (uiState.showBookSearchSheet) {
            GroupBookSearchBottomSheet(
                onDismiss = { onToggleBookSearchSheet(false) },
                onBookSelect = { book ->
                    onSelectBook(book)
                    onToggleBookSearchSheet(false)
                },
                onRequestBook = {
                    onToggleBookSearchSheet(false)
                },
                savedBooks = uiState.savedBooks,
                groupBooks = uiState.groupBooks,
                searchResults = uiState.searchResults,
                isLoading = uiState.isLoadingBooks,
                isSearching = uiState.isSearching,
                onSearch = onSearchBooks
            )
        }
    }
}


@Preview
@Composable
private fun FeedWriteScreenPreview() {
    ThipTheme {
        FeedWriteContent(
            uiState = FeedWriteUiState(
                selectedBook = BookData(
                    title = "미드나이트 라이브러리",
                    imageUrl = "https://picsum.photos/300/400?1",
                    author = "매트 헤이그",
                    isbn = "9788937477263"
                ),
                feedContent = "이 책을 읽고 정말 많은 생각이 들었습니다...",
                selectedCategoryIndex = 0,
                selectedTags = listOf("한국소설", "에세이"),
                categories = listOf(
                    FeedCategory(
                        category = "문학",
                        tagList = listOf("한국소설", "외국소설", "에세이", "시", "고전")
                    ),
                    FeedCategory(
                        category = "과학·IT",
                        tagList = listOf("프로그래밍", "AI", "과학일반")
                    ),
                    FeedCategory(
                        category = "사회과학",
                        tagList = listOf("프로그래밍", "AI", "과학일반")
                    ),
                    FeedCategory(
                        category = "인문학",
                        tagList = listOf("프로그래밍", "AI", "과학일반")
                    ),
                    FeedCategory(
                        category = "예술",
                        tagList = listOf("프로그래밍", "AI", "과학일반")
                    )
                ),
                imageUris = emptyList(),
                isPrivate = false
            )
        )
    }
}
