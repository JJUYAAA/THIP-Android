package com.texthip.thip.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.texthip.thip.R

val thipFontBold = FontFamily(Font(R.font.bold))
val thipFontSemiBold = FontFamily(Font(R.font.semibold))
val thipFontMedium = FontFamily(Font(R.font.medium))
val thipFontRegular = FontFamily(Font(R.font.regular))

@Immutable
data class ThipTypography(
    val bigtitle_b700_s22_h24: TextStyle,
    val title_b700_s20_h24: TextStyle,
    val navipressed_b700_s10: TextStyle,
    val smalltitle_sb600_s18_h24: TextStyle,
    val smalltitle_sb600_s16_h24: TextStyle,
    val smalltitle_sb600_s16_h20: TextStyle,
    val menu_sb600_s14_h24: TextStyle,
    val menu_sb600_s12_h20: TextStyle,
    val menu_sb600_s12: TextStyle,
    val smalltitle_m500_s18_h24: TextStyle,
    val menu_m500_s16_h24: TextStyle,
    val menu_m500_s14_h24: TextStyle,
    val copy_m500_s14_h20: TextStyle,
    val view_m500_s14: TextStyle,
    val view_m500_s12_h20: TextStyle,
    val info_m500_s12: TextStyle,
    val navi_m500_s10: TextStyle,
    val menu_r400_s14_h24: TextStyle,
    val feedcopy_r400_s14_h20: TextStyle,
    val copy_r400_s14: TextStyle,
    val info_r400_s12_h24: TextStyle,
    val copy_r400_s12_h20: TextStyle,
    val info_r400_s12: TextStyle,
    val view_r400_s11_h20: TextStyle,
    val timedate_r400_s11: TextStyle,
)

val defaultThipTypography = ThipTypography(
    bigtitle_b700_s22_h24 = TextStyle(
        fontFamily = thipFontBold,
        fontSize = 22.sp,
        lineHeight = 24.sp,
    ),
    title_b700_s20_h24 = TextStyle(
        fontFamily = thipFontBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
    ),
    navipressed_b700_s10 = TextStyle(
        fontFamily = thipFontBold,
        fontSize = 10.sp,
    ),
    smalltitle_sb600_s18_h24 = TextStyle(
        fontFamily = thipFontSemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    smalltitle_sb600_s16_h24 = TextStyle(
        fontFamily = thipFontSemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    smalltitle_sb600_s16_h20 = TextStyle(
        fontFamily = thipFontSemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    ),
    menu_sb600_s14_h24 = TextStyle(
        fontFamily = thipFontSemiBold,
        fontSize = 14.sp,
        lineHeight = 24.sp,
    ),
    menu_sb600_s12_h20 = TextStyle(
        fontFamily = thipFontSemiBold,
        fontSize = 12.sp,
        lineHeight = 20.sp,
    ),
    menu_sb600_s12 = TextStyle(
        fontFamily = thipFontSemiBold,
        fontSize = 12.sp,
    ),
    smalltitle_m500_s18_h24 = TextStyle(
        fontFamily = thipFontMedium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    menu_m500_s16_h24 = TextStyle(
        fontFamily = thipFontMedium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    menu_m500_s14_h24 = TextStyle(
        fontFamily = thipFontMedium,
        fontSize = 14.sp,
        lineHeight = 24.sp,
    ),
    copy_m500_s14_h20 = TextStyle(
        fontFamily = thipFontMedium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    view_m500_s14 = TextStyle(
        fontFamily = thipFontMedium,
        fontSize = 14.sp,
    ),
    view_m500_s12_h20 = TextStyle(
        fontFamily = thipFontMedium,
        fontSize = 12.sp,
        lineHeight = 20.sp,
    ),
    info_m500_s12 = TextStyle(
        fontFamily = thipFontMedium,
        fontSize = 12.sp,
    ),
    navi_m500_s10 = TextStyle(
        fontFamily = thipFontMedium,
        fontSize = 10.sp,
    ),
    menu_r400_s14_h24 = TextStyle(
        fontFamily = thipFontRegular,
        fontSize = 14.sp,
        lineHeight = 24.sp,
    ),
    feedcopy_r400_s14_h20 = TextStyle(
        fontFamily = thipFontRegular,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    copy_r400_s14 = TextStyle(
        fontFamily = thipFontRegular,
        fontSize = 14.sp,
    ),
    info_r400_s12_h24 = TextStyle(
        fontFamily = thipFontRegular,
        fontSize = 12.sp,
        lineHeight = 24.sp,
    ),
    copy_r400_s12_h20 = TextStyle(
        fontFamily = thipFontRegular,
        fontSize = 12.sp,
        lineHeight = 20.sp,
    ),
    info_r400_s12 = TextStyle(
        fontFamily = thipFontRegular,
        fontSize = 12.sp,
    ),
    view_r400_s11_h20 = TextStyle(
        fontFamily = thipFontRegular,
        fontSize = 11.sp,
        lineHeight = 20.sp,
    ),
    timedate_r400_s11 = TextStyle(
        fontFamily = thipFontRegular,
        fontSize = 11.sp,
    ),
)

val LocalThipTypographyProvider = staticCompositionLocalOf { defaultThipTypography }