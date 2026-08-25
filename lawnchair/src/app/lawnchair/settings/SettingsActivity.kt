package app.lawnchair.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.lawnchair.preferences.getAdapter
import app.lawnchair.preferences.preferenceManager
import app.lawnchair.preferences2.preferenceManager2

private data class SettingsSection(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val accent: Color,
)

private val sections = listOf(
    SettingsSection("Home", Icons.Outlined.Home, Icons.Filled.Home, SectionColors.Home),
    SettingsSection("Look", Icons.Outlined.Palette, Icons.Filled.Palette, SectionColors.Appearance),
    SettingsSection("Dock", Icons.Outlined.ViewAgenda, Icons.Filled.ViewAgenda, SectionColors.Dock),
    SettingsSection("Apps", Icons.Outlined.Apps, Icons.Filled.Apps, SectionColors.Apps),
    SettingsSection("Gestures", Icons.Outlined.TouchApp, Icons.Filled.TouchApp, SectionColors.Gestures),
    SettingsSection("More", Icons.Outlined.Tune, Icons.Filled.Tune, SectionColors.More),
)

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val prefs = LauncherPreferences(this)
        setContent {
            SettingsTheme {
                SettingsScreen(prefs)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(prefs: LauncherPreferences) {
    var selectedTab by remember { mutableStateOf(0) }
    val current = sections[selectedTab]
    val accent by animateColorAsStateCompat(current.accent)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                "Settings",
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Text(
                                "Customize your launcher",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
            }
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp,
            ) {
                Column {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 0.dp,
                    ) {
                        sections.forEachIndexed { index, section ->
                            val selected = selectedTab == index
                            NavigationBarItem(
                                selected = selected,
                                onClick = { selectedTab = index },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) section.selectedIcon else section.icon,
                                        contentDescription = section.label,
                                    )
                                },
                                label = { Text(section.label, fontSize = 11.sp, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = section.accent,
                                    selectedTextColor = section.accent,
                                    indicatorColor = section.accent.copy(alpha = 0.14f),
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    (fadeIn(tween(180)) togetherWith fadeOut(tween(120)))
                },
                label = "settings-tab",
            ) { tab ->
                when (tab) {
                    0 -> HomeSettingsTab(prefs, accent)
                    1 -> AppearanceSettingsTab(prefs, accent)
                    2 -> DockSettingsTab(prefs, accent)
                    3 -> AppDrawerSettingsTab(prefs, accent)
                    4 -> GestureSettingsTab(prefs, accent)
                    5 -> MoreSettingsTab(prefs, accent)
                }
            }
        }
    }
}

// Simple color animation helper without extra deps
@Composable
private fun animateColorAsStateCompat(target: Color) =
    androidx.compose.animation.animateColorAsState(target, tween(220), label = "accent")

@Composable
fun HomeSettingsTab(prefs: LauncherPreferences, accent: Color) {
    val legacyPrefs = preferenceManager()
    val columnsAdapter = legacyPrefs.workspaceColumns.getAdapter()
    val rowsAdapter = legacyPrefs.workspaceRows.getAdapter()
    val columns = columnsAdapter.state.value
    val rows = rowsAdapter.state.value
    var showSearchBar by remember { mutableStateOf(prefs.showSearchBar) }

    SettingsList {
        item {
            LivePreview(accent) {
                HomeScreenPreview(
                    columns = columns,
                    rows = rows,
                    showSearchBar = showSearchBar,
                    accent = accent,
                )
            }
        }
        item { SectionHeader("Grid Layout", Icons.Outlined.GridView, accent) }
        item {
            SettingSlider(
                label = "Columns",
                description = "Icons per row on the home screen",
                icon = Icons.Outlined.ViewColumn,
                accent = accent,
                value = columns.toFloat(),
                range = 3f..8f,
                onValueChange = { columnsAdapter.onChange(it.toInt()) }
            )
        }
        item {
            SettingSlider(
                label = "Rows",
                description = "Icon rows visible per screen",
                icon = Icons.Outlined.TableRows,
                accent = accent,
                value = rows.toFloat(),
                range = 3f..9f,
                onValueChange = { rowsAdapter.onChange(it.toInt()) }
            )
        }
        item { SectionHeader("Search", Icons.Outlined.Search, accent) }
        item {
            SettingToggle(
                label = "Search Bar",
                description = "Show the search bar on your home screen (not yet connected to the launcher)",
                icon = Icons.Outlined.Search,
                accent = accent,
                checked = showSearchBar,
                onCheckedChange = {
                    showSearchBar = it
                    prefs.showSearchBar = it
                }
            )
        }
    }
}

