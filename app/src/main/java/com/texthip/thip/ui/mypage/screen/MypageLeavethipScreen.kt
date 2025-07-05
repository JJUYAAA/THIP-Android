package com.texthip.thip.ui.mypage.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.window.Dialog
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.CheckboxButton
import com.texthip.thip.ui.common.modal.DialogPopup
import com.texthip.thip.ui.common.topappbar.DefaultTopAppBar
import com.texthip.thip.ui.theme.DarkGrey02
import com.texthip.thip.ui.theme.Red
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun DeleteAccountScreen() {
    var isChecked by rememberSaveable { mutableStateOf(false) }
    val backgroundColor = if (isChecked) colors.Purple else colors.Grey02
    var isDialogVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        Modifier
            .background(colors.Black)
            .fillMaxSize()
    ) {
        DefaultTopAppBar(
            title = stringResource(R.string.delete_account),
            onLeftClick = {},
        )
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
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
                        color = colors.White
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
                        color = colors.White
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(R.string.leave_thip_notice_5),
                        style = typography.copy_r400_s14,
                        color = colors.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(29.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.leave_thip_agree),
                    style = typography.copy_r400_s14,
                    color = colors.White,
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
        Spacer(modifier = Modifier.height(55.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(backgroundColor)
                .clickable(
                    enabled = isChecked,
                    onClick = {
                        isDialogVisible = true
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
        if (isDialogVisible) {
            Dialog(onDismissRequest = { isDialogVisible = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    DialogPopup(
                        modifier = Modifier
                            .fillMaxWidth(),
                        title = stringResource(R.string.ask_account_deletion),
                        description = stringResource(R.string.delete_account_description),
                        onCancel = { isDialogVisible = false },
                        onConfirm = {
                            isDialogVisible = false
                            // TODO: 회원탈퇴 로직
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DeleteAccountScreenPrev() {
    DeleteAccountScreen()
}