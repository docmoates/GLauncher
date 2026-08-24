package com.pocketforge.app

import android.appwidget.AppWidgetHost
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.view.WindowCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MainActivity : ComponentActivity() {
    /** Owns every widget id we place on the home screen. */
    private lateinit var widgetHost: AppWidgetHost

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        widgetHost = LauncherWidgetHost(applicationContext, WIDGET_HOST_ID)
        // The home screen floats over the wallpaper, so force *light* status and
        // navigation bar icons. The default "auto" style follows the system
        // light/dark setting and paints them black on a dark wallpaper.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )
        setContent {
            GLauncherTheme {
                LauncherRoot(widgetHost)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        runCatching { widgetHost.startListening() }
    }

    override fun onStop() {
        super.onStop()
        runCatching { widgetHost.stopListening() }
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
private fun LauncherRoot(widgetHost: AppWidgetHost) {
    val context = LocalContext.current
    val apps by produceState<List<AppInfo>?>(initialValue = null) {
        value = withContext(Dispatchers.IO) { loadApps(context) }
    }
    val widgets = rememberWidgetState(widgetHost)
    val homeGrid = rememberHomeGridState()
    val swipe = rememberAllAppsSwipeState()
    val settings = rememberLauncherSettings()
    var settingsOpen by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // White bar icons over the wallpaper; in the app drawer follow the theme so
    // they stay readable against its opaque surface.
    // Over the wallpaper (home or drawer strip) white icons read best; the
    // settings page is an opaque themed surface, so follow the theme there.
    SystemBarIcons(lightIcons = !settingsOpen || isSystemInDarkTheme())

    BackHandler(enabled = settingsOpen) { settingsOpen = false }
    BackHandler(enabled = swipe.isOpen && !settingsOpen) { scope.launch { swipe.close() } }

    Box(
        Modifier
            .fillMaxSize()
            // The drawer travels the full height of the launcher; this is
            // Launcher3's shiftRange.
            .onSizeChanged { swipe.shiftRangePx = it.height.toFloat() },
    ) {
        Box(Modifier.graphicsLayer { alpha = swipe.homeAlpha }) {
            HomeScreen(
                apps = apps.orEmpty(),
                widgets = widgets,
                homeGrid = homeGrid,
                swipe = swipe,
                settings = settings,
                onOpenDrawer = { scope.launch { swipe.open() } },
                onOpenSettings = { settingsOpen = true },
            )
        }

        if (!swipe.isFullyClosed) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f * swipe.scrimAlpha)),
            )
            AppDrawer(
                apps = apps,
                homeGrid = homeGrid,
                swipe = swipe,
                onClose = { scope.launch { swipe.close() } },
                modifier = Modifier.graphicsLayer {
                    translationY = swipe.progress.value * swipe.shiftRangePx
                },
            )
        }

        if (settingsOpen) {
            SettingsScreen(settings = settings, onClose = { settingsOpen = false })
        }
    }
}

/**
 * Drives the status / navigation bar icon colour. [lightIcons] true means white
 * icons (for a dark backdrop), false means black icons (for a light one).
 */
@Composable
private fun SystemBarIcons(lightIcons: Boolean) {
    val view = LocalView.current
    if (view.isInEditMode) return
    val window = (view.context as? android.app.Activity)?.window ?: return
    androidx.compose.runtime.SideEffect {
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = !lightIcons
            isAppearanceLightNavigationBars = !lightIcons
        }
    }
}

/* ----------------------------- Home screen ------------------------------- */