@Composable
fun AppearanceSettingsTab(prefs: LauncherPreferences, accent: Color) {
    val themes = listOf("Light", "Dark", "System")
    var selectedTheme by remember { mutableStateOf(prefs.themeMode) }
    val prefs2 = preferenceManager2()
    val statusBarAdapter = prefs2.showStatusBar.getAdapter()
    val showStatusBar = statusBarAdapter.state.value

    SettingsList {
        item {
            LivePreview(accent) {
                AppearancePreview(
                    themeMode = selectedTheme,
                    showStatusBar = showStatusBar,
                    accent = accent,
                )
            }
        }
        item { SectionHeader("Theme", Icons.Outlined.Palette, accent) }
        item {
            SettingDropdown(
                label = "Theme Mode",
                description = "Choose how the launcher looks (not yet connected)",
                icon = Icons.Outlined.Contrast,
                accent = accent,
                options = themes,
                selectedValue = selectedTheme,
                onValueChange = {
                    selectedTheme = it
                    prefs.themeMode = it.lowercase()
                }
            )
        }
        item { SectionHeader("System Bars", Icons.Outlined.PhoneAndroid, accent) }
        item {
            SettingToggle(
                label = "Status Bar",
                description = "Show the clock and icons at the top",
                icon = Icons.Outlined.SignalCellularAlt,
                accent = accent,
                checked = showStatusBar,
                onCheckedChange = { statusBarAdapter.onChange(it) }
            )
        }
    }
}

@Composable
fun DockSettingsTab(prefs: LauncherPreferences, accent: Color) {
    val prefs2 = preferenceManager2()
    val legacyPrefs = preferenceManager()
    val hotseatEnabledAdapter = prefs2.isHotseatEnabled.getAdapter()
    val hotseatBgAdapter = legacyPrefs.hotseatBG.getAdapter()
    val hotseatBgAlphaAdapter = legacyPrefs.hotseatBGAlpha.getAdapter()
    val hotseatColumnsAdapter = legacyPrefs.hotseatColumns.getAdapter()

    val showDock = hotseatEnabledAdapter.state.value
    val opacity = hotseatBgAlphaAdapter.state.value / 100f
    val gridSize = hotseatColumnsAdapter.state.value

    SettingsList {
        item {
            LivePreview(accent) {
                DockPreview(
                    showDock = showDock,
                    dockIcons = gridSize,
                    opacity = opacity,
                    accent = accent,
                )
            }
        }
        item { SectionHeader("Dock", Icons.Outlined.ViewAgenda, accent) }
        item {
            SettingToggle(
                label = "Show Dock",
                description = "Keep your favorite apps within reach",
                icon = Icons.Outlined.Apps,
                accent = accent,
                checked = showDock,
                onCheckedChange = { hotseatEnabledAdapter.onChange(it) }
            )
        }
        item {
            SettingSlider(
                label = "Background Opacity",
                description = "How solid the dock background appears",
                icon = Icons.Outlined.Opacity,
                accent = accent,
                value = opacity,
                range = 0f..1f,
                valueLabel = { "${(it * 100).toInt()}%" },
                onValueChange = {
                    hotseatBgAdapter.onChange(true)
                    hotseatBgAlphaAdapter.onChange((it * 100).toInt())
                }
            )
        }
        item {
            SettingSlider(
                label = "Dock Icons",
                description = "Number of icons in the dock",
                icon = Icons.Outlined.Apps,
                accent = accent,
                value = gridSize.toFloat(),
                range = 3f..10f,
                onValueChange = { hotseatColumnsAdapter.onChange(it.toInt()) }
            )
        }
    }
}

@Composable
fun AppDrawerSettingsTab(prefs: LauncherPreferences, accent: Color) {
    var columns by remember { mutableStateOf(prefs.appDrawerColumns.toFloat()) }
    var searchEnabled by remember { mutableStateOf(prefs.appDrawerSearchEnabled) }
    val sortOptions = listOf("Alphabetical", "Recent", "Frequency")
    var selectedSort by remember { mutableStateOf(prefs.appDrawerSortBy) }

    SettingsList {
        item {
            LivePreview(accent) {
                AppDrawerPreview(
                    columns = columns.toInt(),
                    showSearch = searchEnabled,
                    accent = accent,
                )
            }
        }
        item { SectionHeader("App Drawer", Icons.Outlined.Apps, accent) }
        item {
            SettingSlider(
                label = "Columns",
                description = "Icons per row in the app drawer (not yet connected)",
                icon = Icons.Outlined.GridView,
                accent = accent,
                value = columns,
                range = 3f..7f,
                onValueChange = {
                    columns = it
                    prefs.appDrawerColumns = it.toInt()
                }
            )
        }
        item {
            SettingToggle(
                label = "Search",
                description = "Find apps quickly by typing (not yet connected)",
                icon = Icons.Outlined.Search,
                accent = accent,
                checked = searchEnabled,
                onCheckedChange = {
                    searchEnabled = it
                    prefs.appDrawerSearchEnabled = it
                }
            )
        }
        item {
            SettingDropdown(
                label = "Sort By",
                description = "How apps are ordered in the drawer (not yet connected)",
                icon = Icons.Outlined.Sort,
                accent = accent,
                options = sortOptions,
                selectedValue = selectedSort,
                onValueChange = {
                    selectedSort = it
                    prefs.appDrawerSortBy = it.lowercase()
                }
            )
        }
    }
}

