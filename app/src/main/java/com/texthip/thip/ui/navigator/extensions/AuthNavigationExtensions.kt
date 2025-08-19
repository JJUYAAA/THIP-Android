package com.texthip.thip.ui.navigator.extensions

import androidx.navigation.NavHostController
import com.texthip.thip.ui.navigator.routes.CommonRoutes

/**
 * 스플래시 화면을 지우고 로그인 화면으로 이동
 */
fun NavHostController.navigateToLogin() {
    navigate(CommonRoutes.Login) {
        popUpTo(CommonRoutes.Splash) { inclusive = true }
    }
}

/**
 * 닉네임 설정(회원가입) 화면으로 이동
 */
fun NavHostController.navigateToSignup() {
    navigate(CommonRoutes.SignupFlow)
}

/**
 * 장르 선택(회원가입) 화면으로 이동
 */
fun NavHostController.navigateToSignupGenre() {
    navigate(CommonRoutes.Genre)
}


/**
 * 회원가입 성공 후, 인증 과정을 모두 지우고 메인 화면으로 이동
 */
fun NavHostController.navigateToMainAfterSignup() {
    navigate(CommonRoutes.Main) {
        // 인증과 관련된 모든 화면을 백스택에서 제거
        popUpTo(CommonRoutes.Login) {
            inclusive = true
        }
    }
}