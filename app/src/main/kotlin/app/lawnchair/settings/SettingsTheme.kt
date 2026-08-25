package app.lawnchair.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Section accent colors — each settings category gets its own identity color
object SectionColors {
    val Home = Color(0xFF5C8A5C)      // sage green
    val Appearance = Color(0xFFA87CAF) // muted purple
    val Dock = Color(0xFF6B8CAE)       // slate blue
    val Apps = Color(0xFFC08A4A)       // warm amber
    val Gestures = Color(0xFFC26B7C)   // dusty rose
    val More = Color(0xFF6B6B7C)       // neutral slate
}

private val LightColors = lightColorScheme(
    primary = Color(0xFF4E7A4E),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDCEBDC),
    onPrimaryContainer = Color(0xFF1B321B),
    secondary = Color(0xFF8A715C),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFFAF7F2),
    onBackground = Color(0xFF2B2620),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF2B2620),
    surfaceVariant = Color(0xFFF0EBE2),
    onSurfaceVariant = Color(0xFF5C5648),
    outline = Color(0xFFDED5C4),
    outlineVariant = Color(0xFFEBE4D6),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9CC49C),
    onPrimary = Color(0xFF1B321B),
    primaryContainer = Color(0xFF345334),
    onPrimaryContainer = Color(0xFFDCEBDC),
    secondary = Color(0xFFD4B9A0),
    onSecondary = Color(0xFF3D2F24),
    background = Color(0xFF1C1A16),
    onBackground = Color(0xFFEAE4D9),
    surface = Color(0xFF262320),
    onSurface = Color(0xFFEAE4D9),
    surfaceVariant = Color(0xFF332F29),
    onSurfaceVariant = Color(0xFFCBC2B4),
    outline = Color(0xFF4A453C),
    outlineVariant = Color(0xFF3A362F),
)

@Composable
fun SettingsTheme(darkTheme: Boolean = androidx.compose.foundation.isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = MaterialTheme.typography,
        content = content
    )
}
