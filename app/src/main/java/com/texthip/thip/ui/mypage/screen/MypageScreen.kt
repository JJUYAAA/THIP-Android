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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.MenuItemButton
import com.texthip.thip.ui.common.header.AuthorHeader
import com.texthip.thip.ui.common.modal.DialogPopup
import com.texthip.thip.ui.common.topappbar.LeftNameTopAppBar
import com.texthip.thip.ui.mypage.viewmodel.MyPageUiState
import com.texthip.thip.ui.mypage.viewmodel.MyPageViewModel
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import com.texthip.thip.utils.color.hexToColor

@Composable
fun MyPageScreen(
    navController: NavController,
    viewModel: MyPageViewModel = hiltViewModel(),
    onNavigateToEditProfile: () -> Unit,
    onNavigateToSavedFeeds: () -> Unit,
    onNavigateToNotificationSettings: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    MyPageContent(
        uiState = uiState,
        onEditProfileClick = onNavigateToEditProfile,
        onSavedFeedsClick = onNavigateToSavedFeeds,
        onNotificationSettingsClick = onNavigateToNotificationSettings,
        onLogoutClick = { viewModel.onLogoutClick() },
        onDismissLogoutDialog = { viewModel.onDismissLogoutDialog() },
        onConfirmLogout = { viewModel.confirmLogout() },
        onDeleteAccount = onDeleteAccount
    )
}
@Composable
fun MyPageContent(
    uiState: MyPageUiState,
    onEditProfileClick: () -> Unit,
    onSavedFeedsClick: () -> Unit,
    onNotificationSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDismissLogoutDialog: () -> Unit,
    onConfirmLogout: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    Box(
        Modifier
            .background(colors.Black)
            .fillMaxSize()
    ) {
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
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    AuthorHeader(
                        profileImage = uiState.profileImageUrl,
                        nickname = uiState.nickname,
                        badgeText = uiState.aliasName,
                        badgeTextColor = hexToColor(uiState.aliasColor),
                        buttonText = stringResource(R.string.edit),
                        onButtonClick = onEditProfileClick
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                }
                item {
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
                            onClick = onSavedFeedsClick
                        )
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
                item {
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
                            onClick = onNotificationSettingsClick
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
                }
                item {
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
                            modifier = Modifier.clickable { onLogoutClick() }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = stringResource(R.string.delete_account),
                            style = typography.feedcopy_r400_s14_h20,
                            color = colors.Grey01,
                            modifier = Modifier.clickable { onDeleteAccount() }
                        )
                    }
                }
            }
        }
        if (uiState.showLogoutDialog) {
            Dialog(onDismissRequest = { onDismissLogoutDialog() }) {
                DialogPopup(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.log_out),
                    description = stringResource(R.string.logout_description),
                    onCancel = { onDismissLogoutDialog() },
                    onConfirm = { onConfirmLogout() }
                )
            }
        }
    }
}


@Preview
@Composable
private fun MyPagePrev() {
    ThipTheme {
        MyPageContent(
            uiState = MyPageUiState(
                isLoading = false,
                nickname = "ThipUser01",
                aliasName = "문학가",
                aliasColor = "#FFFFFF"
            ),
            onLogoutClick = {},
            onEditProfileClick = {},
            onSavedFeedsClick = {},
            onNotificationSettingsClick = {},
            onDismissLogoutDialog = {},
            onConfirmLogout = {},
            onDeleteAccount = {}
        )
    }
}