package com.texthip.thip.ui.myPage.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.CheckboxButton
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.theme.*
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun DeleteAccountScreen() {
    var isChecked by rememberSaveable { mutableStateOf(false) }
    val backgroundColor = if (isChecked) Purple else Grey02

    Scaffold(
        containerColor = Black,
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.delete_account),
                onLeftClick = {},
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(backgroundColor)
                    .clickable(onClick = {
                        //TODO 탈퇴 로직
                    }),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bye),
                    contentDescription = null,
                    tint = colors.White
                )
                Text(
                    text = stringResource(R.string.leave_thip),
                    color = colors.White,
                    style = typography.smalltitle_sb600_s18_h24,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(20.dp)
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(DarkGrey02, shape = RoundedCornerShape(12.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.leave_thip_notice_title),
                        style = typography.menu_m500_s16_h24,
                        color = White
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(R.string.leave_thip_notice_1) + " ")
                            withStyle(style = SpanStyle(color = Red)) {
                                append(stringResource(R.string.leave_thip_notice_2))
                            }
                            append(" ")
                            append(stringResource(R.string.leave_thip_notice_3))
                        },
                        style = typography.copy_r400_s14,
                        color = colors.White
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(R.string.leave_thip_notice_4),
                        style = typography.copy_r400_s14,
                        color = White
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(R.string.leave_thip_notice_5),
                        style = typography.copy_r400_s14,
                        color = White
                    )
                }
            }
            Spacer(modifier = Modifier.height(29.dp))
            Row (verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = stringResource(R.string.leave_thip_agree),
                    style = typography.copy_r400_s14,
                    color = White,
                    modifier = Modifier.weight(1f)
                )
                CheckboxButton(
                    isChecked = isChecked,
                    onCheckedChange = {
                        isChecked = it

                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun DeleteAccountScreenPrev() {
    DeleteAccountScreen()
}