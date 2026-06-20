package com.pocketforge.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GLauncherTheme {
                LauncherRoot()
            }
        }
    }
}

/* ------------------------------------------------------------------------- */
/*  Tweak these to customise the launcher.                                   */
/* ------------------------------------------------------------------------- */
private object LauncherConfig {
    const val DOCK_APPS_COMPACT = 5      // dock icons on a phone-width screen
    const val DOCK_APPS_WIDE = 7         // dock icons on an unfolded / tablet screen
    val WIDE_SCREEN_BREAKPOINT = 600.dp  // >= this width counts as "unfolded"
    val DRAWER_ICON_MIN = 76.dp          // min cell width in the all-apps grid
    val DOCK_ICON = 56.dp
    val DRAWER_ICON = 52.dp
}

@Composable
private fun LauncherRoot() {
    val context = LocalContext.current
    val apps by produceState<List<AppInfo>?>(initialValue = null) {
        value = withContext(Dispatchers.IO) { loadApps(context) }
    }
    var drawerOpen by remember { mutableStateOf(false) }

    BackHandler(enabled = drawerOpen) { drawerOpen = false }

    Box(Modifier.fillMaxSize()) {
        HomeScreen(
            apps = apps.orEmpty(),
            onOpenDrawer = { drawerOpen = true },
        )
        AnimatedVisibility(
            visible = drawerOpen,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
        ) {
            AppDrawer(
                apps = apps,
                onClose = { drawerOpen = false },
            )
        }
    }
}

/* ----------------------------- Home screen ------------------------------- */

@Composable
private fun HomeScreen(apps: List<AppInfo>, onOpenDrawer: () -> Unit) {
    val density = LocalDensity.current
    val swipeThreshold = remember(density) { with(density) { 80.dp.toPx() } }
    var dragAmount by remember { mutableStateOf(0f) }

    androidx.compose.foundation.layout.BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = { dragAmount = 0f },
                    onDragEnd = { if (dragAmount < -swipeThreshold) onOpenDrawer() },
                    onVerticalDrag = { _, dy -> dragAmount += dy },
                )
            },
    ) {
        val wide = maxWidth >= LauncherConfig.WIDE_SCREEN_BREAKPOINT
        val dockCount =
            if (wide) LauncherConfig.DOCK_APPS_WIDE else LauncherConfig.DOCK_APPS_COMPACT

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(40.dp))
            AtAGlance()

            Spacer(Modifier.weight(1f))

            SearchPill(onClick = onOpenDrawer)
            Spacer(Modifier.height(20.dp))
            Dock(apps = pickDockApps(apps, dockCount))
            Spacer(Modifier.height(12.dp))
        }
    }
}

/** Pixel-style "At a Glance": a large clock and the date. */
@Composable
private fun AtAGlance() {
    val now by rememberTicker()
    val time = remember { SimpleDateFormat("h:mm", Locale.getDefault()) }
    val date = remember { SimpleDateFormat("EEE, MMM d", Locale.getDefault()) }
    val onWallpaper = TextStyle(
        color = Color.White,
        shadow = Shadow(color = Color.Black.copy(alpha = 0.55f), blurRadius = 12f),
    )

    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = time.format(now.time),
            style = onWallpaper.copy(fontSize = 72.sp, fontWeight = FontWeight.Light),
        )
        Text(
            text = date.format(now.time),
            style = onWallpaper.copy(fontSize = 18.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

@Composable
private fun SearchPill(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.width(14.dp))
            Text(
                text = "Search",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun Dock(apps: List<AppInfo>) {
    if (apps.isEmpty()) return
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            apps.forEach { app ->
                AppIcon(app = app, showLabel = false, iconSize = LauncherConfig.DOCK_ICON)
            }
        }
    }
}

/* ----------------------------- App drawer -------------------------------- */

@Composable
private fun AppDrawer(apps: List<AppInfo>?, onClose: () -> Unit) {
    val density = LocalDensity.current
    val closeThreshold = remember(density) { with(density) { 80.dp.toPx() } }
    var dragAmount by remember { mutableStateOf(0f) }
    var query by remember { mutableStateOf("") }

    val filtered = remember(apps, query) {
        val all = apps.orEmpty()
        if (query.isBlank()) all
        else all.filter { it.label.contains(query.trim(), ignoreCase = true) }
    }

    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.97f),
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = { dragAmount = 0f },
                    onDragEnd = { if (dragAmount > closeThreshold) onClose() },
                    onVerticalDrag = { _, dy -> dragAmount += dy },
                )
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp),
        ) {
            Spacer(Modifier.height(8.dp))
            DrawerSearchField(query = query, onQueryChange = { query = it })
            Spacer(Modifier.height(12.dp))

            if (apps == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading apps…", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(LauncherConfig.DRAWER_ICON_MIN),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(filtered, key = { it.packageName + "/" + it.activityName }) { app ->
                        AppIcon(
                            app = app,
                            showLabel = true,
                            iconSize = LauncherConfig.DRAWER_ICON,
                            onLaunched = onClose,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerSearchField(query: String, onQueryChange: (String) -> Unit) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.width(14.dp))
            Box(Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        text = "Search apps",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(
                        MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

/* --------------------------- Shared app icon ----------------------------- */

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AppIcon(
    app: AppInfo,
    showLabel: Boolean,
    iconSize: androidx.compose.ui.unit.Dp,
    onLaunched: () -> Unit = {},
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(
                onClick = {
                    launchApp(context, app)
                    onLaunched()
                },
                onLongClick = { openAppInfo(context, app.packageName) },
            )
            .padding(vertical = 6.dp, horizontal = 4.dp),
    ) {
        Image(
            painter = BitmapPainter(app.icon),
            contentDescription = app.label,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(iconSize)
                .clip(CircleShape),
        )
        if (showLabel) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = app.label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/* ------------------------------- Helpers --------------------------------- */

/** Recomposes once a second so the clock stays current. */
@Composable
private fun rememberTicker(): State<Calendar> {
    val state = remember { mutableStateOf(Calendar.getInstance()) }
    LaunchedEffect(Unit) {
        while (true) {
            state.value = Calendar.getInstance()
            delay(1000)
        }
    }
    return state
}
