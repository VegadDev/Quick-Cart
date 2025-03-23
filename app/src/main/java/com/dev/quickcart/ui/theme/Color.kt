package com.dev.quickcart.ui.theme

import androidx.compose.ui.graphics.Color


data class AppColors(

    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val error: Color,
    val onError: Color,
    val outline: Color,
    val background: Color,
    val onBackground: Color,
    val textColorLight: Color,
    val textColorMedium: Color,
    val textColorDark: Color,
    val cardBackgroundColor: Color,
    val lightGray: Color,
    val titleText: Color,
    val googleButtonColor: Color,
    val otpCard: Color,
    val black: Color,
    val borderGray: Color,
    val minusGray: Color,
    val darkGreen: Color = Color(0xFF005418),
    val cardShadow: Color,
    val billCardBg: Color,
    val billCardText: Color,
    val billCardIcon: Color,
    val divider: Color,
)


val customAppColorsLight = AppColors(
    cardShadow = Color(0x333A4A5A),
    primary = Color(0xFF23AA49),
    onPrimary = Color(0xFFFAFAFA),
    secondary = Color(0xFFFF324B),
    onSecondary = Color(0xFFFFFFFF),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    outline = Color(0xFF74777F),
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF001F25),
    textColorLight = Color(0xFF979899),
    textColorMedium = Color(0xFF2F2F2F),
    textColorDark = Color(0xFF06161C),
    cardBackgroundColor = Color(0xFFF3F5F7),
    lightGray = Color(0xFF979899),
    titleText = Color(0xFF06161C),
    googleButtonColor = Color(0xFF5383EC),
    otpCard = Color(0xFFF0F1F2),
    black = Color(0xFF0D0D0D),
    borderGray = Color(0xFF1A2525),
    minusGray = Color(0xFFB3B3B3),
    darkGreen = Color(0xFF005418),
    billCardBg = Color(0xFFF5F8FF),
    billCardText = Color(0xFF333333),
    billCardIcon = Color(0xFF4CAF50),
    divider = Color(0xFFE0E0E0),

)



val customAppColorsDark = AppColors(
    cardShadow = Color(0x4D8090A0),
    primary = Color(0xFF23AA49),
    onPrimary = Color(0xFFFAFAFA),
    secondary = Color(0xFFFF324B),
    onSecondary = Color(0xFF3F2E00),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    outline = Color(0xFF8D9199),
    background = Color(0xFF06161C),
    onBackground = Color(0xFFA6EEFF),
    textColorLight = Color(0xFF828282),
    textColorMedium = Color(0xFFCDCDCD),
    textColorDark = Color(0xFFFFFFFF),
    cardBackgroundColor = Color(0xFF1A3848),
    lightGray = Color(0xFF617986),
    titleText = Color(0xFFFAFAFA),
    googleButtonColor = Color(0xFF5383EC),
    otpCard = Color(0xFF1A3848),
    black = Color(0xFFFAF9F6),
    borderGray = Color(0xFF2F2F2F),
    minusGray = Color(0xFFF5FEFD),
    darkGreen = Color(0xFF005418),
    billCardBg = Color(0xFF232733),
    billCardText = Color(0xFFE0E0E0),
    billCardIcon = Color(0xFF66BB6A),
    divider = Color(0xFF3A3F4B),


)