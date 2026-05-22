package app.krafted.jokersjuggle.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Gold,
    secondary = Burgundy,
    tertiary = MarqueeDim,
    background = StageDark,
    surface = DeepStage,
    onPrimary = StageDark,
    onSecondary = CreamText,
    onTertiary = CreamText,
    onBackground = CreamText,
    onSurface = CreamText
)

@Composable
fun JokersJuggleTheme(
    darkTheme: Boolean = true, // Force dark theme by default
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = StageDark.toArgb()
            window.navigationBarColor = StageDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}