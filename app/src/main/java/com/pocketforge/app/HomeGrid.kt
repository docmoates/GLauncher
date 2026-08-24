package com.pocketforge.app

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

private const val PREFS = "glauncher"
private const val KEY_HOME_APPS = "home_apps"
private val WIDE_SCREEN_BREAKPOINT = 600.dp
private const val COLUMNS_COMPACT = 4
private const val COLUMNS_WIDE = 6
private val HOME_GRID_ICON = 56.dp

/**
 * Holds the ordered list of apps pinned to the Pixel-style home-screen grid,
 * persisted in the "glauncher" SharedPreferences file as a comma-separated
 * list of "package/activity" entries under [KEY_HOME_APPS]. Every mutation
 * persists immediately.
 */
@Stable
class HomeGridState(context: Context) {
    private val appContext = context.applicationContext
    private val prefs = appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    private val _pinned = mutableStateListOf<Pair<String, String>>()

    /** Ordered (packageName, activityName) pairs pinned to the home screen. */
    val pinned: List<Pair<String, String>> get() = _pinned

    init {
        _pinned.addAll(readEntries())
    }

    /** Pins [packageName]/[activityName] to the end of the grid; no-op if already pinned. */
    fun pin(packageName: String, activityName: String) {
        if (isPinned(packageName, activityName)) return
        _pinned.add(packageName to activityName)
        persist()
    }

    /** Removes [packageName]/[activityName] from the grid, if present. */
    fun unpin(packageName: String, activityName: String) {
        if (_pinned.removeAll { it.first == packageName && it.second == activityName }) {
            persist()
        }
    }

    /**
     * Moves the pinned entry at [from] to [to], shifting the others to make room.
     * No-op if either index is out of bounds or they're equal.
     */
    fun move(from: Int, to: Int) {
        if (from == to || from !in _pinned.indices || to !in _pinned.indices) return
        val entry = _pinned.removeAt(from)
        _pinned.add(to, entry)
        persist()
    }

    /** True if [packageName]/[activityName] is currently pinned. */
    fun isPinned(packageName: String, activityName: String): Boolean =
        _pinned.any { it.first == packageName && it.second == activityName }

    /** Silently drops [packageName]/[activityName], e.g. once its app is detected uninstalled. */
    internal fun prune(packageName: String, activityName: String) {
        if (_pinned.removeAll { it.first == packageName && it.second == activityName }) {
            persist()
        }
    }

    private fun persist() {
        val value = _pinned.joinToString(",") { "${it.first}/${it.second}" }
        prefs.edit().putString(KEY_HOME_APPS, value).apply()
    }

    private fun readEntries(): List<Pair<String, String>> =
        prefs.getString(KEY_HOME_APPS, "").orEmpty()
            .split(",")
            .mapNotNull { entry ->
                val slash = entry.indexOf('/')
                if (slash <= 0 || slash == entry.lastIndex) return@mapNotNull null
                entry.substring(0, slash) to entry.substring(slash + 1)
            }
}

/** Remembers a [HomeGridState] scoped to the current [LocalContext]. */
@Composable
fun rememberHomeGridState(): HomeGridState {
    val context = LocalContext.current
    return remember { HomeGridState(context) }
}

/**
 * Pixel-style grid of pinned home-screen apps. Uses 4 columns under the
 * [WIDE_SCREEN_BREAKPOINT] width and 6 columns above it. Renders nothing if
 * [state] has no pinned apps — the caller is expected to show its own hint.
 *
 * Pinned entries that no longer resolve against [apps] (e.g. an uninstalled
 * app) are skipped and pruned from [state] once [apps] has actually loaded.
 */
@Composable
fun HomeIconGrid(
    state: HomeGridState,
    apps: List<AppInfo>,
    modifier: Modifier = Modifier,
) {
    if (state.pinned.isEmpty()) return

    val appsByKey = remember(apps) { apps.associateBy { it.packageName + "/" + it.activityName } }

    // Apps may still be loading (empty list); don't treat that as "uninstalled".
    val missing = if (apps.isEmpty()) {
        emptyList()
    } else {
        state.pinned.filter { (pkg, activity) -> appsByKey[pkg + "/" + activity] == null }
    }
    LaunchedEffect(missing) {
        missing.forEach { (pkg, activity) -> state.prune(pkg, activity) }
    }

    val resolved = if (apps.isEmpty()) {
        emptyList()
    } else {
        state.pinned.mapNotNull { (pkg, activity) ->
            appsByKey[pkg + "/" + activity]?.let { Triple(pkg, activity, it) }
        }
    }
    if (resolved.isEmpty()) return

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val columns = if (maxWidth < WIDE_SCREEN_BREAKPOINT) COLUMNS_COMPACT else COLUMNS_WIDE

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(resolved, key = { it.first + "/" + it.second }) { (pkg, activity, app) ->
                val index = state.pinned.indexOf(pkg to activity)
                HomeGridIcon(state = state, app = app, index = index)
            }
        }
    }
}

/** A single icon cell in [HomeIconGrid]: tap launches, long-press opens [AppLongPressMenu]. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeGridIcon(
    state: HomeGridState,
    app: AppInfo,
    index: Int,
) {
    val context = LocalContext.current
    var menuOpen by remember { mutableStateOf(false) }

    Box {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .combinedClickable(
                    onClick = { launchApp(context, app) },
                    onLongClick = { menuOpen = true },
                )
                .padding(vertical = 6.dp, horizontal = 4.dp),
        ) {
            Image(
                painter = BitmapPainter(app.icon),
                contentDescription = app.label,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(HOME_GRID_ICON)
                    .clip(CircleShape),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = app.label,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Color.White,
                    shadow = Shadow(Color.Black.copy(alpha = 0.55f), blurRadius = 8f),
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }

        AppLongPressMenu(
            expanded = menuOpen,
            onDismiss = { menuOpen = false },
            onAppInfo = { openAppInfo(context, app.packageName) },
            onRemove = { state.unpin(app.packageName, app.activityName) },
            onMoveLeft = { state.move(index, index - 1) },
            onMoveRight = { state.move(index, index + 1) },
            canMoveLeft = index > 0,
            canMoveRight = index in 0 until state.pinned.size - 1,
        )
    }
}

/** Long-press popup for a pinned app icon: info, remove, and reorder actions. */
@Composable
private fun AppLongPressMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onAppInfo: () -> Unit,
    onRemove: () -> Unit,
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    canMoveLeft: Boolean,
    canMoveRight: Boolean,
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        DropdownMenuItem(
            text = { Text("App info") },
            onClick = {
                onDismiss()
                onAppInfo()
            },
        )
        DropdownMenuItem(
            text = { Text("Remove") },
            onClick = {
                onDismiss()
                onRemove()
            },
        )
        if (canMoveLeft) {
            DropdownMenuItem(
                text = { Text("Move left") },
                onClick = {
                    onDismiss()
                    onMoveLeft()
                },
            )
        }
        if (canMoveRight) {
            DropdownMenuItem(
                text = { Text("Move right") },
                onClick = {
                    onDismiss()
                    onMoveRight()
                },
            )
        }
    }
}
