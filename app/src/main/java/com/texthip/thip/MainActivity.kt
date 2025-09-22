package com.texthip.thip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.texthip.thip.data.manager.AuthStateManager
import com.texthip.thip.data.manager.TokenManager
import com.texthip.thip.ui.navigator.navigations.authNavigation
import com.texthip.thip.ui.navigator.routes.CommonRoutes
import com.texthip.thip.ui.theme.ThipTheme
import com.texthip.thip.utils.permission.NotificationPermissionUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var authStateManager: AuthStateManager

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // 앱 시작 시 알림 권한 요청
        requestNotificationPermissionIfNeeded()
        
        setContent {
            ThipTheme {
                RootNavHost(authStateManager)
            }
        }
//        getKakaoKeyHash(this)
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (NotificationPermissionUtils.shouldRequestNotificationPermission(this)) {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

@Composable
fun RootNavHost(authStateManager: AuthStateManager) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        authStateManager.tokenExpiredEvent.collectLatest {
            navController.navigate(CommonRoutes.Login) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

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