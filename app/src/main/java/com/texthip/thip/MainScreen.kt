package com.texthip.thip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.texthip.thip.data.repository.NotificationRepository
import com.texthip.thip.ui.navigator.BottomNavigationBar
import com.texthip.thip.ui.navigator.MainNavHost
import com.texthip.thip.ui.navigator.extensions.isMainTabRoute
import com.texthip.thip.ui.navigator.extensions.navigateFromNotification
import com.texthip.thip.ui.navigator.routes.MainTabRoutes
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MainScreenEntryPoint {
    fun notificationRepository(): NotificationRepository
}

@Composable
fun MainScreen(
    onNavigateToLogin: () -> Unit,
    notificationData: MainActivity.NotificationData? = null
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var feedReselectionTrigger by remember { mutableStateOf(0) }
    val context = LocalContext.current
    
    // 처리된 알림 ID 추적 (중복 처리 방지)
    var processedNotificationId by remember { mutableStateOf<String?>(null) }

    // 푸시 알림에서 온 경우 알림 읽기 API 호출 및 네비게이션
    LaunchedEffect(notificationData?.notificationId) {
        val data = notificationData
        
        // 중복 처리 방지: 이미 처리한 알림이면 스킵
        if (data?.notificationId == processedNotificationId) {
            Log.d("MainScreen", "Notification already processed: ${data?.notificationId}")
            return@LaunchedEffect
        }
        
        data?.let { notificationData ->
            if (notificationData.fromNotification && notificationData.notificationId != null) {
                Log.d("MainScreen", "Processing notification: ${notificationData.notificationId}")
                
                try {
                    val entryPoint = EntryPointAccessors.fromApplication(
                        context.applicationContext,
                        MainScreenEntryPoint::class.java
                    )
                    val notificationRepository = entryPoint.notificationRepository()

                    // 알림 ID를 Int로 변환 시도
                    val notificationId = try {
                        notificationData.notificationId.toInt()
                    } catch (e: NumberFormatException) {
                        Log.e("MainScreen", "Invalid notification ID format: ${notificationData.notificationId}", e)
                        return@LaunchedEffect
                    }

                    val result = notificationRepository.checkNotification(notificationId)
                    
                    result.onSuccess { response ->
                        if (response != null) {
                            Log.d("MainScreen", "Notification check successful, navigating to: ${response.route}")
                            navController.navigateFromNotification(response)
                            // 알림 상태 강제 새로고침 트리거
                            notificationRepository.onNotificationReceived()
                            // 처리 완료 표시
                            processedNotificationId = notificationData.notificationId
                        } else {
                            Log.w("MainScreen", "Notification check returned null response")
                        }
                    }.onFailure { exception ->
                        Log.e("MainScreen", "Failed to check notification: ${notificationData.notificationId}", exception)
                    }
                    
                } catch (e: Exception) {
                    Log.e("MainScreen", "Unexpected error processing notification: ${notificationData.notificationId}", e)
                }
            }
        }
    }

    val showBottomBar = currentDestination?.isMainTabRoute() ?: true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    onTabReselected = { route ->
                        when (route) {
                            MainTabRoutes.Feed -> {
                                feedReselectionTrigger += 1
                            }

                            else -> {
                                // 다른 탭들은 향후 확장 가능
                            }
                        }
                    }
                )
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MainNavHost(
                navController = navController,
                onNavigateToLogin = onNavigateToLogin,
                onFeedTabReselected = feedReselectionTrigger
            )
        }
    }
}