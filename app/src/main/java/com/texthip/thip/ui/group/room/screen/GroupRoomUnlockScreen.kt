package com.texthip.thip.ui.group.room.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.texthip.thip.R
import com.texthip.thip.ui.common.forms.SingleDigitBox
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.group.room.viewmodel.GroupRoomUnlockViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.utils.rooms.advancedImePadding
import kotlinx.coroutines.delay

@Composable
fun GroupRoomUnlockScreen(
    roomId: Int = 0,
    viewModel: GroupRoomUnlockViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onSuccessNavigation: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var password by remember { mutableStateOf(arrayOf("", "", "", "")) }
    var showError by remember { mutableStateOf(false) }
    val focusRequesters = remember { List(4) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(password.toList()) {
        val fullPassword = password.joinToString("")
        if (!uiState.isLoading && fullPassword.length == 4 && password.all { it.length == 1 }) {
            viewModel.checkPassword(roomId, fullPassword)
        }
    }

    LaunchedEffect(uiState.passwordMatched) {
        when (uiState.passwordMatched) {
            true -> {
                // 비밀번호 일치: 성공 콜백 호출하여 네비게이션 처리
                keyboardController?.hide()
                viewModel.resetPasswordState()
                onSuccessNavigation()
            }

            false -> {
                showError = true
                delay(1000) // 사용자에게 에러 메시지를 보여줄 시간
                password = arrayOf("", "", "", "")
                showError = false
                focusRequesters[0].requestFocus()
                viewModel.resetPasswordState() // ViewModel 상태 초기화
            }

            null -> {}
        }
    }

    // 화면 진입 시 상태 초기화 및 키보드 자동 표시
    LaunchedEffect(Unit) {
        viewModel.initializeState() // 상태 완전 초기화
        delay(300) // 화면 전환 애니메이션 후 키보드 표시
        focusRequesters[0].requestFocus()
        keyboardController?.show()
    }

    // 화면 종료 시 리소스 정리
    DisposableEffect(Unit) {
        onDispose {
            // 키보드 숨기기
            keyboardController?.hide()
        }
    }

    GroupRoomUnlockContent(
        password = password,
        showError = showError,
        focusRequesters = focusRequesters,
        onBackClick = onBackClick,
        onPasswordChange = { index, input ->
            if (input.length <= 1 && input.all { it.isDigit() }) {
                val newPassword = password.copyOf()
                val wasEmpty = password[index].isEmpty()
                newPassword[index] = input
                password = newPassword

                // 숫자가 입력되면 다음 칸으로 이동
                if (input.isNotEmpty() && index < 3) {
                    focusRequesters[index + 1].requestFocus()
                } else if (input.isEmpty() && !wasEmpty && index > 0) {
                    focusRequesters[index - 1].requestFocus()
                }
            }
        },
        onBackspace = { index ->
            // 빈 박스에서 백스페이스 → 이전 박스로 이동
            if (index > 0) {
                val prevIndex = index - 1
                focusRequesters[prevIndex].requestFocus()
            }
        }
    )
}

@Composable
private fun GroupRoomUnlockContent(
    password: Array<String>,
    showError: Boolean,
    focusRequesters: List<FocusRequester>,
    onBackClick: () -> Unit,
    onPasswordChange: (Int, String) -> Unit,
    onBackspace: (Int) -> Unit
) {

    Box(modifier = Modifier.fillMaxSize().advancedImePadding()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            DefaultTopAppBar(
                isRightIconVisible = false,
                isTitleVisible = false,
                onLeftClick = onBackClick,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(160.dp))

                Text(
                    modifier = Modifier.padding(bottom = 32.dp),
                    text = stringResource(R.string.group_secret_screen_comment),
                    style = typography.smalltitle_sb600_s18_h24,
                    color = colors.White,
                    textAlign = TextAlign.Center
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { index ->
                        SingleDigitBox(
                            value = password[index],
                            onValueChange = { input -> onPasswordChange(index, input) },
                            onBackspace = { onBackspace(index) },
                            borderColor = if (showError) colors.Red else Color.Transparent,
                            modifier = Modifier
                                .size(44.dp)
                                .focusRequester(focusRequesters[index])
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 에러 메시지
                if (showError) {
                    Text(
                        text = stringResource(R.string.group_secret_screen_error_comment),
                        style = typography.menu_r400_s14_h24,
                        color = colors.Red,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupRoomUnlockContentPreview() {
    ThipTheme {
        GroupRoomUnlockContent(
            password = arrayOf("", "", "", ""),
            showError = false,
            focusRequesters = List(4) { FocusRequester() },
            onBackClick = {},
            onPasswordChange = { _, _ -> },
            onBackspace = {}
        )
    }
}