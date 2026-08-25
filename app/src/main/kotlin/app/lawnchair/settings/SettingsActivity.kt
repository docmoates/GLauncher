package app.lawnchair.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = LauncherPreferences(this)
        setContent {
            MaterialTheme {
                SettingsScreen(prefs)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(prefs: LauncherPreferences) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Home", "Appearance", "Dock", "Apps", "Gestures", "More")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Launcher Settings", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, label ->
                    NavigationBarItem(
                        label = { Text(label, fontSize = 10.sp) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {}
                    )
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
            when (selectedTab) {
                0 -> HomeSettingsTab(prefs)
                1 -> AppearanceSettingsTab(prefs)
                2 -> DockSettingsTab(prefs)
                3 -> AppDrawerSettingsTab(prefs)
                4 -> GestureSettingsTab(prefs)
                5 -> MoreSettingsTab(prefs)
            }
        }
    }
}

@Composable
fun HomeSettingsTab(prefs: LauncherPreferences) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            SectionHeader("Grid Layout")
        }
        item {
            var columns by remember { mutableStateOf(prefs.gridColumns.toFloat()) }
            SettingSlider(
                label = "Columns",
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
                value = rows,
                range = 4f..8f,
                onValueChange = {
                    rows = it
                    prefs.gridRows = it.toInt()
                }
            )
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item {
            SectionHeader("Other")
        }
        item {
            var showSearchBar by remember { mutableStateOf(prefs.showSearchBar) }
            SettingToggle(
                label = "Show Search Bar",
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
fun AppearanceSettingsTab(prefs: LauncherPreferences) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { SectionHeader("Theme") }
        item {
            val themes = listOf("Light", "Dark", "System")
            var selectedTheme by remember { mutableStateOf(prefs.themeMode) }
            SettingDropdown(
                label = "Theme Mode",
                options = themes,
                selectedValue = selectedTheme,
                onValueChange = {
                    selectedTheme = it
                    prefs.themeMode = it.lowercase()
                }
            )
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item { SectionHeader("Status Bar") }
        item {
            var showStatusBar by remember { mutableStateOf(prefs.showStatusBar) }
            SettingToggle(
                label = "Show Status Bar",
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
fun DockSettingsTab(prefs: LauncherPreferences) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { SectionHeader("Dock Settings") }
        item {
            var showDock by remember { mutableStateOf(prefs.showDock) }
            SettingToggle(
                label = "Show Dock",
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
                value = opacity,
                range = 0f..1f,
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
fun AppDrawerSettingsTab(prefs: LauncherPreferences) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { SectionHeader("App Drawer") }
        item {
            var columns by remember { mutableStateOf(prefs.appDrawerColumns.toFloat()) }
            SettingSlider(
                label = "Columns",
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
                label = "Enable Search",
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
fun GestureSettingsTab(prefs: LauncherPreferences) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { SectionHeader("Gestures") }
        item {
            val gestureOptions = listOf("App Drawer", "Google Search", "Voice Search", "None")
            var swipeUp by remember { mutableStateOf(prefs.swipeUpGesture) }
            SettingDropdown(
                label = "Swipe Up",
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
fun MoreSettingsTab(prefs: LauncherPreferences) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { SectionHeader("Animations") }
        item {
            val speeds = listOf("Slow", "Normal", "Fast")
            var animSpeed by remember { mutableStateOf(prefs.animationSpeed) }
            SettingDropdown(
                label = "Animation Speed",
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
                checked = iconAnim,
                onCheckedChange = {
                    iconAnim = it
                    prefs.iconAnimationEnabled = it
                }
            )
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item { SectionHeader("Notifications") }
        item {
            var badges by remember { mutableStateOf(prefs.showNotificationBadges) }
            SettingToggle(
                label = "Notification Badges",
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
                checked = count,
                onCheckedChange = {
                    count = it
                    prefs.showNotificationCount = it
                }
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SettingToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 14.sp)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun SettingSlider(label: String, value: Float, range: ClosedFloatingPointRange<Float>, onValueChange: (Float) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 14.sp)
            Text(value.toInt().toString(), fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            steps = (range.endInclusive - range.start).toInt() - 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

@Composable
fun SettingDropdown(label: String, options: List<String>, selectedValue: String, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                .padding(16.dp)
                .clickable { expanded = !expanded }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(selectedValue, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
                Text("▼", fontSize = 12.sp)
            }
        }

        if (expanded) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                LazyColumn {
                    items(options) { option ->
                        Text(
                            text = option,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onValueChange(option)
                                    expanded = false
                                }
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
