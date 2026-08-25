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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    SettingsList {
        item { SectionHeader("Grid Layout", Icons.Outlined.GridView, accent) }
        item {
            var columns by remember { mutableStateOf(prefs.gridColumns.toFloat()) }
            SettingSlider(
                label = "Columns",
                description = "Icons per row on the home screen",
                icon = Icons.Outlined.ViewColumn,
                accent = accent,
                value = columns,
                range = 3f..7f,
                onValueChange = {
                    columns = it
                    prefs.gridColumns = it.toInt()
                }
            )
        }
        item {
            var rows by remember { mutableStateOf(prefs.gridRows.toFloat()) }
            SettingSlider(
                label = "Rows",
                description = "Icon rows visible per screen",
                icon = Icons.Outlined.TableRows,
                accent = accent,
                value = rows,
                range = 4f..8f,
                onValueChange = {
                    rows = it
                    prefs.gridRows = it.toInt()
                }
            )
        }
        item { SectionHeader("Search", Icons.Outlined.Search, accent) }
        item {
            var showSearchBar by remember { mutableStateOf(prefs.showSearchBar) }
            SettingToggle(
                label = "Search Bar",
                description = "Show the search bar on your home screen",
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
    SettingsList {
        item { SectionHeader("Theme", Icons.Outlined.Palette, accent) }
        item {
            val themes = listOf("Light", "Dark", "System")
            var selectedTheme by remember { mutableStateOf(prefs.themeMode) }
            SettingDropdown(
                label = "Theme Mode",
                description = "Choose how the launcher looks",
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
            var showStatusBar by remember { mutableStateOf(prefs.showStatusBar) }
            SettingToggle(
                label = "Status Bar",
                description = "Show the clock and icons at the top",
                icon = Icons.Outlined.SignalCellularAlt,
                accent = accent,
                checked = showStatusBar,
                onCheckedChange = {
                    showStatusBar = it
                    prefs.showStatusBar = it
                }
            )
        }
    }
}

@Composable
fun DockSettingsTab(prefs: LauncherPreferences, accent: Color) {
    SettingsList {
        item { SectionHeader("Dock", Icons.Outlined.ViewAgenda, accent) }
        item {
            var showDock by remember { mutableStateOf(prefs.showDock) }
            SettingToggle(
                label = "Show Dock",
                description = "Keep your favorite apps within reach",
                icon = Icons.Outlined.Apps,
                accent = accent,
                checked = showDock,
                onCheckedChange = {
                    showDock = it
                    prefs.showDock = it
                }
            )
        }
        item {
            var opacity by remember { mutableStateOf(prefs.dockBackgroundOpacity) }
            SettingSlider(
                label = "Background Opacity",
                description = "How solid the dock background appears",
                icon = Icons.Outlined.Opacity,
                accent = accent,
                value = opacity,
                range = 0f..1f,
                valueLabel = { "${(it * 100).toInt()}%" },
                onValueChange = {
                    opacity = it
                    prefs.dockBackgroundOpacity = it
                }
            )
        }
        item {
            var gridSize by remember { mutableStateOf(prefs.dockGridSize.toFloat()) }
            SettingSlider(
                label = "Dock Icons",
                description = "Number of icons in the dock",
                icon = Icons.Outlined.Apps,
                accent = accent,
                value = gridSize,
                range = 3f..7f,
                onValueChange = {
                    gridSize = it
                    prefs.dockGridSize = it.toInt()
                }
            )
        }
    }
}

@Composable
fun AppDrawerSettingsTab(prefs: LauncherPreferences, accent: Color) {
    SettingsList {
        item { SectionHeader("App Drawer", Icons.Outlined.Apps, accent) }
        item {
            var columns by remember { mutableStateOf(prefs.appDrawerColumns.toFloat()) }
            SettingSlider(
                label = "Columns",
                description = "Icons per row in the app drawer",
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
            var searchEnabled by remember { mutableStateOf(prefs.appDrawerSearchEnabled) }
            SettingToggle(
                label = "Search",
                description = "Find apps quickly by typing",
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
            val sortOptions = listOf("Alphabetical", "Recent", "Frequency")
            var selectedSort by remember { mutableStateOf(prefs.appDrawerSortBy) }
            SettingDropdown(
                label = "Sort By",
                description = "How apps are ordered in the drawer",
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
                description = "Action when swiping up from home",
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
                description = "Action when double-tapping home",
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
                description = "Action when long-pressing home",
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
                description = "How fast transitions play",
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
                description = "Playful motion when opening apps",
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
                description = "Small dots on apps with alerts",
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
                description = "Display the number of notifications",
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
