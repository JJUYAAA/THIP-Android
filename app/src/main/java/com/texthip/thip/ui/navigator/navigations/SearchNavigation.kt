package com.texthip.thip.ui.navigator.navigations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.texthip.thip.ui.navigator.extensions.navigateToBookDetail
import com.texthip.thip.ui.navigator.extensions.navigateToBookGroup
import com.texthip.thip.ui.navigator.extensions.navigateToFeedComment
import com.texthip.thip.ui.navigator.extensions.navigateToFeedWrite
import com.texthip.thip.ui.navigator.extensions.navigateToGroupMakeRoomWithBook
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
            onWriteFeedClick = { bookDetail ->
                navController.navigateToFeedWrite(
                    isbn = bookDetail.isbn,
                    bookTitle = bookDetail.title,
                    bookAuthor = bookDetail.authorName,
                    bookImageUrl = bookDetail.imageUrl
                )
            },
            onFeedClick = { feedId ->
                navController.navigateToFeedComment(feedId)
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
            onCreateRoomClick = { isbn, title, imageUrl, author ->
                navController.navigateToGroupMakeRoomWithBook(
                    isbn = isbn,
                    title = title,
                    imageUrl = imageUrl,
                    author = author
                )
            }
        )
    }
}