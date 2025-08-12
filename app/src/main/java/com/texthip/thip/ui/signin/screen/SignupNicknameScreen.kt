package com.texthip.thip.ui.signin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.texthip.thip.R
import com.texthip.thip.ui.common.forms.WarningTextField
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SigninNicknameScreen(
    navController: NavController,
) {
    var nickname by rememberSaveable { mutableStateOf("") }
    var showWarning by remember { mutableStateOf(false) }
    var warningMessageResId by remember { mutableStateOf<Int?>(null) }
    val isRightButtonEnabled by remember { derivedStateOf { nickname.isNotBlank() } } // 닉네임 공백 아닐때 버튼 활성화
    val coroutineScope = rememberCoroutineScope()

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
            onRightClick = {
                //TODO 서버 연동시 로직 변경 필요
                coroutineScope.launch {
                    delay(500) // 서버 응답 시뮬레이션
                    if (nickname == "test") {
                        showWarning = true
                        warningMessageResId = R.string.nickname_warning
                    } else {
                        showWarning = false
                        warningMessageResId = null
                        // 다음 페이지로 이동
                    }
                }
            }
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
                onValueChange = {
                    nickname = it
                    showWarning = false // 입력 중에는 경고 숨기기
                },
                hint = stringResource(R.string.nickname_condition),
                showWarning = showWarning,
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
private fun SigninNicknameScreenPrev() {
    val navController = rememberNavController()
    SigninNicknameScreen(navController)
}