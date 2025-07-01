package com.texthip.thip.ui.myPage.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.texthip.thip.R
import com.texthip.thip.ui.common.buttons.ToggleSwitchButton
import com.texthip.thip.ui.common.modal.Toast
import com.texthip.thip.ui.common.topappbar.InputTopAppBar
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography
import kotlinx.coroutines.delay

@Composable
fun NotificationScreen() {
    var isChecked by rememberSaveable { mutableStateOf(true) }
    var toastMessage by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(2000)
            toastMessage = null
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        toastMessage?.let { message ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f)
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 15.dp, vertical = 15.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Toast(
                    message = stringResource(
                        if (message == "push_on") R.string.push_on else R.string.push_off
                    ),
                    date = "2025년 6월 29일 22시 30분",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Scaffold(
            containerColor = colors.Black,
            topBar = {
                InputTopAppBar(
                    title = stringResource(R.string.notification_settings),
                    isRightButtonEnabled = true,
                    onLeftClick = {},
                    onRightClick = {}
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.push_notification),
                    style = typography.smalltitle_sb600_s18_h24,
                    color = colors.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.notification_description),
                        style = typography.menu_r400_s14_h24,
                        color = colors.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .weight(1f)
                    )
                    ToggleSwitchButton(
                        isChecked = isChecked,
                        onToggleChange = {
                            isChecked = it
                            toastMessage = if (it) "push_on" else "push_off"
                        }
                    )
                }

            }


        }
    }
}

@Preview
@Composable
private fun NotificationScreenPrev() {
    NotificationScreen()
}