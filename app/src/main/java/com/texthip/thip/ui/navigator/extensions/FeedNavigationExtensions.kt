package com.texthip.thip.ui.navigator.extensions

import androidx.navigation.NavHostController
import com.texthip.thip.ui.navigator.data.Routes

// Feed 확장 함수
fun NavHostController.navigateToFeed() {
    navigate(Routes.Feed)
}
