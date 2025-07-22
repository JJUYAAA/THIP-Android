package com.texthip.thip.ui.feed.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.saveable.rememberSaveable

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
    var roomTitle by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(false) }
    val showBookSearchSheet = remember { mutableStateOf(false) }
    val selectedBook = remember { mutableStateOf<BookData?>(null) }
    Box {
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputTopAppBar(
                title = stringResource(R.string.new_feed),
                rightButtonName = stringResource(R.string.registration),
                isRightButtonEnabled = false,
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
                Spacer(modifier = Modifier.padding(top = 20.dp))

                GroupSelectBook(
                    selectedBook = selectedBook.value,
                    onChangeBookClick = { showBookSearchSheet.value = true },
                    onSelectBookClick = { showBookSearchSheet.value = true }
                )

                SectionDivider()

                GroupInputField(
                    title = stringResource(R.string.write_feed),
                    hint = stringResource(R.string.write_feed_hint),
                    value = roomTitle,
                    maxLength = 2000,
                    onValueChange = { roomTitle = it }
                )

                SectionDivider()

                Text(
                    text = stringResource(R.string.add_photo),
                    style = typography.smalltitle_sb600_s18_h24,
                    color = colors.White
                )
                Spacer(modifier = Modifier.padding(top = 12.dp))

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
                            text = "${selectedSubGenres.size}/5",
                            style = typography.info_r400_s12,
                            color = colors.NeonGreen,
                            )
                    }
                }
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
                                    // 칩을 클릭해도 제거되도록 할 수 있음
                                    selectedSubGenres = selectedSubGenres - subGenre
                                },
                                onCloseClick = {
                                    // X 버튼 클릭시 해당 서브장르 제거
                                    selectedSubGenres = selectedSubGenres - subGenre
                                }
                            )
                        }
                    }
                }


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

        // 로딩 인디케이터
        /*if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colors.NeonGreen)
            }
        }*/
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