@Composable
fun GestureSettingsTab(prefs: LauncherPreferences, accent: Color) {
    SettingsList {
        item { SectionHeader("Gestures", Icons.Outlined.TouchApp, accent) }
        item {
            val gestureOptions = listOf("App Drawer", "Google Search", "Voice Search", "None")
            var swipeUp by remember { mutableStateOf(prefs.swipeUpGesture) }
            SettingDropdown(
                label = "Swipe Up",
                description = "Action when swiping up from home (not yet connected)",
                icon = Icons.Outlined.SwipeUp,
                accent = accent,
                options = gestureOptions,
                selectedValue = swipeUp,
                onValueChange = {
                    swipeUp = it
                    prefs.swipeUpGesture = it.lowercase().replace(" ", "_")
                }
            )
        }
        item {
            val gestureOptions = listOf("Lock Screen", "Google Assistant", "None")
            var doubleTap by remember { mutableStateOf(prefs.doubleTapGesture) }
            SettingDropdown(
                label = "Double Tap",
                description = "Action when double-tapping home (not yet connected)",
                icon = Icons.Outlined.TouchApp,
                accent = accent,
                options = gestureOptions,
                selectedValue = doubleTap,
                onValueChange = {
                    doubleTap = it
                    prefs.doubleTapGesture = it.lowercase().replace(" ", "_")
                }
            )
        }
        item {
            val gestureOptions = listOf("Wallpaper", "Launcher Settings", "None")
            var longPress by remember { mutableStateOf(prefs.longPressGesture) }
            SettingDropdown(
                label = "Long Press",
                description = "Action when long-pressing home (not yet connected)",
                icon = Icons.Outlined.PanTool,
                accent = accent,
                options = gestureOptions,
                selectedValue = longPress,
                onValueChange = {
                    longPress = it
                    prefs.longPressGesture = it.lowercase().replace(" ", "_")
                }
            )
        }
    }
}

@Composable
fun MoreSettingsTab(prefs: LauncherPreferences, accent: Color) {
    SettingsList {
        item { SectionHeader("Animations", Icons.Outlined.Animation, accent) }
        item {
            val speeds = listOf("Slow", "Normal", "Fast")
            var animSpeed by remember { mutableStateOf(prefs.animationSpeed) }
            SettingDropdown(
                label = "Animation Speed",
                description = "How fast transitions play (not yet connected)",
                icon = Icons.Outlined.Speed,
                accent = accent,
                options = speeds,
                selectedValue = animSpeed,
                onValueChange = {
                    animSpeed = it
                    prefs.animationSpeed = it.lowercase()
                }
            )
        }
        item {
            var iconAnim by remember { mutableStateOf(prefs.iconAnimationEnabled) }
            SettingToggle(
                label = "Icon Animations",
                description = "Playful motion when opening apps (not yet connected)",
                icon = Icons.Outlined.AutoAwesome,
                accent = accent,
                checked = iconAnim,
                onCheckedChange = {
                    iconAnim = it
                    prefs.iconAnimationEnabled = it
                }
            )
        }
        item { SectionHeader("Notifications", Icons.Outlined.Notifications, accent) }
        item {
            var badges by remember { mutableStateOf(prefs.showNotificationBadges) }
            SettingToggle(
                label = "Notification Badges",
                description = "Small dots on apps with alerts (not yet connected)",
                icon = Icons.Outlined.Circle,
                accent = accent,
                checked = badges,
                onCheckedChange = {
                    badges = it
                    prefs.showNotificationBadges = it
                }
            )
        }
        item {
            var count by remember { mutableStateOf(prefs.showNotificationCount) }
            SettingToggle(
                label = "Show Count",
                description = "Display the number of notifications (not yet connected)",
                icon = Icons.Outlined.Numbers,
                accent = accent,
                checked = count,
                onCheckedChange = {
                    count = it
                    prefs.showNotificationCount = it
                }
            )
        }
    }
}

// ---------- Reusable building blocks ----------

@Composable
fun SettingsList(content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = content,
    )
}

