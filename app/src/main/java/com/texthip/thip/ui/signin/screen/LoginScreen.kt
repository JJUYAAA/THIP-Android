package com.texthip.thip.ui.signin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ActionMediumButton
import com.texthip.thip.ui.theme.Purple
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography


@Composable
fun LoginScreen() {
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
                    append(stringResource(R.string.thip))
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
            onClick = {},
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
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun LoginScreenPrev() {
    LoginScreen()
}