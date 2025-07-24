package com.texthip.thip.ui.navigator.navigations

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.texthip.thip.ui.group.makeroom.screen.GroupMakeRoomScreen
import com.texthip.thip.ui.group.makeroom.viewmodel.GroupMakeRoomViewModel
import com.texthip.thip.ui.group.screen.GroupScreen
import com.texthip.thip.ui.navigator.data.Routes
import com.texthip.thip.ui.navigator.extensions.navigateBack
import com.texthip.thip.ui.navigator.extensions.navigateToGroupMakeRoom

// Group
fun NavGraphBuilder.groupNavigation(navController: NavHostController) {
    // 메인 Group 화면
    composable<Routes.Group> {
        GroupScreen(
            onNavigateToMakeRoom = {
                navController.navigateToGroupMakeRoom()
            }
        )
    }
    
    // Group MakeRoom 화면
    composable<Routes.GroupMakeRoom> {
        val viewModel: GroupMakeRoomViewModel = viewModel()
        GroupMakeRoomScreen(
            viewModel = viewModel,
            onNavigateBack = {
                navController.navigateBack()
            },
            onGroupCreated = {
                navController.navigateBack()
            }
        )
    }
}