@Composable
private fun HomeScreen(
    apps: List<AppInfo>,
    widgets: WidgetState,
    homeGrid: HomeGridState,
    swipe: AllAppsSwipeState,
    settings: LauncherSettingsState,
    onOpenDrawer: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var menuAt by remember { mutableStateOf<Offset?>(null) }
    val addWidget = rememberWidgetAdder(widgets)

    androidx.compose.foundation.layout.BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = { at -> menuAt = at })
            }
            // Swipe up follows the finger straight into the app drawer.
            .draggable(
                orientation = AllAppsDragOrientation,
                state = rememberDraggableState { dy -> scope.launch { swipe.dragBy(dy) } },
                onDragStopped = { velocity -> swipe.settle(velocity) },
            ),
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
            Spacer(Modifier.height(24.dp))
            WidgetPanel(widgets)
            HomeIconGrid(state = homeGrid, apps = apps)

            Spacer(Modifier.weight(1f))

            SearchPill(onClick = onOpenDrawer)
            Spacer(Modifier.height(20.dp))
            Dock(apps = pickDockApps(apps, dockCount), settings = settings)
            Spacer(Modifier.height(12.dp))
        }

        menuAt?.let { at ->
            HomeMenu(
                at = at,
                onAddWidget = addWidget,
                onOpenSettings = onOpenSettings,
                onDismiss = { menuAt = null },
            )
        }
    }
}

/**
 * Pixel-style long-press menu: a small floating popup at the touch point with
 * the home screen actions.
 */
@Composable
private fun HomeMenu(
    at: Offset,
    onAddWidget: () -> Unit,
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    Popup(
        offset = IntOffset(at.x.toInt(), at.y.toInt()),
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true),
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
            shadowElevation = 8.dp,
        ) {
            Column(Modifier.width(220.dp).padding(vertical = 8.dp)) {
                HomeMenuItem("Wallpaper & style") {
                    onDismiss()
                    openWallpaperPicker(context)
                }
                HomeMenuItem("Widgets") {
                    onDismiss()
                    onAddWidget()
                }
                HomeMenuItem("Home settings") {
                    onDismiss()
                    onOpenSettings()
                }
            }
        }
    }
}

@Composable
private fun HomeMenuItem(label: String, onClick: () -> Unit) {
    Surface(onClick = onClick, color = Color.Transparent, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
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
private fun Dock(apps: List<AppInfo>, settings: LauncherSettingsState) {
    if (apps.isEmpty()) return
    Surface(
        shape = RoundedCornerShape(settings.dockCornerRadiusDp.dp),
        color = settings.resolvedDockColor(),
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
private fun AppDrawer(
    apps: List<AppInfo>?,
    homeGrid: HomeGridState,
    swipe: AllAppsSwipeState,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    val gridState = rememberLazyGridState()
    val nestedScrollConnection = rememberDrawerNestedScroll(
        state = swipe,
        gridState = gridState,
        onDrag = { dy -> scope.launch { swipe.dragBy(dy) } },
        onSettle = { velocity -> scope.launch { swipe.settle(velocity) } },
    )

    val filtered = remember(apps, query) {
        val all = apps.orEmpty()
        if (query.isBlank()) all
        else all.filter { it.label.contains(query.trim(), ignoreCase = true) }
    }

    // A sheet rising over the wallpaper, not an opaque page: rounded top
    // corners and enough translucency for the wallpaper to tint through,
    // matching how Pixel's all-apps reads.
    Surface(
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.82f),
        modifier = modifier
            .fillMaxSize()
            // Launcher3 stops all-apps at mInsets.top rather than y=0, which
            // is what keeps the sheet's top edge visible over the wallpaper.
            .statusBarsPadding()
            .nestedScroll(nestedScrollConnection),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
                // Contents fade in late in the gesture, the way Launcher3
                // clamps the all-apps content fade.
                .graphicsLayer { alpha = swipe.contentAlpha },
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
                    state = gridState,
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
                            onAddToHome = {
                                homeGrid.pin(app.packageName, app.activityName)
                                onClose()
                            },
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
    onAddToHome: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    var menuOpen by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(
                onClick = {
                    launchApp(context, app)
                    onLaunched()
                },
                onLongClick = { menuOpen = true },
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
        DropdownMenu(
            expanded = menuOpen,
            onDismissRequest = { menuOpen = false },
            shape = RoundedCornerShape(20.dp),
        ) {
            if (onAddToHome != null) {
                DropdownMenuItem(
                    text = { Text("Add to Home screen") },
                    onClick = {
                        menuOpen = false
                        onAddToHome()
                    },
                )
            }
            DropdownMenuItem(
                text = { Text("App info") },
                onClick = {
                    menuOpen = false
                    openAppInfo(context, app.packageName)
                },
            )
        }
    }
}

/* ------------------------------- Helpers --------------------------------- */
