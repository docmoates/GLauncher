package com.pocketforge.app

import android.app.Activity
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewConfiguration
import kotlin.math.abs
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Stable id for our [AppWidgetHost]; changing it orphans every bound widget. */
const val WIDGET_HOST_ID = 27713

private const val PREFS = "glauncher"
private const val KEY_IDS = "widget_ids"
private const val KEY_HEIGHT = "widget_height_"
private const val DEFAULT_HEIGHT_DP = 160
private const val MIN_HEIGHT_DP = 80
private const val MAX_HEIGHT_DP = 480

/**
 * The set of native app widgets placed on the home screen, persisted across
 * launches. Widget ids are owned by [host]; dropping one here also releases it
 * so the provider stops sending updates.
 */
@Stable
/**
 * [AppWidgetHost] that creates [LongPressWidgetHostView]s, so the launcher can
 * detect long-presses on widgets even though RemoteViews consume all touches
 * (the same trick Launcher3 uses for drag/resize).
 */
class LauncherWidgetHost(context: Context, hostId: Int) : AppWidgetHost(context, hostId) {
    override fun onCreateView(
        context: Context,
        appWidgetId: Int,
        appWidget: android.appwidget.AppWidgetProviderInfo?,
    ): AppWidgetHostView = LongPressWidgetHostView(context)
}

/**
 * An [AppWidgetHostView] that watches the raw touch stream and fires
 * [onLongPressListener] when the user holds still past the long-press timeout,
 * then steals the rest of the gesture so the widget doesn't also see a click.
 */
class LongPressWidgetHostView(context: Context) : AppWidgetHostView(context) {
    var onLongPressListener: (() -> Unit)? = null

    private var longPressFired = false
    private var downX = 0f
    private var downY = 0f
    private val fireLongPress = Runnable {
        longPressFired = true
        onLongPressListener?.invoke()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val slop = ViewConfiguration.get(context).scaledTouchSlop
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                longPressFired = false
                downX = ev.x
                downY = ev.y
                postDelayed(fireLongPress, ViewConfiguration.getLongPressTimeout().toLong())
            }
            MotionEvent.ACTION_MOVE ->
                if (abs(ev.x - downX) > slop || abs(ev.y - downY) > slop) {
                    removeCallbacks(fireLongPress)
                }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> removeCallbacks(fireLongPress)
        }
        return longPressFired
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        // Only reached once we've intercepted (or the widget ignored the
        // event); swallow the remainder of a long-press gesture.
        if (ev.actionMasked == MotionEvent.ACTION_UP || ev.actionMasked == MotionEvent.ACTION_CANCEL) {
            removeCallbacks(fireLongPress)
        }
        return longPressFired || super.onTouchEvent(ev)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(fireLongPress)
    }
}

class WidgetState(context: Context, val host: AppWidgetHost) {
    private val appContext = context.applicationContext
    private val prefs = appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    val manager: AppWidgetManager = AppWidgetManager.getInstance(appContext)

    // Each entry is one home-screen slot; a slot with more than one id is a
    // "stack" the user can swipe up through, like Pixel/iOS widget stacks.
    private val _slots = mutableStateListOf<List<Int>>()
    val slots: List<List<Int>> get() = _slots
    val ids: List<Int> get() = _slots.flatten()

    private val heights = mutableStateMapOf<Int, Int>()

    init {
        // Drop ids whose provider was uninstalled while we were away, and any
        // slot left empty as a result.
        _slots.addAll(
            readSlots()
                .map { slot -> slot.filter { manager.getAppWidgetInfo(it) != null } }
                .filter { it.isNotEmpty() },
        )
        persist()
    }

    fun info(id: Int): AppWidgetProviderInfo? = manager.getAppWidgetInfo(id)

    fun label(id: Int): String =
        info(id)?.loadLabel(appContext.packageManager)?.toString() ?: "Widget"

    fun heightDp(id: Int): Int =
        heights[id] ?: prefs.getInt(KEY_HEIGHT + id, DEFAULT_HEIGHT_DP)

