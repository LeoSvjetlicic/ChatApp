package ls.android.chatapp.presentation.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import javax.annotation.concurrent.Immutable

@Immutable
data class CustomPalette(
    val primary: Color = Color.Unspecified,
    val secondary: Color = Color.Unspecified,
    val tertiary: Color = Color.Unspecified,
    val textColor: Color = Color.Unspecified,
    val background: Color = Color.Unspecified,
    val privateModeOutline: Color = Color.Unspecified,
)

val LocalCustomColorsPalette = staticCompositionLocalOf { CustomPalette() }
