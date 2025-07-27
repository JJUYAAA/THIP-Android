package com.texthip.thip.ui.navigator.navigations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.texthip.thip.ui.common.alarmpage.screen.AlarmScreen
import com.texthip.thip.ui.navigator.routes.CommonRoutes
import com.texthip.thip.ui.navigator.extensions.navigateBack

// Common 관련 네비게이션
fun NavGraphBuilder.commonNavigation(navController: NavHostController) {
    // Alarm 화면
    composable<CommonRoutes.Alarm> {
        AlarmScreen(
            alarmItems = emptyList(), // TODO: ViewModel에서 가져오기
            onCardClick = { /* TODO: 알림 카드 클릭 처리 */ },
            onNavigateBack = {
                navController.navigateBack()
            }
        )
    }
}