    /** Adjusts a widget's height by [deltaDp], clamped to the allowed range. Called
     *  continuously while the user drags a resize handle, so small deltas are fine. */
    fun resize(id: Int, deltaDp: Int) {
        val next = (heightDp(id) + deltaDp).coerceIn(MIN_HEIGHT_DP, MAX_HEIGHT_DP)
        heights[id] = next
        prefs.edit().putInt(KEY_HEIGHT + id, next).apply()
    }

    /** Places [id] on the home screen as its own new slot. */
    fun add(id: Int) {
        if (id !in ids) {
            _slots.add(listOf(id))
            persist()
        }
    }

    /**
     * Adds [newId] into the same slot as [existingId], turning that slot into
     * (or extending) a swipe-through stack. Falls back to a standalone slot if
     * [existingId] isn't currently placed.
     */
    fun addToStack(existingId: Int, newId: Int) {
        if (newId in ids) return
        val index = _slots.indexOfFirst { existingId in it }
        if (index == -1) {
            add(newId)
            return
        }
        _slots[index] = _slots[index] + newId
        persist()
    }

    fun remove(id: Int) {
        val index = _slots.indexOfFirst { id in it }
        if (index == -1) return
        val remaining = _slots[index] - id
        if (remaining.isEmpty()) _slots.removeAt(index) else _slots[index] = remaining
        heights.remove(id)
        prefs.edit().remove(KEY_HEIGHT + id).apply()
        runCatching { host.deleteAppWidgetId(id) }
        persist()
    }

    private fun persist() {
        val serialized = _slots.joinToString(";") { slot -> slot.joinToString(",") }
        prefs.edit().putString(KEY_IDS, serialized).apply()
    }

    private fun readSlots(): List<List<Int>> {
        val raw = prefs.getString(KEY_IDS, "").orEmpty()
        if (raw.isBlank()) return emptyList()
        // Pre-stacking installs persisted a flat "id,id,id" list with no
        // slot separator; treat that as one widget per slot rather than
        // accidentally stacking everything the user had placed.
        if (";" !in raw) {
            return raw.split(",").mapNotNull { it.toIntOrNull() }.map { listOf(it) }
        }
        return raw.split(";")
            .filter { it.isNotBlank() }
            .map { slot -> slot.split(",").mapNotNull { it.toIntOrNull() } }
    }
}

@Composable
fun rememberWidgetState(host: AppWidgetHost): WidgetState {
    val context = LocalContext.current
    return remember(host) { WidgetState(context, host) }
}

/** One widget id whose bind result we're waiting on via the system bind prompt. */
private data class PendingBind(val id: Int, val provider: AppWidgetProviderInfo)

