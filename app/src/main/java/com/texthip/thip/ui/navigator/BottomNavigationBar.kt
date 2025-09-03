package com.texthip.thip.ui.navigator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.texthip.thip.ui.navigator.data.NavBarItems
import com.texthip.thip.ui.navigator.extensions.isRoute
import com.texthip.thip.ui.navigator.extensions.navigateToTab
import com.texthip.thip.ui.navigator.routes.MainTabRoutes
import com.texthip.thip.ui.theme.ThipTheme.colors
import com.texthip.thip.ui.theme.ThipTheme.typography

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    onTabReselected: ((MainTabRoutes) -> Unit)? = null
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val greyColor = colors.Grey02

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(73.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 12.dp,
                    topEnd = 12.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
            .background(colors.Black)
            .drawBehind {
                val cornerRadius = 12.dp.toPx()
                val strokeWidth = 2.dp.toPx()

                val path = Path().apply {
                    // 좌상단 모서리부터 시작
                    moveTo(0f, cornerRadius)
                    arcTo(
                        rect = androidx.compose.ui.geometry.Rect(
                            left = 0f,
                            top = 0f,
                            right = cornerRadius * 2,
                            bottom = cornerRadius * 2
                        ),
                        startAngleDegrees = 180f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                    lineTo(size.width - cornerRadius, 0f)
                    arcTo(
                        rect = androidx.compose.ui.geometry.Rect(
                            left = size.width - cornerRadius * 2,
                            top = 0f,
                            right = size.width,
                            bottom = cornerRadius * 2
                        ),
                        startAngleDegrees = 270f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                }

                drawPath(
                    path = path,
                    color = greyColor,
                    style = Stroke(width = strokeWidth)
                )
            },
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarItems.BarItems.forEach { item ->
                val isSelected = currentDestination?.isRoute(item.route) == true
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = if (isSelected) item.selectedIconRes else item.iconRes),
                            contentDescription = stringResource(item.titleRes),
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(item.titleRes),
                            style = typography.navi_m500_s10
                        )
                    },
                    selected = isSelected,
                    onClick = {
                        if (isSelected) {
                            onTabReselected?.invoke(item.route)
                        } else {
                            navController.navigateToTab(item.route)
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = colors.Purple,
                        unselectedIconColor = colors.Grey02,
                        selectedTextColor = colors.Purple,
                        unselectedTextColor = colors.Grey02
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun BottomNavigationBarPreview() {
    val navController = rememberNavController()
    BottomNavigationBar(
        navController = navController
    )
}