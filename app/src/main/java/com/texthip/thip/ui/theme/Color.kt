package com.texthip.thip.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple = Color(0xFF6868FF)
val PurpleSub = Color(0xFFA1A1FF)
val PurpleDark = Color(0xFF414194)

val NeonGreen = Color(0xFFA7FFB4)
val NeonGreen50 = Color(0x80A7FFB4)

val Red = Color(0xFFFF9496)

val PureWhite = Color(0xFFFFFFFF)
val White = Color(0xFFFEFEFE)

val Grey50 = Color(0x80C4C4C4)
val Grey = Color(0xFFDADADA)
val Grey01 = Color(0xFFADADAD)
val Grey02 = Color(0xFF888888)
val Grey03 = Color(0xFF525252)
val DarkGrey = Color(0xFF3D3D3D)
val DarkGrey50 = Color(0x803D3D3D)
val DarkGrey02 = Color(0xFF282828)

val Black = Color(0xFF121212)
val Black50 = Color(0x80121212)
val Black10 = Color(0x1A121212)
val Black00 = Color(0x00121212)

@Immutable
data class ThipColors(
    val Purple: Color,
    val PurpleSub: Color,
    val PurpleDark: Color,
    val NeonGreen: Color,
    val NeonGreen50: Color,
    val Red: Color,
    val PureWhite: Color,
    val White: Color,
    val Grey50: Color,
    val Grey: Color,
    val Grey01: Color,
    val Grey02: Color,
    val Grey03: Color,
    val DarkGrey: Color,
    val DarkGrey50: Color,
    val DarkGrey02: Color,
    val Black: Color,
    val Black50: Color,
    val Black10: Color,
    val Black00: Color,
)

val defaultThipColors = ThipColors(
    Purple = Purple,
    PurpleSub = PurpleSub,
    PurpleDark = PurpleDark,
    NeonGreen = NeonGreen,
    NeonGreen50 = NeonGreen50,
    Red = Red,
    PureWhite = PureWhite,
    White = White,
    Grey50 = Grey50,
    Grey = Grey,
    Grey01 = Grey01,
    Grey02 = Grey02,
    Grey03 = Grey03,
    DarkGrey = DarkGrey,
    DarkGrey50 = DarkGrey50,
    DarkGrey02 = DarkGrey02,
    Black = Black,
    Black50 = Black50,
    Black10 = Black10,
    Black00 = Black00
)

val LocalThipColorsProvider = staticCompositionLocalOf { defaultThipColors }