/**
 * Returns a callback that opens the in-app widget picker. Picking a widget
 * there runs the full "add a widget" flow: allocate an id, bind it (silently
 * if already allowed, otherwise via the system bind prompt), then run the
 * provider's configuration screen if it has one.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberWidgetAdder(state: WidgetState, onBound: (Int) -> Unit = { state.add(it) }): () -> Unit {
    var pickerOpen by remember { mutableStateOf(false) }
    var pendingBind by remember { mutableStateOf<PendingBind?>(null) }
    var pendingConfigureId by remember { mutableStateOf(-1) }

    val configureLauncher = rememberLauncherForActivityResult(StartActivityForResult()) { result ->
        val id = pendingConfigureId
        pendingConfigureId = -1
        if (id == -1) return@rememberLauncherForActivityResult
        if (result.resultCode == Activity.RESULT_OK) onBound(id)
        else runCatching { state.host.deleteAppWidgetId(id) }
    }

    fun proceedAfterBind(id: Int, provider: AppWidgetProviderInfo) {
        val configureComponent = provider.configure
        if (configureComponent == null) {
            onBound(id)
            return
        }
        pendingConfigureId = id
        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
            .setComponent(configureComponent)
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
        // Some configuration activities aren't exported to third-party hosts;
        // in that case just place the widget with its defaults.
        runCatching { configureLauncher.launch(intent) }.onFailure {
            pendingConfigureId = -1
            state.add(id)
        }
    }

    val bindLauncher = rememberLauncherForActivityResult(StartActivityForResult()) { result ->
        val pending = pendingBind
        pendingBind = null
        if (pending == null) return@rememberLauncherForActivityResult
        if (result.resultCode == Activity.RESULT_OK) {
            proceedAfterBind(pending.id, pending.provider)
        } else {
            runCatching { state.host.deleteAppWidgetId(pending.id) }
        }
    }

    fun startBind(provider: AppWidgetProviderInfo) {
        val id = state.host.allocateAppWidgetId()
        val bound = runCatching {
            state.manager.bindAppWidgetIdIfAllowed(id, provider.provider)
        }.getOrDefault(false)
        if (bound) {
            proceedAfterBind(id, provider)
        } else {
            pendingBind = PendingBind(id, provider)
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND)
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, provider.provider)
            runCatching { bindLauncher.launch(intent) }.onFailure {
                pendingBind = null
                runCatching { state.host.deleteAppWidgetId(id) }
            }
        }
    }

    if (pickerOpen) {
        WidgetPickerSheet(
            onDismiss = { pickerOpen = false },
            onPick = { provider ->
                pickerOpen = false
                startBind(provider)
            },
        )
    }

    return { pickerOpen = true }
}

/**
 * Renders the placed widgets, top-aligned, the way a home screen page would.
 * Long-pressing a widget toggles Pixel-style "resize mode": a frame with drag
 * handles appears around it (drag to change height) plus a Remove chip, other
 * widgets dim, and tapping anywhere outside the frame exits the mode.
 */
@Composable
fun WidgetPanel(state: WidgetState, modifier: Modifier = Modifier) {
    var resizingId by remember { mutableStateOf(-1) }
    // Reused for the "stack with another widget" action inside resize mode;
    // whatever gets picked is appended to this slot instead of becoming its
    // own standalone widget.
    var stackTargetId by remember { mutableStateOf(-1) }
    val stackAdder = rememberWidgetAdder(state) { newId ->
        state.addToStack(stackTargetId, newId)
        stackTargetId = -1
    }

    // If the widget being resized gets removed from under us, fall out of
    // resize mode instead of pointing at a stale id.
    LaunchedEffect(state.ids.toList()) {
        if (resizingId != -1 && resizingId !in state.ids) resizingId = -1
    }

    val resizeModeActive = resizingId != -1
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (resizeModeActive) {
                    Modifier.pointerInput(resizingId) {
                        detectTapGestures(onTap = { resizingId = -1 })
                    }
                } else {
                    Modifier
                },
            ),
    ) {
        state.slots.forEach { slot ->
            key(slot.first()) {
                if (slot.size == 1) {
                    val id = slot.first()
                    HostedWidget(
                        state = state,
                        id = id,
                        isResizing = resizingId == id,
                        resizeModeActive = resizeModeActive,
                        onLongPress = { resizingId = id },
                        onExitResize = { resizingId = -1 },
                        onAddToStack = {
                            stackTargetId = id
                            stackAdder()
                        },
                    )
                } else {
                    StackedWidget(
                        state = state,
                        ids = slot,
                        resizingId = resizingId,
                        resizeModeActive = resizeModeActive,
                        onLongPress = { id -> resizingId = id },
                        onExitResize = { resizingId = -1 },
                        onAddToStack = { id ->
                            stackTargetId = id
                            stackAdder()
                        },
                    )
                }
            }
        }
    }
}

/**
 * A slot holding more than one widget: swiping up on the visible widget
 * cycles to the next one in the stack (wrapping around), Pixel/iOS-style.
 */
