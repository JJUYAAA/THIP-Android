package com.texthip.thip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.texthip.thip.ui.navigator.BottomNavigationBar
import com.texthip.thip.ui.navigator.MainNavHost
import com.texthip.thip.ui.navigator.data.Routes

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val mainTabRoutes = setOf(
        Routes.Feed::class.qualifiedName,
        Routes.Group::class.qualifiedName,
        Routes.Search::class.qualifiedName,
        Routes.MyPage::class.qualifiedName
    )
    val showBottomBar = currentDestination?.route?.let { route ->
        mainTabRoutes.contains(route)
    } ?: true

    Scaffold(
        bottomBar = {
            if (showBottomBar) BottomNavigationBar(navController)
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MainNavHost(navController)
        }
    }
}