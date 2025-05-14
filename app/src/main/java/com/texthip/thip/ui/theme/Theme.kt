package com.texthip.thip.ui.theme

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.texthip.thip.ui.theme.ThipTheme.colors

object ThipTheme {
    val colors: ThipColors
        @Composable
        @ReadOnlyComposable
        get() = LocalThipColorsProvider.current

    val typography: ThipTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalThipTypographyProvider.current
}

@Composable
fun ProvideThipColorsAndTypography(
    colors: ThipColors,
    typography: ThipTypography,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalThipColorsProvider provides colors,
        LocalThipTypographyProvider provides typography,
        content = content
    )
}

@Composable
fun ThipTheme(
    content: @Composable () -> Unit
) {
    ProvideThipColorsAndTypography (
        colors = defaultThipColors,
        typography = defaultThipTypography
    ) {
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                (view.context as Activity).window.run {
                    WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = false
                }
            }
        }
        MaterialTheme(
            content = {
                Box(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxSize()
                        .background(colors.Black)
                ) {
                    content()
                }
            }
        )
    }
}