@Composable
private fun StackedWidget(
    state: WidgetState,
    ids: List<Int>,
    resizingId: Int,
    resizeModeActive: Boolean,
    onLongPress: (Int) -> Unit,
    onExitResize: () -> Unit,
    onAddToStack: (Int) -> Unit,
) {
    var currentIndex by remember(ids.first()) { mutableIntStateOf(0) }
    val safeIndex = currentIndex.coerceIn(0, ids.lastIndex)
    var dragAccumulatorPx by remember(ids.first()) { mutableFloatStateOf(0f) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta -> dragAccumulatorPx += delta },
                    onDragStopped = {
                        if (dragAccumulatorPx <= -SWAP_DRAG_THRESHOLD_PX) {
                            currentIndex = (safeIndex + 1) % ids.size
                        }
                        dragAccumulatorPx = 0f
                    },
                ),
        ) {
            AnimatedContent(
                targetState = safeIndex,
                transitionSpec = {
                    (slideInVertically(animationSpec = tween(220)) { h -> h } togetherWith
                        slideOutVertically(animationSpec = tween(220)) { h -> -h })
                },
                label = "widget-stack-swap",
            ) { index ->
                val id = ids[index]
                HostedWidget(
                    state = state,
                    id = id,
                    isResizing = resizingId == id,
                    resizeModeActive = resizeModeActive,
                    onLongPress = { onLongPress(id) },
                    onExitResize = onExitResize,
                    onAddToStack = { onAddToStack(id) },
                )
            }
        }
        StackIndicator(
            count = ids.size,
            selectedIndex = safeIndex,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp, bottom = 4.dp),
        )
    }
}

/** Minimum upward drag (px) before a stack swipe counts as a swap. */
private const val SWAP_DRAG_THRESHOLD_PX = 120f

@Composable
private fun StackIndicator(count: Int, selectedIndex: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(count) { i ->
            val isSelected = i == selectedIndex
            Box(
                modifier = Modifier
                    .height(6.dp)
                    .width(if (isSelected) 18.dp else 6.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant,
                    ),
            )
        }
    }
}

@Composable
private fun HostedWidget(
    state: WidgetState,
    id: Int,
    isResizing: Boolean,
    resizeModeActive: Boolean,
    onLongPress: () -> Unit,
    onExitResize: () -> Unit = {},
    onAddToStack: () -> Unit = {},
) {
    val info = state.info(id) ?: return
    val heightDp = state.heightDp(id)
    val label = state.label(id)
    val context = LocalContext.current
    val density = LocalDensity.current

    // Widget hosts can throw for all sorts of provider-side reasons (stale
    // ids, revoked bind grants, a buggy remote view). Never let that crash
    // the launcher: create the view once up front and fall back to a
    // placeholder if it fails.
    val viewResult = remember(id) {
        runCatching {
            state.host.createView(context.applicationContext, id, info).apply {
                setAppWidget(id, info)
            }
        }
    }
    val view = viewResult.getOrNull()
    // RemoteViews consume every touch, so a Compose long-press detector on a
    // parent never fires; the interception happens inside the host view.
    (view as? LongPressWidgetHostView)?.onLongPressListener = onLongPress

    // Fractional drag remainders so slow, small drag deltas still accumulate
    // into whole-dp resize steps instead of being truncated away every frame.
    var topRemainder by remember(id) { mutableStateOf(0f) }
    var bottomRemainder by remember(id) { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .pointerInput(id) {
                detectTapGestures(onLongPress = { onLongPress() })
            },
    ) {
        if (view != null) {
            AndroidView(
                modifier = Modifier.fillMaxWidth().height(heightDp.dp),
                factory = { view },
                update = { v ->
                    val widthDp = v.resources.configuration.screenWidthDp - 40
                    runCatching {
                        // Bundle.EMPTY is immutable; passing it here crashes with an
                        // ArrayMap.allocArrays UnsupportedOperationException deep
                        // inside the framework. Always use a fresh Bundle.
                        @Suppress("DEPRECATION")
                        v.updateAppWidgetSize(Bundle(), widthDp, heightDp, widthDp, heightDp)
                    }
                },
            )
        } else {
            WidgetErrorPlaceholder(label = label, onRemove = { state.remove(id) })
        }

        if (resizeModeActive && !isResizing) {
            // Dim other widgets while one is in resize mode, matching Pixel's
            // "jostle" behaviour; the panel-level tap catcher exits the mode.
            Surface(
                modifier = Modifier.fillMaxWidth().height(heightDp.dp),
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.35f),
            ) {}
        }

        if (isResizing) {
            ResizeFrameOverlay(
                heightDp = heightDp,
                onDragTopPx = { deltaPx ->
                    topRemainder += with(density) { deltaPx.toDp().value }
                    val whole = topRemainder.toInt()
                    if (whole != 0) {
                        // Dragging the top handle up should grow the widget.
                        state.resize(id, -whole)
                        topRemainder -= whole
                    }
                },
                onDragBottomPx = { deltaPx ->
                    bottomRemainder += with(density) { deltaPx.toDp().value }
                    val whole = bottomRemainder.toInt()
                    if (whole != 0) {
                        state.resize(id, whole)
                        bottomRemainder -= whole
                    }
                },
                onRemove = { state.remove(id) },
                onTapInside = onExitResize,
                onStack = onAddToStack,
            )
        }
    }
}

