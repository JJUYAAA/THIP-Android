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
import androidx.compose.runtime.LaunchedEffect
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
import com.texthip.thip.R
import com.texthip.thip.ui.common.forms.SingleDigitBox
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import kotlinx.coroutines.delay

@Composable
fun GroupRoomUnlockScreen(
    onBackClick: () -> Unit = {},
    onPasswordComplete: (String) -> Unit = {},
    correctPassword: String = "1234" // 실제로는 외부에서 받아올 값
) {
    var password by remember { mutableStateOf(arrayOf("", "", "", "")) }
    var showError by remember { mutableStateOf(false) }
    val focusRequesters = remember { List(4) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current

    // 비밀번호가 4자리 완성되면 자동으로 검증
    LaunchedEffect(password.toList()) {
        val fullPassword = password.joinToString("")
        if (fullPassword.length == 4 && password.all { it.length == 1 }) {
            if (fullPassword == correctPassword) {
                showError = false
                onPasswordComplete(fullPassword)
            } else {
                showError = true
                delay(1000)
                password = arrayOf("", "", "", "")
                showError = false
                focusRequesters[0].requestFocus()
            }
        } else {
            showError = false
        }
    }

    // 화면 진입 시 키보드 자동 포커스
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
        keyboardController?.show()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 상단 앱바
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
                    text = stringResource(R.string.group_secret_screen_comment),
                    style = typography.smalltitle_sb600_s18_h24,
                    color = colors.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { index ->
                        SingleDigitBox(
                            value = password[index],
                            onValueChange = { input ->
                                if (input.length <= 1 && input.all { it.isDigit() }) {
                                    val newPassword = password.copyOf()
                                    newPassword[index] = input
                                    password = newPassword

                                    if (input.isNotEmpty() && index < 3) {
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                }
                            },
                            onBackspace = {
                                if (password[index].isEmpty() && index > 0) {
                                    val newPassword = password.copyOf()
                                    newPassword[index - 1] = ""
                                    password = newPassword
                                    focusRequesters[index - 1].requestFocus()
                                }
                            },
                            containerColor = colors.DarkGrey50,
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
fun GroupRoomUnlockScreenPreview() {
    ThipTheme {
        GroupRoomUnlockScreen(
            onBackClick = {},
            onPasswordComplete = { password ->
            },
            correctPassword = "1234"
        )
    }
}