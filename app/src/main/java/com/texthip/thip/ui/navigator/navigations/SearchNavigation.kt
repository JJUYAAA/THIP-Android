package com.texthip.thip.ui.navigator.navigations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.texthip.thip.ui.navigator.extensions.navigateToBookDetail
import com.texthip.thip.ui.navigator.routes.MainTabRoutes
import com.texthip.thip.ui.navigator.routes.SearchRoutes
import com.texthip.thip.ui.search.screen.SearchBookScreen
import com.texthip.thip.ui.search.screen.SearchBookDetailScreen

fun NavGraphBuilder.searchNavigation(navController: NavHostController) {
    composable<MainTabRoutes.Search> {
        SearchBookScreen(
            onBookClick = { isbn ->
                navController.navigateToBookDetail(isbn)
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
                // TODO: 모집중인 그룹 화면으로 이동
            },
            onBookMarkClick = { isBookmarked ->
                // TODO: 북마크 액션 구현
            },
            onWriteFeedClick = {
                // TODO: 피드 작성 화면으로 이동
            }
        )
    }
}