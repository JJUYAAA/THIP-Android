package com.texthip.thip.utils.type

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.texthip.thip.ui.theme.ThipTheme.colors

enum class GenreColor(
    val colorProvider: @Composable () -> Color
) {
    RED({ colors.Art }),
    BLUE({ colors.Humanities }),
    GREEN({ colors.Literature }),
    YELLOW({ colors.SocialScience }),
    PURPLE({ colors.ScienceIt });


    companion object {
        fun fromString(colorString: String?): GenreColor {
            return entries.find { it.name.equals(colorString, ignoreCase = true) }
                ?: RED
        }
    }
}