package ls.android.chatapp.presentation.ui

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = IceBlue,
    secondary = DarkBlue,
    tertiary = DarkGray
)

private val LightColorScheme = lightColorScheme(
    primary = IceBlue,
    secondary = DarkBlue,
    tertiary = DarkGray

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val CustomDarkColorScheme = CustomPalette(
    primary = IceBlue,
    secondary = DarkBlue,
    tertiary = DarkGray,
    textColor = Color.White,
    background = Color(0xFF333333),
    privateModeOutline = Color(0xFF444444)
)

private val CustomLightColorScheme = CustomPalette(
    primary = IceBlue,
    secondary = DarkBlue,
    tertiary = DarkGray,
    textColor = Color.Black,
    background = Color(0xFFDEDEDE),
    privateModeOutline = Color(0xFFECECEC)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ChatAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    val palette = if (darkTheme) {
        CustomDarkColorScheme
    } else {
        CustomLightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                !darkTheme
        }
    }

    CompositionLocalProvider(LocalCustomColorsPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
