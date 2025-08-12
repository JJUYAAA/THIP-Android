package com.texthip.thip.ui.navigator.navigations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.texthip.thip.ui.navigator.extensions.navigateToBookDetail
import com.texthip.thip.ui.navigator.extensions.navigateToBookGroup
import com.texthip.thip.ui.navigator.extensions.navigateToGroupRecruit
import com.texthip.thip.ui.navigator.extensions.navigateToRegisterBook
import com.texthip.thip.ui.navigator.routes.MainTabRoutes
import com.texthip.thip.ui.navigator.routes.SearchRoutes
import com.texthip.thip.ui.search.screen.SearchBookScreen
import com.texthip.thip.ui.search.screen.SearchBookDetailScreen
import com.texthip.thip.ui.search.screen.SearchBookGroupScreen

fun NavGraphBuilder.searchNavigation(navController: NavHostController) {
    composable<MainTabRoutes.Search> {
        SearchBookScreen(
            onBookClick = { isbn ->
                navController.navigateToBookDetail(isbn)
            },
            onRequestBook = {
                navController.navigateToRegisterBook()
            }
        )
    }
    
    composable<SearchRoutes.BookDetail> { backStackEntry ->
        val route = backStackEntry.toRoute<SearchRoutes.BookDetail>()
        val isbn = route.isbn
        
        SearchBookDetailScreen(
            isbn = isbn,
            onLeftClick = {
                navController.popBackStack()
            },
            onRightClick = {
                // TODO: 우측 버튼 액션 구현
            },
            onRecruitingGroupClick = {
                navController.navigateToBookGroup(isbn)
            },
            onWriteFeedClick = {
                // TODO: 피드 작성 화면으로 이동
            }
        )
    }
    
    composable<SearchRoutes.BookGroup> { backStackEntry ->
        val route = backStackEntry.toRoute<SearchRoutes.BookGroup>()
        val isbn = route.isbn
        
        SearchBookGroupScreen(
            isbn = isbn,
            onLeftClick = {
                navController.popBackStack()
            },
            onCardClick = { roomId ->
                navController.navigateToGroupRecruit(roomId)
            },
            onCreateRoomClick = {
                // TODO: 방 생성 화면으로 이동
            }
        )
    }
}