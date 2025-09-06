package com.freelancekc.fdjtechnicaltest.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// We are not handling darkTheme here to simplify the code.
// I still left the code here to show how to handle darkTheme
// However, it is a good UX practice to handle it.

//private val DarkColorScheme = darkColorScheme(
//    primary = primaryBlue,
//    secondary = secondaryBlue,
//    tertiary = tertiaryGray
//)

private val ColorScheme = lightColorScheme(
    primary = primaryBlue,
    secondary = secondaryBlue,
    tertiary = tertiaryGray
)

@Composable
fun FdjTechnicalTestTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }

    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}
