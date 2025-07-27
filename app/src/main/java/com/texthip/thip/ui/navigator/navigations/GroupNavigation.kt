package com.texthip.thip.ui.navigator.navigations

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.texthip.thip.ui.group.makeroom.screen.GroupMakeRoomScreen
import com.texthip.thip.ui.group.makeroom.viewmodel.GroupMakeRoomViewModel
import com.texthip.thip.ui.group.screen.GroupScreen
import com.texthip.thip.ui.group.screen.GroupDoneScreen
import com.texthip.thip.ui.group.myroom.screen.GroupMyScreen
import com.texthip.thip.ui.group.search.screen.GroupSearchScreen
import com.texthip.thip.ui.group.viewmodel.GroupViewModel
import com.texthip.thip.ui.navigator.routes.MainTabRoutes
import com.texthip.thip.ui.navigator.routes.GroupRoutes
import com.texthip.thip.ui.navigator.extensions.navigateBack
import com.texthip.thip.ui.navigator.extensions.navigateToGroupMakeRoom
import com.texthip.thip.ui.navigator.extensions.navigateToGroupDone
import com.texthip.thip.ui.navigator.extensions.navigateToGroupSearch
import com.texthip.thip.ui.navigator.extensions.navigateToGroupMy
import com.texthip.thip.ui.navigator.extensions.navigateToAlarm

// Group
fun NavGraphBuilder.groupNavigation(navController: NavHostController) {
    // 메인 Group 화면
    composable<MainTabRoutes.Group> {
        GroupScreen(
            onNavigateToMakeRoom = {
                navController.navigateToGroupMakeRoom()
            },
            onNavigateToGroupDone = {
                navController.navigateToGroupDone()
            },
            onNavigateToAlarm = {
                navController.navigateToAlarm()
            },
            onNavigateToGroupSearch = {
                navController.navigateToGroupSearch()
            },
            onNavigateToGroupMy = {
                navController.navigateToGroupMy()
            }
        )
    }
    
    // Group MakeRoom 화면
    composable<GroupRoutes.MakeRoom> {
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
    
    // Group Done 화면
    composable<GroupRoutes.Done> {
        val groupViewModel: GroupViewModel = viewModel()
        val userName by groupViewModel.userName.collectAsState()
        val doneGroups by groupViewModel.doneGroups.collectAsState()
        
        GroupDoneScreen(
            name = userName,
            allDataList = doneGroups,
            onCardClick = { groupViewModel.onRoomCardClick(it) },
            onNavigateBack = {
                navController.navigateBack()
            }
        )
    }
    
    // Group My 화면
    composable<GroupRoutes.My> {
        val groupViewModel: GroupViewModel = viewModel()
        val myRoomGroups by groupViewModel.myRoomGroups.collectAsState()
        
        GroupMyScreen(
            allDataList = myRoomGroups,
            onCardClick = { groupViewModel.onRoomCardClick(it) },
            onNavigateBack = {
                navController.navigateBack()
            }
        )
    }
    
    // Group Search 화면
    composable<GroupRoutes.Search> {
        val groupViewModel: GroupViewModel = viewModel()
        val searchGroups by groupViewModel.searchGroups.collectAsState()
        
        GroupSearchScreen(
            roomList = searchGroups,
            onNavigateBack = {
                navController.navigateBack()
            }
        )
    }
}