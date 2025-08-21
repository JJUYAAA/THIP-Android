package com.texthip.thip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.texthip.thip.data.manager.TokenManager
import com.texthip.thip.ui.navigator.navigations.authNavigation
import com.texthip.thip.ui.navigator.routes.CommonRoutes
import com.texthip.thip.ui.theme.ThipTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThipTheme {
                RootNavHost()
            }
        }
//        getKakaoKeyHash(this)
    }
}

@Composable
fun RootNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = CommonRoutes.Splash
    ) {
        // --- 인증 관련 화면들 ---
        authNavigation(
            onNavigateToLogin = {
                navController.navigate(CommonRoutes.Login) {
                    popUpTo(CommonRoutes.Splash) { inclusive = true }
                }
            },
            onNavigateToHome = {
                navController.navigate(CommonRoutes.Main) {
                    popUpTo(CommonRoutes.Splash) { inclusive = true }
                }
            },
            onNavigateToSignup = {
                navController.navigate(CommonRoutes.SignupFlow)
            },
            onNavigateToMainAfterSignup = {
                navController.navigate(CommonRoutes.Main) { // 혹은 MainGraph
                    popUpTo(CommonRoutes.Splash) { inclusive = true }
                }
            },
            navController = navController
        )


        // --- 메인 관련 화면들 ---
        composable<CommonRoutes.Main> { // MainScreen으로 가는 경로 추가
            MainScreen(
                onNavigateToLogin = {
                    navController.navigate(CommonRoutes.Login) {
                        // 메인 화면으로 돌아올 수 없도록 모든 화면 기록 삭제
                        popUpTo(CommonRoutes.Main) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}