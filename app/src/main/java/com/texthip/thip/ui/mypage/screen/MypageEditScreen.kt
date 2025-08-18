package com.texthip.thip.ui.mypage.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.texthip.thip.R
import com.texthip.thip.ui.common.forms.FormTextFieldDefault
import com.texthip.thip.ui.common.forms.WarningTextField
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.mypage.component.RoleCard
import com.texthip.thip.ui.mypage.mock.RoleItem
import com.texthip.thip.ui.mypage.viewmodel.EditProfileUiState
import com.texthip.thip.ui.mypage.viewmodel.EditProfileViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EditProfileContent(
        uiState = uiState,
        onNicknameChange = viewModel::onNicknameChange,
        onCardSelected = viewModel::selectCard,
        onSaveClick = viewModel::saveProfile
    )
}


@Composable
fun EditProfileContent(
    uiState: EditProfileUiState,
    onNicknameChange: (String) -> Unit,
    onCardSelected: (Int) -> Unit,
    onSaveClick: () -> Unit
) {
    val isChanged = uiState.nickname != uiState.initialNickname || uiState.selectedIndex != uiState.initialSelectedIndex
    val isRightButtonEnabled = isChanged && uiState.nickname.isNotBlank() && !uiState.isLoading

    Column(
        Modifier
            .fillMaxSize()
    ) {
        InputTopAppBar(
            title = stringResource(R.string.edit_profile),
            isRightButtonEnabled = isRightButtonEnabled,
            onLeftClick = {},
            onRightClick = onSaveClick
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.change_nickname),
                style = typography.smalltitle_sb600_s18_h24,
                color = colors.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            //TODO 컴포넌트 수정 필요 -> text count 추가, boolean 값으로 icon, limit 설정가능하도록
           /* FormTextFieldDefault(
                value = uiState.nickname,
                onValueChange = onNicknameChange,
                modifier = Modifier.fillMaxWidth(),
                showLimit = true,
                limit = 10,
                showIcon = false,
                containerColor = colors.DarkGrey02,
                hint = stringResource(R.string.change_nickname)
            )*/
            WarningTextField(
                containerColor = colors.DarkGrey02,
                value = uiState.nickname,
                onValueChange = onNicknameChange,
                hint = stringResource(R.string.nickname_condition),
                showWarning = uiState.nicknameWarningMessageResId != null,
                showIcon = false,
                showLimit = true,
                maxLength = 10,
                warningMessage = uiState.nicknameWarningMessageResId?.let { stringResource(it) } ?: ""
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.edit_role),
                style = typography.smalltitle_sb600_s18_h24,
                color = colors.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.role_description),
                style = typography.copy_r400_s14,
                color = colors.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )
            /*Text(
                text = stringResource(R.string.choice_one),
                style = typography.info_r400_s12,
                color = colors.NeonGreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )*/


            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 152.dp), // 카드 최소 크기
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                userScrollEnabled = false,
            ) {
                itemsIndexed(uiState.roleCards) { index, roleItem ->
                    RoleCard(
                        genre = roleItem.genre,
                        role = roleItem.role,
                        imageUrl = roleItem.imageUrl,
                        roleColor = roleItem.roleColor,
                        selected = uiState.selectedIndex == index,
                        onClick = { onCardSelected(index) }
                    )
                }
            }

        }
    }
}


@Preview
@Composable
private fun EditProfileScreenPrev() {
    val previewRoleCards = listOf(
        RoleItem("문학", "문학가", "", "#FFFFFF"),
        RoleItem("과학/IT", "과학자", "", "#FFFFFF")
    )
    val previewUiState = EditProfileUiState(
        isLoading = false,
        roleCards = previewRoleCards,
        selectedIndex = 0,
        nickname = "기존닉네임"
    )
    ThipTheme {
        EditProfileContent(
            uiState = previewUiState,
            onNicknameChange = {},
            onCardSelected = {},
            onSaveClick = {}
        )
    }
}