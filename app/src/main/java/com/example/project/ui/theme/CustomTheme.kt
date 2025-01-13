package com.example.project.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val darkScheme = ColorScheme(
    background = OrangeDeep,
onBackground = LightGreen,
primary = Orange,
onPrimary = Dark,
secondary = LightGreen,
onSecondary = Green
)

val lightScheme = ColorScheme(
    background = LightOrange,
    onBackground = OrangeDeep,
    primary = Orange,
    onPrimary = Dark,
    secondary = LightGreen,
    onSecondary = Green
)

private val typography = CustomTypography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    titleNormal = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    body = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    labelNormal = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp
    )
)

private val shape = CustomShape(
    container = RoundedCornerShape(12.dp),
    button = RoundedCornerShape(50)
)

private val size = Size(
    large = 24.dp,
    medium = 16.dp,
    normal = 12.dp,
    small = 8.dp
)

@Composable
fun CustomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
){
    val colorScheme = if (isSystemInDarkTheme()) darkScheme else lightScheme
    val rippleIndication = rememberRipple()
    CompositionLocalProvider (
        LocalColorScheme provides colorScheme,
        LocalTypography provides typography,
        LocalCustonShape provides shape,
        LocalSize provides size,
        LocalIndication provides rippleIndication,
        content = content
    )
}

object CustomTheme {
    val colorScheme: ColorScheme
        @Composable get() = LocalColorScheme.current

    val typography: CustomTypography
        @Composable get() = LocalTypography.current

    val shape: CustomShape
        @Composable get() = LocalCustonShape.current

    val size: Size
        @Composable get() = LocalSize.current

}
