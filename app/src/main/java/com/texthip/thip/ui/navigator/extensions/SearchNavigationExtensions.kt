package com.texthip.thip.ui.navigator.extensions

import androidx.navigation.NavHostController
import com.texthip.thip.ui.navigator.data.Routes


// Search 관련 네비게이션 확장 함수들
fun NavHostController.navigateToSearch() {
    navigate(Routes.Search)
}