@Composable
private fun WidgetErrorPlaceholder(label: String, onRemove: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.errorContainer,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$label couldn't load",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = onRemove) { Text("Remove") }
        }
    }
}

/* ------------------------------ Resize mode ------------------------------- */

/**
 * The Pixel-style resize frame: a rounded outline the size of the widget with
 * a drag handle centred on the top and bottom edges, and a "Remove" chip
 * floating above it. [onDragTopPx] / [onDragBottomPx] receive raw per-frame
 * vertical drag deltas in px, positive meaning the finger moved down.
 */
@Composable
private fun ResizeFrameOverlay(
    heightDp: Int,
    onDragTopPx: (Float) -> Unit,
    onDragBottomPx: (Float) -> Unit,
    onRemove: () -> Unit,
    onTapInside: () -> Unit = {},
    onStack: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxWidth().height(heightDp.dp)) {
        // While in resize mode the whole widget surface is a drag target
        // (Pixel behaviour): drag down grows, drag up shrinks. A plain tap
        // exits resize mode.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightDp.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { onTapInside() })
                }
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            onDragBottomPx(dragAmount)
                        },
                    )
                },
        )
        Row(
            modifier = Modifier.align(Alignment.TopCenter).offset(y = (-40).dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Surface(
                onClick = onStack,
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.secondaryContainer,
            ) {
                Text(
                    text = "Stack",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                )
            }
            Surface(
                onClick = onRemove,
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.errorContainer,
            ) {
                Text(
                    text = "Remove",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                )
            }
        }
        ResizeHandle(
            modifier = Modifier.align(Alignment.TopCenter).offset(y = (-6).dp),
            onDrag = onDragTopPx,
        )
        ResizeHandle(
            modifier = Modifier.align(Alignment.BottomCenter).offset(y = 6.dp),
            onDrag = onDragBottomPx,
        )
    }
}

/**
 * A pill-shaped drag handle with a generous invisible touch target around it;
 * reports raw vertical drag deltas in px.
 */
@Composable
private fun ResizeHandle(modifier: Modifier = Modifier, onDrag: (Float) -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(width = 120.dp, height = 48.dp)
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount)
                    },
                )
            },
    ) {
        Box(
            modifier = Modifier
                .size(width = 32.dp, height = 12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.primary),
        )
    }
}

/* ------------------------------ In-app picker ----------------------------- */

/** One installed widget provider, ready to display in the picker list. */
private data class WidgetPickerItem(
    val provider: AppWidgetProviderInfo,
    val label: String,
    val preview: ImageBitmap?,
    val cellWidthDp: Int,
    val cellHeightDp: Int,
)

/** Widget providers grouped by the app that publishes them. */
private data class WidgetPickerGroup(
    val packageName: String,
    val appLabel: String,
    val appIcon: ImageBitmap?,
    val items: List<WidgetPickerItem>,
)

