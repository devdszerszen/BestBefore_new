package pl.dszerszen.bestbefore.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    //primary
    primary = Color(104, 159, 56, 255),
    primaryVariant = Color(25, 118, 210, 255),
    onPrimary = Color(56, 142, 60, 255),
    //secondary
    secondary = Color(245, 124, 0, 255),
    onSecondary = Color(211, 47, 47, 255),
    //background
    background = Color.White,
    onBackground = Color.Black,
    //surface
    surface = Color.White,
    onSurface = Color.Black,
    //error
    error = Color.Red
)

private val DarkColorPalette = darkColors(
    //primary
    primary = Color(0, 77, 64, 255),
    primaryVariant = Color(49, 27, 146, 255),
    onPrimary = Color(104, 159, 56, 255),
    //secondary
    secondary = Color(191, 54, 12, 255),
    onSecondary = Color(255, 179, 0, 255),
    //background
    background = Color.Black,
    onBackground = Color.White,
    //surface
    surface = Color.Black,
    onSurface = Color.White,
    //error
    error = Color.Red
)

@Composable
fun BestBeforeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    CompositionLocalProvider(
        LocalDimens provides Dimens()
    ) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = { Surface(modifier = Modifier.fillMaxSize(), content = content) }
        )
    }
}