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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.AsyncImage
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.GenreChipButton
import com.texthip.thip.ui.common.buttons.GenreChipRow
import com.texthip.thip.ui.common.buttons.SubGenreChipGrid
import com.texthip.thip.ui.common.buttons.ToggleSwitchButton
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.feed.mock.FeedData
import com.texthip.thip.ui.group.makeroom.component.GroupBookSearchBottomSheet
import com.texthip.thip.ui.group.makeroom.component.GroupInputField
import com.texthip.thip.ui.group.makeroom.component.GroupSelectBook
import com.texthip.thip.ui.group.makeroom.component.SectionDivider
import com.texthip.thip.ui.group.makeroom.mock.dummyGroupBooks
import com.texthip.thip.ui.group.makeroom.mock.dummySavedBooks
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun FeedWriteScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var feedData by remember { mutableStateOf(FeedData()) }
    val scrollState = rememberScrollState()
    val genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술")
    val subGenreMap = mapOf(
        0 to listOf("소설", "에세이", "시", "고전", "추리", "판타지", "로맨스", "SF", "공포", "역사"),
        1 to listOf("AI", "프로그래밍", "로봇", "IT 일반", "수학", "물리", "화학"),
        2 to listOf("정치", "경제", "법", "사회", "교육"),
        3 to listOf("철학", "역사", "심리", "종교", "윤리"),
        4 to listOf("음악", "미술", "공예", "무용", "연극")
    )
    val showBookSearchSheet = remember { mutableStateOf(false) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            val availableSlots = 3 - feedData.imageUris.size
            val imagesToAdd = uris.take(availableSlots) // 3장까지만 유지
            feedData.imageUris.addAll(imagesToAdd)
        }
    }
    val isImageLimitReached = feedData.imageUris.size >= 3
    val focusManager = LocalFocusManager.current

    Box {
        Column(
            modifier = modifier
                .fillMaxSize()
                .then(if (showBookSearchSheet.value) Modifier.blur(5.dp) else Modifier),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val isRightButtonEnabled = feedData.selectedBook != null && feedData.feedContent.isNotBlank() && feedData.selectedGenreIndex != -1 && feedData.selectedSubGenres.isNotEmpty()
            InputTopAppBar(
                title = stringResource(R.string.new_feed),
                rightButtonName = stringResource(R.string.registration),
                isRightButtonEnabled = isRightButtonEnabled,
                onLeftClick = onNavigateBack,
                onRightClick = {}
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
                    selectedBook = feedData.selectedBook,
                    onChangeBookClick = { showBookSearchSheet.value = true },
                    onSelectBookClick = { showBookSearchSheet.value = true }
                )

                SectionDivider()

                GroupInputField(
                    title = stringResource(R.string.write_feed),
                    hint = stringResource(R.string.write_feed_hint),
                    value = feedData.feedContent,
                    maxLength = 2000,
                    onValueChange = { newText ->
                        feedData = feedData.copy(feedContent = newText)
                        }
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
                    item {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(color = colors.DarkGrey02)
                                .border(width = 1.dp, color = if (isImageLimitReached) colors.DarkGrey else colors.Grey02,
                                )
                                .let {
                                    if (!isImageLimitReached) it.clickable {
                                        imagePickerLauncher.launch("image/*")
                                    } else it // 클릭 비활성화
                                },

                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_plus),
                                contentDescription = null,
                                tint = if (isImageLimitReached) colors.DarkGrey else colors.White
                            )
                        }
                    }
                    items(feedData.imageUris.size) { index ->
                        Box(modifier = Modifier.size(80.dp)) {
                            AsyncImage(
                                model = feedData.imageUris[index],
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { feedData.imageUris.removeAt(index) },
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
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(id = R.string.photo_count, feedData.imageUris.size, 3),
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
                        isChecked = feedData.isPrivate,
                        onToggleChange = { isChecked ->
                            feedData = feedData.copy(isPrivate = isChecked)
                        }
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
                    genres = genres,
                    selectedIndex = feedData.selectedGenreIndex,
                    onSelect = {
                        feedData = feedData.copy(selectedGenreIndex = it, selectedSubGenres = emptyList())
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (feedData.selectedGenreIndex != -1) {
                    val subGenres = subGenreMap[feedData.selectedGenreIndex].orEmpty()
                    Spacer(modifier = Modifier.height(8.dp))

                    SubGenreChipGrid(
                        subGenres = subGenres,
                        selectedGenres = feedData.selectedSubGenres,
                        onGenreToggle = { genre ->
                            val newSelected = if (feedData.selectedSubGenres.contains(genre)) {
                                feedData.selectedSubGenres - genre
                            } else {
                                if (feedData.selectedSubGenres.size < 5) {
                                    feedData.selectedSubGenres + genre
                                } else {
                                    feedData.selectedSubGenres
                                }
                            }

                            feedData = feedData.copy(selectedSubGenres = newSelected)
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = stringResource(id = R.string.tag_count, feedData.selectedSubGenres.size, 5),
                            style = typography.info_r400_s12,
                            color = colors.NeonGreen,
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
                if (feedData.selectedSubGenres.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(feedData.selectedSubGenres) { subGenre ->
                            GenreChipButton(
                                text = subGenre,
                                onClick = {
                                    //해당 칩 눌렀을 때도 서브장르 삭제
                                    feedData = feedData.copy(
                                        selectedSubGenres =feedData.selectedSubGenres - subGenre
                                    )
                                },
                                onCloseClick = {
                                    //x버튼 누르면 서브장르 삭제
                                    feedData = feedData.copy(
                                        selectedSubGenres = feedData.selectedSubGenres - subGenre
                                    )
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        if (showBookSearchSheet.value) {
            GroupBookSearchBottomSheet(
                onDismiss = { showBookSearchSheet.value = false },
                onBookSelect = { book ->
                    feedData = feedData.copy(selectedBook= book)
                    showBookSearchSheet.value = false
                },
                onRequestBook = {
                    showBookSearchSheet.value = false
                },
                savedBooks = dummySavedBooks,
                groupBooks = dummyGroupBooks
            )
        }
    }
}


@Preview
@Composable
private fun FeedWriteScreenPreview() {
    ThipTheme {
        FeedWriteScreen(
            onNavigateBack = { }
        )
    }
}
