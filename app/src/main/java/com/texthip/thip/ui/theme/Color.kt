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

val Literature = Color(0xFFA0F8E8)
val LiteratureSub = Color(0xFF4FD9C0)
val SocialScience = Color(0xFFFDB770)
val SocialScienceSub = Color(0xFFFF8B17)
val Humanities = Color(0xFFA1D5FF)
val HumanitiesSub = Color(0xFF6DB5EE)
val Art = Color(0xFFFF8BAC)
val ArtSub = Color(0xFFFB5A88)
val ScienceIt = Color(0xFFC8A5FF)
val ScienceItSub = Color(0xFFA76FFF)
val Yellow = Color(0xFFFFECA7)
val KakaoYellow = Color(0xFFFEE500)

val PureWhite = Color(0xFFFFFFFF)
val White = Color(0xFFFEFEFE)

val Grey50 = Color(0x80C4C4C4)
val Grey = Color(0xFFDADADA)
val Grey01 = Color(0xFFADADAD)
val Grey02 = Color(0xFF888888)
val Grey03 = Color(0xFF525252)
val DarkGrey = Color(0xFF3D3D3D)
val DarkGrey50 = Color(0x803D3D3D)
val DarkGrey03 = Color(0xFF1D1D1D)
val DarkGrey02 = Color(0xFF282828)
val DarkGrey01 = Color(0x4B4B4B4B)
val Black = Color(0xFF121212)
val Black50 = Color(0x80121212)
val Black10 = Color(0x1A121212)
val Black00 = Color(0x00121212)
val Black700 = Color(0xFF090909)
val Black800 = Color(0xFF040404)



@Immutable
data class ThipColors(
    val Purple: Color,
    val PurpleSub: Color,
    val PurpleDark: Color,
    val NeonGreen: Color,
    val NeonGreen50: Color,
    val Red: Color,
    val Literature: Color,
    val LiteratureSub: Color,
    val SocialScience: Color,
    val SocialScienceSub: Color,
    val Humanities: Color,
    val HumanitiesSub: Color,
    val Art: Color,
    val ArtSub: Color,
    val ScienceIt: Color,
    val ScienceItSub: Color,
    val Yellow: Color,
    val KakaoYellow: Color,
    val PureWhite: Color,
    val White: Color,
    val Grey50: Color,
    val Grey: Color,
    val Grey01: Color,
    val Grey02: Color,
    val Grey03: Color,
    val DarkGrey: Color,
    val darkGray01: Color,
    val DarkGrey50: Color,
    val DarkGrey03: Color,
    val DarkGrey02: Color,
    val Black: Color,
    val Black50: Color,
    val Black10: Color,
    val Black00: Color,
    val Black800: Color,
    val Black700: Color
)

val defaultThipColors = ThipColors(
    Purple = Purple,
    PurpleSub = PurpleSub,
    PurpleDark = PurpleDark,
    NeonGreen = NeonGreen,
    NeonGreen50 = NeonGreen50,
    Red = Red,
    Literature = Literature,
    LiteratureSub = LiteratureSub,
    SocialScience = SocialScience,
    SocialScienceSub = SocialScienceSub,
    Humanities = Humanities,
    HumanitiesSub = HumanitiesSub,
    Art = Art,
    ArtSub = ArtSub,
    ScienceIt = ScienceIt,
    ScienceItSub = ScienceItSub,
    Yellow = Yellow,
    KakaoYellow = KakaoYellow,
    PureWhite = PureWhite,
    White = White,
    Grey50 = Grey50,
    Grey = Grey,
    Grey01 = Grey01,
    Grey02 = Grey02,
    Grey03 = Grey03,
    DarkGrey = DarkGrey,
    darkGray01 = DarkGrey01,
    DarkGrey50 = DarkGrey50,
    DarkGrey03 = DarkGrey03,
    DarkGrey02 = DarkGrey02,
    Black = Black,
    Black50 = Black50,
    Black10 = Black10,
    Black00 = Black00,
    Black700 = Black700,
    Black800 = Black800
)

val LocalThipColorsProvider = staticCompositionLocalOf { defaultThipColors }