/** Loads every installed widget provider, grouped by app, off the main thread. */
private suspend fun loadWidgetPickerGroups(context: Context): List<WidgetPickerGroup> =
    withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val manager = AppWidgetManager.getInstance(context)
        val iconCache = mutableMapOf<String, ImageBitmap?>()
        val labelCache = mutableMapOf<String, String>()

        fun appIcon(packageName: String): ImageBitmap? = iconCache.getOrPut(packageName) {
            runCatching { pm.getApplicationIcon(packageName).toBitmap().asImageBitmap() }
                .getOrNull()
        }
        fun appLabel(packageName: String): String = labelCache.getOrPut(packageName) {
            runCatching { pm.getApplicationInfo(packageName, 0).loadLabel(pm).toString() }
                .getOrDefault(packageName)
        }

        manager.installedProviders
            .groupBy { it.provider.packageName }
            .map { (packageName, providers) ->
                val items = providers.map { provider ->
                    val label = runCatching { provider.loadLabel(pm) }.getOrNull() ?: packageName
                    val preview = runCatching { provider.loadPreviewImage(context, 0) }
                        .getOrNull()
                        ?.let { runCatching { it.toBitmap().asImageBitmap() }.getOrNull() }
                        ?: appIcon(packageName)
                    WidgetPickerItem(
                        provider = provider,
                        label = label,
                        preview = preview,
                        cellWidthDp = provider.minWidth,
                        cellHeightDp = provider.minHeight,
                    )
                }.sortedBy { it.label.lowercase() }
                WidgetPickerGroup(
                    packageName = packageName,
                    appLabel = appLabel(packageName),
                    appIcon = appIcon(packageName),
                    items = items,
                )
            }
            .sortedBy { it.appLabel.lowercase() }
    }

/**
 * Pixel-style in-app widget picker: a [ModalBottomSheet] rising from the
 * bottom of the screen, initially partially expanded (roughly the lower half)
 * and draggable up to nearly full height, listing every installed widget
 * grouped by app in a lazily-scrolling list that's fully contained inside the
 * sheet itself.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WidgetPickerSheet(
    onDismiss: () -> Unit,
    onPick: (AppWidgetProviderInfo) -> Unit,
) {
    val context = LocalContext.current
    // skipPartiallyExpanded stays false (the default) so the sheet opens
    // partially expanded from the bottom and the user can drag it up further,
    // instead of snapping straight to full height or anywhere near the top.
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val groups by produceState<List<WidgetPickerGroup>?>(initialValue = null) {
        value = loadWidgetPickerGroups(context)
    }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
        ) {
            Text("Widgets", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))

            val loaded = groups
            when {
                loaded == null -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Loading widgets…",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                loaded.isEmpty() -> {
                    Text(
                        text = "No widgets available",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                else -> {
                    // A lazy list that scrolls within the sheet's own bounds,
                    // rather than forcing the sheet itself to full height.
                    LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                        loaded.forEach { group ->
                            item(key = "header:${group.packageName}") {
                                WidgetPickerGroupHeader(group)
                            }
                            items(
                                group.items,
                                key = { it.provider.provider.flattenToString() },
                            ) { pickerItem ->
                                WidgetPickerRow(
                                    item = pickerItem,
                                    onClick = { onPick(pickerItem.provider) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WidgetPickerGroupHeader(group: WidgetPickerGroup) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (group.appIcon != null) {
            Image(
                painter = BitmapPainter(group.appIcon),
                contentDescription = group.appLabel,
                modifier = Modifier.size(20.dp).clip(RoundedCornerShape(4.dp)),
            )
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = group.appLabel,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun WidgetPickerRow(item: WidgetPickerItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (item.preview != null) {
            Image(
                painter = BitmapPainter(item.preview),
                contentDescription = item.label,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(width = 96.dp, height = 64.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
        } else {
            Surface(
                modifier = Modifier.size(width = 96.dp, height = 64.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
            ) {}
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${item.cellWidthDp} x ${item.cellHeightDp} dp",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
