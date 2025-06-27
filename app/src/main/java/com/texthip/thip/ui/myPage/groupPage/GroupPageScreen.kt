package com.texthip.thip.ui.myPage.groupPage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.texthip.thip.ui.myPage.viewModel.MyPageViewModel

@Composable
fun GroupPageScreen(
    navController: NavController,
    viewModel: MyPageViewModel = viewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "MyPage Screen")
    }
}