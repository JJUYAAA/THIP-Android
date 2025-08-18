package com.texthip.thip.ui.signin.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.texthip.thip.R
import com.texthip.thip.ui.common.forms.WarningTextField
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.signin.viewmodel.SignupViewModel
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun SignupNicknameScreen(
    viewModel: SignupViewModel = hiltViewModel(),
    onNavigateToGenre: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(uiState.navigateToGenreScreen) {
        if (uiState.navigateToGenreScreen) {
            onNavigateToGenre()
            viewModel.onNavigatedToGenre()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    SignupNicknameContent(
        nickname = uiState.nickname,
        onNicknameChange = viewModel::onNicknameChange,
        onNextClick = viewModel::checkNickname,
        isLoading = uiState.isLoading,
        warningMessageResId = uiState.nicknameWarningMessageResId
    )
}

@Composable
fun SignupNicknameContent(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    onNextClick: () -> Unit,
    isLoading: Boolean,
    warningMessageResId: Int?
) {
    val isRightButtonEnabled = nickname.isNotBlank() && !isLoading // 닉네임 공백 아닐때 버튼 활성화

    Column(
        Modifier
            .background(colors.Black)
            .fillMaxSize()
    ) {
        InputTopAppBar(
            title = stringResource(R.string.settings_1),
            isRightButtonEnabled = isRightButtonEnabled,
            rightButtonName = stringResource(R.string.next),
            isLeftIconVisible = false,
            onLeftClick = {},
            onRightClick = onNextClick
        )
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.nickname),
                style = typography.smalltitle_sb600_s18_h24,
                color = colors.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            WarningTextField(
                containerColor = colors.DarkGrey02,
                value = nickname,
                onValueChange = onNicknameChange,
                hint = stringResource(R.string.nickname_condition),
                showWarning = warningMessageResId != null,
                showIcon = false,
                showLimit = true,
                maxLength = 10,
                warningMessage = warningMessageResId?.let { stringResource(it) } ?: ""
            )
        }
    }
}

@Preview
@Composable
private fun SignupNicknameContentPrev() {
    SignupNicknameContent(
        nickname = "중복된닉네임",
        onNicknameChange = {},
        onNextClick = {},
        isLoading = false,
        warningMessageResId = R.string.nickname_warning
    )
}

@Preview(name = "일반 상태 (비어있음)", showBackground = true)
@Composable
private fun SignupNicknameContentPreview_Normal() {
    var nickname by remember { mutableStateOf("") }
    var warningMessageResId by remember { mutableStateOf<Int?>(null) }

    SignupNicknameContent(
        nickname = nickname,
        onNicknameChange = {
            nickname = it
            warningMessageResId = null
        },
        onNextClick = {
            if (nickname == "test") {
                warningMessageResId = R.string.nickname_warning
            }
        },
        isLoading = false,
        warningMessageResId = warningMessageResId
    )
}
