package com.texthip.thip.ui.mypage.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.MenuItemButton
import com.texthip.thip.ui.common.header.AuthorHeader
import com.texthip.thip.ui.common.modal.DialogPopup
import com.texthip.thip.ui.common.topappbar.LeftNameTopAppBar
import com.texthip.thip.ui.theme.Black
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun MyPageScreen(
    navController: NavController? = null,
    nickname: String,
    badgeText: String
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    Column(
        Modifier
            .background(colors.Black)
            .fillMaxSize()
    ) {
        LeftNameTopAppBar(
            title = stringResource(R.string.my_page),
            leftIcon = painterResource(R.drawable.ic_search),
            rightIcon = painterResource(R.drawable.ic_plus)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            AuthorHeader(
                profileImage = null,
                nickname = nickname,
                badgeText = badgeText,
                buttonText = stringResource(R.string.edit)
            )
            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.my_activity),
                    style = typography.smalltitle_sb600_s18_h24,
                    color = colors.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                MenuItemButton(
                    text = stringResource(R.string.saved),
                    icon = painterResource(R.drawable.ic_save),
                    contentColor = colors.White,
                    backgroundColor = colors.DarkGrey02,
                    hasRightIcon = true,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                MenuItemButton(
                    text = stringResource(R.string.reactions),
                    icon = painterResource(R.drawable.ic_heart),
                    contentColor = colors.White,
                    backgroundColor = colors.DarkGrey02,
                    hasRightIcon = true,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.menu),
                    style = typography.smalltitle_sb600_s18_h24,
                    color = colors.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                MenuItemButton(
                    text = stringResource(R.string.notification_settings),
                    icon = painterResource(R.drawable.ic_notice),
                    contentColor = colors.White,
                    backgroundColor = colors.DarkGrey02,
                    hasRightIcon = true,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                MenuItemButton(
                    text = stringResource(R.string.guide),
                    icon = painterResource(R.drawable.ic_guide),
                    contentColor = colors.White,
                    backgroundColor = colors.DarkGrey02,
                    hasRightIcon = true,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                MenuItemButton(
                    text = stringResource(R.string.customer_service),
                    icon = painterResource(R.drawable.ic_center),
                    contentColor = colors.White,
                    backgroundColor = colors.DarkGrey02,
                    hasRightIcon = true,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                MenuItemButton(
                    text = stringResource(R.string.delete_account),
                    icon = painterResource(R.drawable.ic_bye),
                    contentColor = colors.White,
                    backgroundColor = colors.DarkGrey02,
                    hasRightIcon = true,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                MenuItemButton(
                    text = stringResource(R.string.log_out),
                    icon = painterResource(R.drawable.ic_logout),
                    contentColor = colors.Red,
                    backgroundColor = colors.DarkGrey02,
                    hasRightIcon = false,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        showLogoutDialog = true
                    }
                )
            }
            if (showLogoutDialog) {
                Dialog(onDismissRequest = { showLogoutDialog = false }) {
                    DialogPopup(
                        modifier = Modifier
                            .fillMaxWidth(),
                        title = stringResource(R.string.log_out),
                        description = stringResource(R.string.logout_description),
                        onCancel = { showLogoutDialog = false },
                        onConfirm = {
                            showLogoutDialog = false
                            // TODO: 로그아웃 로직
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MyPagePrev() {
    MyPageScreen(
        nickname = "ThipUser01",
        badgeText = "문학가"
    )
}