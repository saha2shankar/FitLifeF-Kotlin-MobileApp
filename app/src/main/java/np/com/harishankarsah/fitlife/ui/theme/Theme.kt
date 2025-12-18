package np.com.harishankarsah.fitlife.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import np.com.harishankarsah.fitlife.ui.components.dialog.GlobalDialog

// Light Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryVariant,
    secondary = Secondary,
    onSecondary = OnSecondary,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    error = Error,
    onError = OnError,
)

// Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryVariant,
    onPrimary = OnPrimary,
    primaryContainer = Primary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    error = Error,
    onError = OnError,
)

@Composable
fun FitLifeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes
    ) {
        Box {
            content()       // <- main UI
            GlobalDialog()  // <- always on top
        }
    }
}
