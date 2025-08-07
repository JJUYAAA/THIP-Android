package com.texthip.thip.ui.group.makeroom.screen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.GenreChipRow
import com.texthip.thip.ui.common.buttons.ToggleSwitchButton
import com.texthip.thip.ui.common.forms.WarningTextField
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.group.makeroom.component.GroupBookSearchBottomSheet
import com.texthip.thip.ui.group.makeroom.component.GroupInputField
import com.texthip.thip.ui.group.makeroom.component.GroupRoomDurationPicker
import com.texthip.thip.ui.group.makeroom.component.GroupSelectBook
import com.texthip.thip.ui.group.makeroom.component.GroupMemberLimitPicker
import com.texthip.thip.ui.group.makeroom.component.SectionDivider
import com.texthip.thip.ui.group.makeroom.mock.BookData
import com.texthip.thip.ui.group.makeroom.viewmodel.GroupMakeRoomViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography


@Composable
fun GroupMakeRoomScreen(
    viewModel: GroupMakeRoomViewModel,
    onNavigateBack: () -> Unit,
    onGroupCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // 에러 메시지 표시
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            viewModel.clearError()
        }
    }

    Box {
        Column(
            modifier = modifier
                .fillMaxSize()
                .then(if (uiState.showBookSearchSheet) Modifier.blur(5.dp) else Modifier),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputTopAppBar(
                title = stringResource(R.string.group_making_group),
                isRightButtonEnabled = uiState.isFormValid && !uiState.isLoading,
                onLeftClick = onNavigateBack,
                onRightClick = {
                    viewModel.createGroup(
                        onSuccess = { roomId ->
                            // TODO: 생성된 roomId를 사용하여 해당 방으로 이동할 수 있음
                            onGroupCreated()
                        },
                        onError = { errorMessage ->
                            // TODO: 에러 메시지 표시 (토스트 메시지 등)
                            // 현재는 uiState.errorMessage를 통해 처리
                        }
                    )
                }
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
                    selectedBook = uiState.selectedBook,
                    onChangeBookClick = { viewModel.toggleBookSearchSheet(true) },
                    onSelectBookClick = { viewModel.toggleBookSearchSheet(true) }
                )

                SectionDivider()

                Text(
                    text = stringResource(R.string.group_book_genre),
                    style = typography.smalltitle_sb600_s18_h24,
                    color = colors.White,
                )
                Spacer(modifier = Modifier.padding(top = 12.dp))
                GenreChipRow(
                    modifier = Modifier.width(18.dp),
                    genres = uiState.genres,
                    selectedIndex = uiState.selectedGenreIndex,
                    onSelect = viewModel::selectGenre
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(R.string.group_genre_select_comment),
                        style = typography.info_r400_s12,
                        color = colors.NeonGreen
                    )
                }

                SectionDivider()

                GroupInputField(
                    title = stringResource(R.string.group_room_title),
                    hint = stringResource(R.string.group_room_title_hint),
                    value = uiState.roomTitle,
                    maxLength = 15,
                    onValueChange = viewModel::updateRoomTitle
                )

                SectionDivider()

                GroupInputField(
                    title = stringResource(R.string.group_room_explain),
                    hint = stringResource(R.string.group_room_explain_hint),
                    value = uiState.roomDescription,
                    onValueChange = viewModel::updateRoomDescription
                )

                SectionDivider()

                GroupRoomDurationPicker(
                    onDateRangeSelected = viewModel::setDateRange
                )

                SectionDivider()

                GroupMemberLimitPicker(
                    selectedCount = uiState.memberLimit,
                    onCountSelected = viewModel::setMemberLimit
                )

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
                        text = stringResource(R.string.group_private_comment),
                        style = typography.menu_r400_s14_h24,
                        color = colors.White
                    )
                    ToggleSwitchButton(
                        isChecked = uiState.isPrivate,
                        onToggleChange = viewModel::togglePrivate
                    )
                }

                if (uiState.isPrivate) {
                    Spacer(modifier = Modifier.height(12.dp))
                    WarningTextField(
                        value = uiState.password,
                        onValueChange = viewModel::updatePassword,
                        hint = stringResource(R.string.group_password_hint),
                        showWarning = uiState.password.isNotEmpty() && uiState.password.length < 4,
                        warningMessage = stringResource(R.string.group_private_warning_message),
                        maxLength = 4,
                        isNumberOnly = true,
                        keyboardType = KeyboardType.NumberPassword,
                        showIcon = true,
                        showLimit = false,
                        containerColor = colors.DarkGrey02
                    )
                }

                Spacer(modifier = Modifier.padding(top = 134.dp))
            }
        }

        if (uiState.showBookSearchSheet) {
            GroupBookSearchBottomSheet(
                onDismiss = { viewModel.toggleBookSearchSheet(false) },
                onBookSelect = { book: BookData ->
                    viewModel.selectBook(book)
                    viewModel.toggleBookSearchSheet(false)
                },
                onRequestBook = {
                    viewModel.toggleBookSearchSheet(false)
                },
                savedBooks = uiState.savedBooks,
                groupBooks = uiState.groupBooks,
                isLoading = uiState.isLoadingBooks
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
    val mockViewModel: GroupMakeRoomViewModel = viewModel()

    ThipTheme {
        GroupMakeRoomScreen(
            viewModel = mockViewModel,
            onNavigateBack = { },
            onGroupCreated = { }
        )
    }
}
