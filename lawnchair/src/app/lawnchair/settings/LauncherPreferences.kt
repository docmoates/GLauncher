package app.lawnchair.settings

import android.content.Context
import android.content.SharedPreferences

class LauncherPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("launcher_prefs", Context.MODE_PRIVATE)

    // Home Screen Settings
    var gridColumns: Int
        get() = prefs.getInt("grid_columns", 5)
        set(value) = prefs.edit().putInt("grid_columns", value).apply()

    var gridRows: Int
        get() = prefs.getInt("grid_rows", 6)
        set(value) = prefs.edit().putInt("grid_rows", value).apply()

    var dockGridSize: Int
        get() = prefs.getInt("dock_grid_size", 5)
        set(value) = prefs.edit().putInt("dock_grid_size", value).apply()

    // Appearance Settings
    var themeMode: String
        get() = prefs.getString("theme_mode", "system") ?: "system"
        set(value) = prefs.edit().putString("theme_mode", value).apply()

    var accentColor: Int
        get() = prefs.getInt("accent_color", 0xFF6200EE.toInt())
        set(value) = prefs.edit().putInt("accent_color", value).apply()

    var showStatusBar: Boolean
        get() = prefs.getBoolean("show_status_bar", true)
        set(value) = prefs.edit().putBoolean("show_status_bar", value).apply()

    var showSearchBar: Boolean
        get() = prefs.getBoolean("show_search_bar", true)
        set(value) = prefs.edit().putBoolean("show_search_bar", value).apply()

    // Dock Settings
    var showDock: Boolean
        get() = prefs.getBoolean("show_dock", true)
        set(value) = prefs.edit().putBoolean("show_dock", value).apply()

    var dockBackgroundOpacity: Float
        get() = prefs.getFloat("dock_bg_opacity", 0.8f)
        set(value) = prefs.edit().putFloat("dock_bg_opacity", value).apply()

    // App Drawer Settings
    var appDrawerColumns: Int
        get() = prefs.getInt("app_drawer_columns", 4)
        set(value) = prefs.edit().putInt("app_drawer_columns", value).apply()

    var appDrawerSearchEnabled: Boolean
        get() = prefs.getBoolean("app_drawer_search", true)
        set(value) = prefs.edit().putBoolean("app_drawer_search", value).apply()

    var appDrawerSortBy: String
        get() = prefs.getString("app_drawer_sort", "alphabetical") ?: "alphabetical"
        set(value) = prefs.edit().putString("app_drawer_sort", value).apply()

    // Gesture Settings
    var swipeUpGesture: String
        get() = prefs.getString("swipe_up_gesture", "app_drawer") ?: "app_drawer"
        set(value) = prefs.edit().putString("swipe_up_gesture", value).apply()

    var doubleTapGesture: String
        get() = prefs.getString("double_tap_gesture", "lock_screen") ?: "lock_screen"
        set(value) = prefs.edit().putString("double_tap_gesture", value).apply()

    var longPressGesture: String
        get() = prefs.getString("long_press_gesture", "wallpaper") ?: "wallpaper"
        set(value) = prefs.edit().putString("long_press_gesture", value).apply()

    // Animation Settings
    var animationSpeed: String
        get() = prefs.getString("animation_speed", "normal") ?: "normal"
        set(value) = prefs.edit().putString("animation_speed", value).apply()

    var iconAnimationEnabled: Boolean
        get() = prefs.getBoolean("icon_animation", true)
        set(value) = prefs.edit().putBoolean("icon_animation", value).apply()

    // Notification Settings
    var showNotificationBadges: Boolean
        get() = prefs.getBoolean("notification_badges", true)
        set(value) = prefs.edit().putBoolean("notification_badges", value).apply()

    var showNotificationCount: Boolean
        get() = prefs.getBoolean("notification_count", true)
        set(value) = prefs.edit().putBoolean("notification_count", value).apply()

    // Widget Settings
    var enableWidgets: Boolean
        get() = prefs.getBoolean("enable_widgets", true)
        set(value) = prefs.edit().putBoolean("enable_widgets", value).apply()

    var showWidgetLabels: Boolean
        get() = prefs.getBoolean("show_widget_labels", true)
        set(value) = prefs.edit().putBoolean("show_widget_labels", value).apply()
}
