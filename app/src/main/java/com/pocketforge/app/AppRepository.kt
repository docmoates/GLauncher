package com.pocketforge.app

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

/** A single launchable app, with its icon pre-rasterised for Compose. */
data class AppInfo(
    val label: String,
    val packageName: String,
    val activityName: String,
    val icon: ImageBitmap,
)

/**
 * Reads every launchable app on the device, sorted alphabetically. Run this off
 * the main thread (it touches PackageManager and rasterises every icon).
 */
fun loadApps(context: Context): List<AppInfo> {
    val pm = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
    return pm.queryIntentActivities(intent, 0)
        .mapNotNull { ri ->
            val activity = ri.activityInfo ?: return@mapNotNull null
            // Hide ourselves from our own app list.
            if (activity.packageName == context.packageName) return@mapNotNull null
            val label = ri.loadLabel(pm)?.toString()?.takeIf { it.isNotBlank() }
                ?: activity.packageName
            AppInfo(
                label = label,
                packageName = activity.packageName,
                activityName = activity.name,
                icon = ri.loadIcon(pm).toImageBitmap(),
            )
        }
        .distinctBy { it.packageName + "/" + it.activityName }
        .sortedBy { it.label.lowercase() }
}

/** Launches an app's main activity in its own task. */
fun launchApp(context: Context, app: AppInfo) {
    val intent = Intent(Intent.ACTION_MAIN)
        .addCategory(Intent.CATEGORY_LAUNCHER)
        .setComponent(ComponentName(app.packageName, app.activityName))
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
    runCatching { context.startActivity(intent) }
}

/** Opens the system "App info" screen — handy for long-press → uninstall/settings. */
fun openAppInfo(context: Context, packageName: String) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        .setData(Uri.parse("package:$packageName"))
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    runCatching { context.startActivity(intent) }
}

/**
 * Picks the apps shown in the bottom dock. We prefer the usual Pixel suspects
 * (phone, messages, browser, camera, photos…) and fall back to the first apps
 * alphabetically so the dock is never empty. Edit [DOCK_PREFERENCES] to taste.
 */
fun pickDockApps(apps: List<AppInfo>, count: Int): List<AppInfo> {
    if (apps.isEmpty()) return emptyList()
    val byPackage = apps.associateBy { it.packageName }
    val preferred = DOCK_PREFERENCES.firstNotNullOfEachPackage(byPackage)
    val result = LinkedHashSet(preferred)
    for (app in apps) {
        if (result.size >= count) break
        result.add(app)
    }
    return result.take(count)
}

private fun List<List<String>>.firstNotNullOfEachPackage(
    byPackage: Map<String, AppInfo>,
): List<AppInfo> = mapNotNull { candidates ->
    candidates.firstNotNullOfOrNull { byPackage[it] }
}

/**
 * Dock slots, in order. Each inner list is a set of equivalent package names
 * (e.g. Google Dialer vs AOSP Dialer); the first one that's installed wins.
 * Customise these to set your own favourites.
 */
private val DOCK_PREFERENCES: List<List<String>> = listOf(
    listOf("com.google.android.dialer", "com.android.dialer", "com.android.phone"),
    listOf("com.google.android.apps.messaging", "com.android.messaging"),
    listOf("com.android.chrome", "org.mozilla.firefox"),
    listOf("com.google.android.GoogleCamera", "com.android.camera2", "com.android.camera"),
    listOf("com.google.android.apps.photos", "com.android.gallery3d"),
    listOf("com.google.android.gm", "com.android.email"),
    listOf("com.android.vending"),
)

/** Converts any [Drawable] (adaptive icons included) into a Compose [ImageBitmap]. */
private fun Drawable.toImageBitmap(sizePx: Int = 144): ImageBitmap {
    (this as? BitmapDrawable)?.bitmap?.let { return it.asImageBitmap() }
    val width = intrinsicWidth.takeIf { it > 0 } ?: sizePx
    val height = intrinsicHeight.takeIf { it > 0 } ?: sizePx
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap.asImageBitmap()
}

/** Opens the system wallpaper picker ("Wallpaper & style" on Pixel). */
fun openWallpaperPicker(context: Context) {
    val candidates = listOf(
        Intent("android.intent.action.SET_WALLPAPER").setPackage("com.google.android.apps.wallpaper"),
        Intent(Intent.ACTION_SET_WALLPAPER),
    )
    for (intent in candidates) {
        val ok = runCatching {
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }.isSuccess
        if (ok) return
    }
}
