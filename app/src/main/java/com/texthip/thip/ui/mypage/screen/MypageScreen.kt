package com.texthip.thip.ui.mypage.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun MyPageScreen(
    navController: NavController? = null,
    nickname: String,
    badgeText: String
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .background(colors.Black)
            .fillMaxSize()
            .verticalScroll(scrollState)
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
            Spacer(modifier = Modifier.height(20.dp))
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
            }
            Spacer(modifier = Modifier.height(40.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.etc),
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
                    text = stringResource(R.string.terms_of_use),
                    icon = painterResource(R.drawable.ic_doc),
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
                    text = stringResource(R.string.version_1_0),
                    icon = painterResource(R.drawable.ic_version),
                    contentColor = colors.White,
                    backgroundColor = colors.DarkGrey02,
                    hasRightIcon = true,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                )
            }
            Spacer(modifier = Modifier.height(184.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.log_out),
                    style = typography.feedcopy_r400_s14_h20,
                    color = colors.Grey01,
                    modifier = Modifier.clickable { showLogoutDialog = true }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.delete_account),
                    style = typography.feedcopy_r400_s14_h20,
                    color = colors.Grey01,
                    modifier = Modifier.clickable {
                        // TODO: 탈퇴 로직
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
    ThipTheme {
        MyPageScreen(
            nickname = "ThipUser01",
            badgeText = "문학가"
        )
    }
}