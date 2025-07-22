package com.texthip.thip.ui.feed.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.GenreChipButton
import com.texthip.thip.ui.common.buttons.GenreChipRow
import com.texthip.thip.ui.common.buttons.SubGenreChipGrid
import com.texthip.thip.ui.common.buttons.ToggleSwitchButton
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.group.makeroom.component.GroupBookSearchBottomSheet
import com.texthip.thip.ui.group.makeroom.component.GroupInputField
import com.texthip.thip.ui.group.makeroom.component.GroupSelectBook
import com.texthip.thip.ui.group.makeroom.component.SectionDivider
import com.texthip.thip.ui.group.makeroom.mock.BookData
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
    val scrollState = rememberScrollState()
    var selectedGenreIndex by rememberSaveable { mutableIntStateOf(-1) }
    val genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술")
    var selectedSubGenres by remember { mutableStateOf<List<String>>(emptyList()) }
    val subGenreMap = mapOf(
        0 to listOf("소설", "에세이", "시", "고전", "추리", "판타지", "로맨스", "SF", "공포", "역사"),
        1 to listOf("AI", "프로그래밍", "로봇", "IT 일반", "수학", "물리", "화학"),
        2 to listOf("정치", "경제", "법", "사회", "교육"),
        3 to listOf("철학", "역사", "심리", "종교", "윤리"),
        4 to listOf("음악", "미술", "공예", "무용", "연극")
    )
    var feed_content by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(false) }
    val showBookSearchSheet = remember { mutableStateOf(false) }
    val selectedBook = remember { mutableStateOf<BookData?>(null) }
    val imageUris = remember { mutableStateListOf<Uri>() }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            if (imageUris.size < 3) imageUris.add(it)
        }
    }
    val isImageLimitReached = imageUris.size >= 3
    Box {
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val isRightButtonEnabled = selectedBook.value != null && feed_content.isNotBlank() && selectedGenreIndex != -1 && selectedSubGenres.isNotEmpty()
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
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Top,
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                GroupSelectBook(
                    selectedBook = selectedBook.value,
                    onChangeBookClick = { showBookSearchSheet.value = true },
                    onSelectBookClick = { showBookSearchSheet.value = true }
                )

                SectionDivider()

                GroupInputField(
                    title = stringResource(R.string.write_feed),
                    hint = stringResource(R.string.write_feed_hint),
                    value = feed_content,
                    maxLength = 2000,
                    onValueChange = { feed_content = it }
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
                    items(imageUris.size) { index ->
                        Box(modifier = Modifier.size(80.dp)) {
                            Image(
                                painter = rememberAsyncImagePainter(imageUris[index]),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { imageUris.removeAt(index) },
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
                        text = stringResource(id = R.string.photo_count, imageUris.size, 3),
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
                        isChecked = isPrivate,
                        onToggleChange = { isPrivate = it }
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
                    selectedIndex = selectedGenreIndex,
                    onSelect = {
                        selectedGenreIndex = it
                        selectedSubGenres = emptyList()
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (selectedGenreIndex != -1) {
                    val subGenres = subGenreMap[selectedGenreIndex].orEmpty()
                    Spacer(modifier = Modifier.height(8.dp))

                    SubGenreChipGrid(
                        subGenres = subGenres,
                        selectedGenres = selectedSubGenres,
                        onGenreToggle = { genre ->
                            selectedSubGenres = if (selectedSubGenres.contains(genre)) {
                                selectedSubGenres - genre
                            } else {
                                if (selectedSubGenres.size < 5) selectedSubGenres + genre else selectedSubGenres
                            }
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = stringResource(id = R.string.tag_count, selectedSubGenres.size, 5),
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
                if (selectedSubGenres.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(selectedSubGenres) { subGenre ->
                            GenreChipButton(
                                text = subGenre,
                                onClick = {
                                    //해당 칩 눌렀을 때도 서브장르 삭제
                                    selectedSubGenres = selectedSubGenres - subGenre
                                },
                                onCloseClick = {
                                    //x버튼 누르면 서브장르 삭제
                                    selectedSubGenres = selectedSubGenres - subGenre
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
                    selectedBook.value = book
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
private fun GroupMakeRoomScreenPreview() {
    ThipTheme {
        FeedWriteScreen(
            onNavigateBack = { }
        )
    }
}
