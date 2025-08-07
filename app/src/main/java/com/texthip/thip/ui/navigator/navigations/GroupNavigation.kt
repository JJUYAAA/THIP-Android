package com.texthip.thip.ui.navigator.navigations

import android.annotation.SuppressLint
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.texthip.thip.ui.group.done.Screen.GroupDoneScreen
import com.texthip.thip.ui.group.makeroom.screen.GroupMakeRoomScreen
import com.texthip.thip.ui.group.makeroom.viewmodel.GroupMakeRoomViewModel
import com.texthip.thip.ui.group.myroom.screen.GroupMyScreen
import com.texthip.thip.ui.group.room.screen.GroupRoomRecruitScreen
import com.texthip.thip.ui.group.room.screen.GroupRoomScreen
import com.texthip.thip.ui.group.screen.GroupScreen
import com.texthip.thip.ui.group.search.screen.GroupSearchScreen
import com.texthip.thip.ui.group.viewmodel.GroupViewModel
import com.texthip.thip.ui.group.myroom.viewmodel.GroupMyViewModel
import com.texthip.thip.ui.navigator.extensions.navigateToAlarm
import com.texthip.thip.ui.navigator.extensions.navigateToGroupDone
import com.texthip.thip.ui.navigator.extensions.navigateToGroupMakeRoom
import com.texthip.thip.ui.navigator.extensions.navigateToGroupMy
import com.texthip.thip.ui.navigator.extensions.navigateToGroupRecruit
import com.texthip.thip.ui.navigator.extensions.navigateToGroupRoom
import com.texthip.thip.ui.navigator.extensions.navigateToGroupSearch
import com.texthip.thip.ui.navigator.extensions.navigateToRecommendedGroupRecruit
import com.texthip.thip.ui.navigator.routes.GroupRoutes
import com.texthip.thip.ui.navigator.routes.MainTabRoutes

// Group
@SuppressLint("UnrememberedGetBackStackEntry")
fun NavGraphBuilder.groupNavigation(
    navController: NavHostController,
    navigateBack: () -> Unit
) {
    // 메인 Group 화면
    composable<MainTabRoutes.Group> { backStackEntry ->
        val groupViewModel: GroupViewModel = hiltViewModel()
        
        // 네비게이션 파라미터로 전달된 토스트 메시지가 있는지 확인
        LaunchedEffect(Unit) {
            val toastMessage = backStackEntry.savedStateHandle.get<String>("toast_message")
            
            toastMessage?.let { message ->
                backStackEntry.savedStateHandle.remove<String>("toast_message")
                groupViewModel.showToastMessage(message)
            }
        }
        
        GroupScreen(
            viewModel = groupViewModel,
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
            },
            onNavigateToGroupRecruit = { roomId ->
                navController.navigateToGroupRecruit(roomId)
            },
            onNavigateToGroupRoom = { roomId ->
                navController.navigateToGroupRoom(roomId)
            }
        )
    }
    
    // Group MakeRoom 화면
    composable<GroupRoutes.MakeRoom> {
        val viewModel: GroupMakeRoomViewModel = hiltViewModel()
        GroupMakeRoomScreen(
            viewModel = viewModel,
            onNavigateBack = {
                navigateBack()
            },
            onGroupCreated = {
                navigateBack()
            }
        )
    }
    
    // Group Done 화면
    composable<GroupRoutes.Done> {
        GroupDoneScreen(
            onNavigateBack = {
                navigateBack()
            }
        )
    }
    
    // Group My 화면
    composable<GroupRoutes.My> {
        val groupMyViewModel: GroupMyViewModel = hiltViewModel()
        
        GroupMyScreen(
            viewModel = groupMyViewModel,
            onCardClick = { room ->
                val isRecruiting = room.type == "recruiting"
                if (isRecruiting) {
                    navController.navigateToGroupRecruit(room.roomId)
                } else {
                    navController.navigateToGroupRoom(room.roomId)
                }
            },
            onNavigateBack = {
                navigateBack()
            }
        )
    }
    
    // Group Search 화면
    composable<GroupRoutes.Search> {
        val groupViewModel: GroupViewModel = hiltViewModel()
        val searchGroups by groupViewModel.searchGroups.collectAsState()
        
        GroupSearchScreen(
            roomList = searchGroups,
            onNavigateBack = {
                navigateBack()
            },
            onRoomClick = { room ->
                if (room.isRecruiting) {
                    navController.navigateToGroupRecruit(room.id)
                } else {
                    navController.navigateToGroupRoom(room.id)
                }
            }
        )
    }
    
    // Group Recruit 화면
    composable<GroupRoutes.Recruit> { backStackEntry ->
        val route = backStackEntry.toRoute<GroupRoutes.Recruit>()
        val roomId = route.roomId
        
        GroupRoomRecruitScreen(
            roomId = roomId,
            onRecommendationClick = { recommendation ->
                navController.navigateToRecommendedGroupRecruit(recommendation.id)
            },
            onNavigateToGroupScreen = { toastMessage ->
                // GroupScreen에 토스트 메시지 전달
                val groupEntry = navController.getBackStackEntry(MainTabRoutes.Group)
                groupEntry.savedStateHandle["toast_message"] = toastMessage
                navController.popBackStack(MainTabRoutes.Group, false)
            },
            onBackClick = {
                navigateBack()
            }
        )
    }
    
    // Group Room 화면
    composable<GroupRoutes.Room> { backStackEntry ->
        val route = backStackEntry.toRoute<GroupRoutes.Room>()
        val roomId = route.roomId
        val groupViewModel: GroupViewModel = hiltViewModel()

        GroupRoomScreen(
            onBackClick = {
                navigateBack()
            }
        )
    }
}