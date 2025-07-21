package com.texthip.thip.ui.feed.screen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.GenreChipRow
import com.texthip.thip.ui.common.buttons.ToggleSwitchButton
import com.texthip.thip.ui.common.forms.WarningTextField
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.group.makeroom.component.GroupBookSearchBottomSheet
import com.texthip.thip.ui.group.makeroom.component.GroupInputField
import com.texthip.thip.ui.group.makeroom.component.GroupRoomDurationPicker
import com.texthip.thip.ui.group.makeroom.component.GroupSelectBook
import com.texthip.thip.ui.group.makeroom.component.MemberLimitPicker
import com.texthip.thip.ui.group.makeroom.component.SectionDivider
import com.texthip.thip.ui.group.makeroom.mock.BookData
import com.texthip.thip.ui.group.makeroom.mock.GroupMakeRoomRequest
import com.texthip.thip.ui.group.makeroom.mock.dummyGroupBooks
import com.texthip.thip.ui.group.makeroom.mock.dummySavedBooks
import com.texthip.thip.ui.group.makeroom.viewmodel.ApiResult
import com.texthip.thip.ui.group.makeroom.viewmodel.GroupCreateResponse
import com.texthip.thip.ui.group.makeroom.viewmodel.GroupMakeRoomViewModel
import com.texthip.thip.ui.group.makeroom.viewmodel.GroupRepository
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography


@Composable
fun FeedWriteScreen(
    onNavigateBack: () -> Unit,
    onGroupCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var selectedGenreIndex by remember { mutableIntStateOf(-1) }
    val genres = listOf("문학", "과학·IT", "사회과학", "인문학", "예술")

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
                    onSelect = { selectedGenreIndex = it }
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
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
            onNavigateBack = { },
            onGroupCreated = { }
        )
    }
}
