package com.pocketforge.app

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private const val PREFS = "glauncher"
private const val KEY_DOCK_COLOR = "dock_color"
private const val KEY_DOCK_ALPHA = "dock_alpha"
private const val KEY_DOCK_CORNER = "dock_corner_radius"

/** Sentinel meaning "follow the Material You wallpaper colour" rather than a fixed colour. */
const val DOCK_COLOR_WALLPAPER = 0

private const val DEFAULT_DOCK_ALPHA = 0.35f
private const val DEFAULT_DOCK_CORNER_DP = 28

/** A named colour offered in the dock colour picker. */
data class DockColorOption(val label: String, val argb: Int)

val DOCK_COLOR_OPTIONS: List<DockColorOption> = listOf(
    DockColorOption("Wallpaper", DOCK_COLOR_WALLPAPER),
    DockColorOption("Maroon", 0xFF800000.toInt()),
    DockColorOption("Red", 0xFFB3261E.toInt()),
    DockColorOption("Orange", 0xFFB35C00.toInt()),
    DockColorOption("Green", 0xFF2E6B4F.toInt()),
    DockColorOption("Teal", 0xFF1F6F6F.toInt()),
    DockColorOption("Blue", 0xFF1B4F9C.toInt()),
    DockColorOption("Purple", 0xFF5B3B8C.toInt()),
    DockColorOption("Charcoal", 0xFF2A2A2E.toInt()),
    DockColorOption("White", 0xFFF2F2F5.toInt()),
)

/** Persisted launcher preferences. Everything here is applied live. */
@Stable
class LauncherSettingsState(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    var dockColor by mutableIntStateOf(prefs.getInt(KEY_DOCK_COLOR, DOCK_COLOR_WALLPAPER))
        private set
    var dockAlpha by mutableFloatStateOf(prefs.getFloat(KEY_DOCK_ALPHA, DEFAULT_DOCK_ALPHA))
        private set
    var dockCornerRadiusDp by mutableIntStateOf(prefs.getInt(KEY_DOCK_CORNER, DEFAULT_DOCK_CORNER_DP))
        private set

    fun updateDockColor(argb: Int) {
        dockColor = argb
        prefs.edit().putInt(KEY_DOCK_COLOR, argb).apply()
    }

    fun updateDockAlpha(alpha: Float) {
        val clamped = alpha.coerceIn(0f, 1f)
        dockAlpha = clamped
        prefs.edit().putFloat(KEY_DOCK_ALPHA, clamped).apply()
    }

    fun updateDockCornerRadiusDp(radius: Int) {
        val clamped = radius.coerceIn(0, 48)
        dockCornerRadiusDp = clamped
        prefs.edit().putInt(KEY_DOCK_CORNER, clamped).apply()
    }
}

@Composable
fun rememberLauncherSettings(): LauncherSettingsState {
    val context = LocalContext.current
    return remember { LauncherSettingsState(context) }
}

/**
 * The colour actually used to paint the dock: either the chosen swatch or, for
 * [DOCK_COLOR_WALLPAPER], the Material You surface colour. Alpha is applied here
 * so callers just use it directly.
 */
@Composable
fun LauncherSettingsState.resolvedDockColor(): Color {
    val base = if (dockColor == DOCK_COLOR_WALLPAPER) {
        MaterialTheme.colorScheme.surface
    } else {
        Color(dockColor)
    }
    return base.copy(alpha = dockAlpha)
}

/** Full-screen settings page, opened from the home screen long-press menu. */
@Composable
fun SettingsScreen(settings: LauncherSettingsState, onClose: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Home settings",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f),
                )
                Surface(
                    onClick = onClose,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                ) {
                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            SettingsSectionTitle("Dock")

            DockPreview(settings)

            Spacer(Modifier.height(16.dp))
            SettingsLabel("Colour")
            DockColorPicker(
                selected = settings.dockColor,
                onSelect = settings::updateDockColor,
            )

            Spacer(Modifier.height(20.dp))
            SettingsLabel("Opacity  ${(settings.dockAlpha * 100).roundToInt()}%")
            Slider(
                value = settings.dockAlpha,
                onValueChange = settings::updateDockAlpha,
                valueRange = 0f..1f,
            )

            Spacer(Modifier.height(8.dp))
            SettingsLabel("Corner radius  ${settings.dockCornerRadiusDp}dp")
            Slider(
                value = settings.dockCornerRadiusDp.toFloat(),
                onValueChange = { settings.updateDockCornerRadiusDp(it.roundToInt()) },
                valueRange = 0f..48f,
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SettingsSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 12.dp),
    )
}

@Composable
private fun SettingsLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 6.dp),
    )
}

/** Shows the dock's current styling so changes are visible without leaving settings. */
@Composable
private fun DockPreview(settings: LauncherSettingsState) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                shape = RoundedCornerShape(settings.dockCornerRadiusDp.dp),
                color = settings.resolvedDockColor(),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DockColorPicker(selected: Int, onSelect: (Int) -> Unit) {
    val wallpaperColor = MaterialTheme.colorScheme.surface
    Column {
        DOCK_COLOR_OPTIONS.chunked(5).forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                row.forEach { option ->
                    val swatch = if (option.argb == DOCK_COLOR_WALLPAPER) {
                        wallpaperColor
                    } else {
                        Color(option.argb)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(56.dp),
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(swatch)
                                .border(
                                    width = if (option.argb == selected) 3.dp else 1.dp,
                                    color = if (option.argb == selected) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.outlineVariant
                                    },
                                    shape = CircleShape,
                                )
                                .clickable { onSelect(option.argb) },
                        ) {
                            if (option.argb == selected) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = contrastOn(swatch),
                                )
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = option.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

/** Black or white, whichever stays legible on [background]. */
private fun contrastOn(background: Color): Color {
    val argb = background.toArgb()
    val r = (argb shr 16 and 0xFF) / 255f
    val g = (argb shr 8 and 0xFF) / 255f
    val b = (argb and 0xFF) / 255f
    val luminance = 0.299f * r + 0.587f * g + 0.114f * b
    return if (luminance > 0.6f) Color.Black else Color.White
}
