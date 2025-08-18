package com.texthip.thip.ui.signin.screen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ActionMediumButton
import com.texthip.thip.ui.signin.viewmodel.LoginUiState
import com.texthip.thip.ui.signin.viewmodel.LoginViewModel
import com.texthip.thip.ui.theme.Purple
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography


@Composable
fun LoginScreen(
    onNavigateToSignup: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value //by 오류로 인해 변경함

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    // 성공 시, ID 토큰을 ViewModel으로 전달
                    val account = task.getResult(ApiException::class.java)!!
                    val idToken = account.idToken!!
                    viewModel.googleLogin(idToken)
                } catch (e: ApiException) {
                    // 실패 시 에러 처리
                    Toast.makeText(context, "구글 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    //구글 로그인 클라이언트
    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    // uiState가 바뀔 때마다 실행되어 화면 이동이나 토스트 메시지 등을 처리
    LaunchedEffect(key1 = uiState) {
        when (val state = uiState) {
            is LoginUiState.Success -> {
                if (state.response.isNewUser) {
                    onNavigateToSignup()
                } else {
                    onLoginSuccess()
                }
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
            onGoogleLoginClick = { googleSignInLauncher.launch(googleSignInClient.signInIntent)}
        )

        // 로딩 중일 때 화면 전체에 로딩 인디케이터 표시
        if (uiState is LoginUiState.Loading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
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