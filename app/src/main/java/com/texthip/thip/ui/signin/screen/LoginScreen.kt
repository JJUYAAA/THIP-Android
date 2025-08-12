package com.texthip.thip.ui.signin.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.texthip.thip.R
import com.texthip.thip.data.manager.TokenManager
import com.texthip.thip.ui.common.buttons.ActionMediumButton
import com.texthip.thip.ui.signin.viewmodel.LoginUiState
import com.texthip.thip.ui.signin.viewmodel.LoginViewModel
import com.texthip.thip.ui.theme.Purple
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value //by 오류로 인해 변경함
    val coroutineScope = rememberCoroutineScope()
    val tokenManager = remember { TokenManager(context) }

    // uiState가 바뀔 때마다 실행되어 화면 이동이나 토스트 메시지 등을 처리
    LaunchedEffect(key1 = uiState) {
        when (val state = uiState) {
            is LoginUiState.Success -> {
                // TODO: TokenManager를 사용해 토큰 저장하는 로직 추가
                coroutineScope.launch {
                    tokenManager.saveToken(state.response.token)
                }

                val destination = if (state.response.isNewUser) {
                    // "회원가입 화면으로 이동"
                } else {
                    // "홈 화면으로 이동"
                }
                // navController.navigate(destination)
                Toast.makeText(context, "로그인 성공!", Toast.LENGTH_SHORT).show()
                viewModel.clearLoginState() // 상태 초기화
            }

            is LoginUiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.clearLoginState() // 상태 초기화
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LoginContent(
            onKakaoLoginClick = { viewModel.kakaoLogin(context) },
            onGoogleLoginClick = { /* TODO: 구글 로그인 로직 연결 */ }
        )

        // 로딩 중일 때 화면 전체에 로딩 인디케이터 표시
        if (uiState is LoginUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
@Composable
fun LoginContent(
    onKakaoLoginClick: () -> Unit,
    onGoogleLoginClick: () -> Unit
) {
    Column(
        Modifier
            .background(colors.Black)
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = null,
            tint = Unspecified,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Purple)) {
                    append(stringResource(R.string.splash_thip))
                }
                append(stringResource(R.string.splash_ment))
            },
            style = typography.smalltitle_sb600_s18_h24,
            color = colors.White
        )
        Spacer(modifier = Modifier.height(140.dp))
        ActionMediumButton(
            text = stringResource(R.string.kakao_login),
            icon = painterResource(R.drawable.ic_kakaotalk),
            iconSize = 21,
            contentColor = colors.Black,
            backgroundColor = colors.KakaoYellow,
            hasRightIcon = false,
            modifier = Modifier.fillMaxWidth(),
            onClick = onKakaoLoginClick,
        )
        Spacer(modifier = Modifier.height(20.dp))
        ActionMediumButton(
            text = stringResource(R.string.google_login),
            icon = painterResource(R.drawable.ic_google),
            iconSize = 21,
            contentColor = colors.Black,
            backgroundColor = colors.White,
            hasRightIcon = false,
            modifier = Modifier.fillMaxWidth(),
            onClick = onGoogleLoginClick,
        )
    }
}

@Preview
@Composable
private fun LoginContentPreview() {
    ThipTheme {
        LoginContent(onKakaoLoginClick = {}, onGoogleLoginClick = {})
    }
}