// ---------- Live preview ----------

@Composable
fun LivePreview(accent: Color, content: @Composable BoxScope.() -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(accent)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = "LIVE PREVIEW",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                color = accent,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            accent.copy(alpha = 0.16f),
                            MaterialTheme.colorScheme.surfaceVariant,
                        )
                    )
                )
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp))
                .padding(16.dp),
            content = content,
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
private fun PreviewIcon(tint: Color, size: androidx.compose.ui.unit.Dp = 30.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(9.dp))
            .background(tint)
    )
}

@Composable
fun HomeScreenPreview(columns: Int, rows: Int, showSearchBar: Boolean, accent: Color) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (showSearchBar) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.55f))
                    .padding(horizontal = 12.dp)
            ) {
                Icon(
                    Icons.Outlined.Search,
                    contentDescription = null,
                    tint = Color.Black.copy(alpha = 0.45f),
                    modifier = Modifier.size(13.dp),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        val visibleRows = rows.coerceAtMost(5)
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                repeat(visibleRows) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        repeat(columns) {
                            PreviewIcon(tint = accent.copy(alpha = 0.55f), size = 20.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DockPreview(showDock: Boolean, dockIcons: Int, opacity: Float, accent: Color) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        repeat(4) {
                            PreviewIcon(tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.25f), size = 22.dp)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        if (showDock) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(accent.copy(alpha = 0.25f + opacity * 0.6f))
                    .padding(horizontal = 10.dp)
            ) {
                repeat(dockIcons) {
                    PreviewIcon(tint = Color.White.copy(alpha = 0.85f), size = 26.dp)
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "Dock hidden",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun AppDrawerPreview(columns: Int, showSearch: Boolean, accent: Color) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (showSearch) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(50))
                    .padding(horizontal = 12.dp)
            ) {
                Icon(
                    Icons.Outlined.Search,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(13.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Search apps", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        repeat(columns) {
                            PreviewIcon(tint = accent.copy(alpha = 0.5f), size = 22.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppearancePreview(themeMode: String, showStatusBar: Boolean, accent: Color) {
    val isDark = when (themeMode.lowercase()) {
        "dark" -> true
        "light" -> false
        else -> androidx.compose.foundation.isSystemInDarkTheme()
    }
    val phoneBg = if (isDark) Color(0xFF1C1A16) else Color.White
    val phoneFg = if (isDark) Color(0xFFEAE4D9) else Color(0xFF2B2620)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(phoneBg)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (showStatusBar) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp)
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("9:41", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = phoneFg)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(phoneFg.copy(alpha = 0.6f))
                            )
                        }
                    }
                }
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    PreviewIcon(tint = accent, size = 34.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when {
                            themeMode.equals("system", ignoreCase = true) -> "System (${if (isDark) "Dark" else "Light"})"
                            else -> themeMode.replaceFirstChar { it.uppercase() }
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = phoneFg,
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: ImageVector, accent: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
    ) {
        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            color = accent,
        )
    }
}

@Composable
private fun IconBadge(icon: ImageVector, accent: Color) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(accent.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun SettingCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

@Composable
fun SettingToggle(
    label: String,
    description: String,
    icon: ImageVector,
    accent: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    SettingCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCheckedChange(!checked) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconBadge(icon, accent)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Text(description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = accent,
                    checkedBorderColor = accent,
                ),
            )
        }
    }
}

@Composable
fun SettingSlider(
    label: String,
    description: String,
    icon: ImageVector,
    accent: Color,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    valueLabel: (Float) -> String = { it.toInt().toString() },
    onValueChange: (Float) -> Unit,
) {
    SettingCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconBadge(icon, accent)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Text(description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = accent.copy(alpha = 0.14f),
            ) {
                Text(
                    valueLabel(value),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = accent,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                )
            }
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = accent,
                activeTrackColor = accent,
                inactiveTrackColor = accent.copy(alpha = 0.18f),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingDropdown(
    label: String,
    description: String,
    icon: ImageVector,
    accent: Color,
    options: List<String>,
    selectedValue: String,
    onValueChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val displayValue = selectedValue.replaceFirstChar { it.uppercase() }.replace("_", " ")

    SettingCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconBadge(icon, accent)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Text(description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(displayValue, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = accent)
                Icon(
                    Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        ) {
            options.forEach { option ->
                val isSelected = option.equals(displayValue, ignoreCase = true) ||
                    option.lowercase().replace(" ", "_") == selectedValue
                DropdownMenuItem(
                    text = {
                        Text(
                            option,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) accent else MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    },
                    leadingIcon = if (isSelected) {
                        { Icon(Icons.Filled.Check, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp)) }
                    } else null,
                )
            }
        }
    }
}
