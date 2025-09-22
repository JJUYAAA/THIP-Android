package com.texthip.thip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.texthip.thip.ui.navigator.BottomNavigationBar
import com.texthip.thip.ui.navigator.MainNavHost
import com.texthip.thip.ui.navigator.extensions.isMainTabRoute
import com.texthip.thip.ui.navigator.routes.MainTabRoutes

@Composable
fun MainScreen(
    onNavigateToLogin: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var feedReselectionTrigger by remember { mutableStateOf(0) }

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