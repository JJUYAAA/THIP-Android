package com.texthip.thip.ui.navigator.extensions

import androidx.navigation.NavHostController
import com.texthip.thip.ui.navigator.data.Routes


// MyPage 관련 네비게이션 확장 함수들

fun NavHostController.navigateToMyPage() {
    navigate(Routes.MyPage)
}
