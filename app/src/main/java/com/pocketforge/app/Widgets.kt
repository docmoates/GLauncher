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
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Stable id for our [AppWidgetHost]; changing it orphans every bound widget. */
const val WIDGET_HOST_ID = 27713

private const val PREFS = "glauncher"
private const val KEY_IDS = "widget_ids"
private const val KEY_HEIGHT = "widget_height_"
private const val KEY_WIDTH = "widget_width_"
private const val DEFAULT_HEIGHT_DP = 160
private const val MIN_HEIGHT_DP = 80
private const val MAX_HEIGHT_DP = 640
private const val MIN_WIDTH_DP = 100
/** A widget never shrinks below this share of the panel width. */
private const val MIN_WIDTH_FRACTION = 0.25f
/** WidgetsTableUtils.MAX_ITEMS_IN_ROW: widgets are tabled 3 across. */
private const val WIDGETS_PER_ROW = 3

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

    /**
     * Set only for widgets that share a slot. Receives the total vertical
     * distance of a swipe once the finger lifts; negative is upward.
     *
     * Like the long press above, this has to be intercepted here rather than
     * with a Compose gesture on a parent: RemoteViews consume the touch
     * stream, so a draggable() wrapped around the host view never sees it.
     */
    var onStackSwipe: ((Float) -> Unit)? = null

    private var longPressFired = false
    private var swiping = false
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
                swiping = false
                downX = ev.x
                downY = ev.y
                // On a stacked widget the vertical drag belongs to the flip, so
                // stop ancestors claiming it first -- otherwise the home
                // screen's swipe-up-to-all-apps wins the gesture. Compose
                // honours this through AndroidComposeView.
                if (onStackSwipe != null) {
                    parent?.requestDisallowInterceptTouchEvent(true)
                }
                postDelayed(fireLongPress, ViewConfiguration.getLongPressTimeout().toLong())
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = abs(ev.x - downX)
                val dy = abs(ev.y - downY)
                if (dx > slop || dy > slop) {
                    removeCallbacks(fireLongPress)
                }
                // Only claim the gesture for a stack flip once it is clearly a
                // deliberate vertical drag, so a widget's own content still
                // gets ordinary taps and horizontal gestures.
                if (onStackSwipe != null && dy > slop * 2 && dy > dx) {
                    swiping = true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                removeCallbacks(fireLongPress)
                if (onStackSwipe != null) {
                    parent?.requestDisallowInterceptTouchEvent(false)
                }
            }
        }
        return longPressFired || swiping
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        // Only reached once we've intercepted (or the widget ignored the
        // event); swallow the remainder of a long-press gesture.
        if (ev.actionMasked == MotionEvent.ACTION_UP || ev.actionMasked == MotionEvent.ACTION_CANCEL) {
            removeCallbacks(fireLongPress)
        }
        if (swiping) {
            if (ev.actionMasked == MotionEvent.ACTION_UP) {
                onStackSwipe?.invoke(ev.y - downY)
            }
            if (ev.actionMasked == MotionEvent.ACTION_UP ||
                ev.actionMasked == MotionEvent.ACTION_CANCEL
            ) {
                swiping = false
            }
            return true
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
    private val widths = mutableStateMapOf<Int, Int>()

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
        heights[id] ?: prefs.getInt(KEY_HEIGHT + id, naturalHeightDp(id))

    /** The widget's requested width in dp; defaults to what its provider declares. */
    fun widthDp(id: Int): Int =
        widths[id] ?: prefs.getInt(KEY_WIDTH + id, naturalWidthDp(id))

    /** Narrows or widens a widget while a side resize handle is dragged. */
    fun resizeWidth(id: Int, deltaDp: Int) {
        val next = (widthDp(id) + deltaDp).coerceAtLeast(MIN_WIDTH_DP)
        // Everything sharing a slot keeps one footprint -- that identical size
        // is what lets the flip between them look seamless.
        slotMembers(id).forEach { setWidthDp(it, next) }
    }

    private fun setWidthDp(id: Int, value: Int) {
        widths[id] = value
        prefs.edit().putInt(KEY_WIDTH + id, value).apply()
    }

    private val density: Float get() = appContext.resources.displayMetrics.density

    /**
     * AppWidgetProviderInfo.minWidth/minHeight are in PIXELS, so they have to be
     * converted before being used as dp. Sizing widgets from these is what stops
     * them rendering squashed: a widget forced well under its declared size just
     * crushes its own layout.
     */
    private fun naturalWidthDp(id: Int): Int =
        info(id)?.let { (it.minWidth / density).toInt() }?.coerceAtLeast(MIN_WIDTH_DP)
            ?: MIN_WIDTH_DP

    private fun naturalHeightDp(id: Int): Int =
        info(id)?.let { (it.minHeight / density).toInt() }
            ?.coerceIn(MIN_HEIGHT_DP, MAX_HEIGHT_DP)
            ?: DEFAULT_HEIGHT_DP

    /** Gives a freshly placed widget the size its provider asks for. */
    private fun applyNaturalSize(id: Int) {
        setHeightDp(id, naturalHeightDp(id))
        setWidthDp(id, naturalWidthDp(id))
    }

    private fun setHeightDp(id: Int, value: Int) {
        heights[id] = value
        prefs.edit().putInt(KEY_HEIGHT + id, value).apply()
    }

    /** Every widget sharing a home-screen slot with [id], including itself. */
    private fun slotMembers(id: Int): List<Int> =
        _slots.firstOrNull { id in it } ?: listOf(id)

    /** Index of the home-screen slot holding [id], or -1. */
    fun slotIndexOf(id: Int): Int = _slots.indexOfFirst { id in it }

    /** Moves a whole slot up or down the panel. */
    fun moveSlot(fromIndex: Int, toIndex: Int) {
        if (fromIndex !in _slots.indices || toIndex !in _slots.indices) return
        val slot = _slots.removeAt(fromIndex)
        _slots.add(toIndex, slot)
        persist()
    }

    /** Adjusts a widget's height by [deltaDp], clamped to the allowed range. Called
     *  continuously while the user drags a resize handle, so small deltas are fine. */
    fun resize(id: Int, deltaDp: Int) {
        val next = (heightDp(id) + deltaDp).coerceIn(MIN_HEIGHT_DP, MAX_HEIGHT_DP)
        slotMembers(id).forEach { setHeightDp(it, next) }
    }

    /** Places [id] on the home screen as its own new slot. */
    fun add(id: Int) {
        if (id !in ids) {
            _slots.add(listOf(id))
            applyNaturalSize(id)
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
        applyNaturalSize(newId)
        // Stacks hold one shared footprint. Size it to the largest member
        // rather than the anchor, so joining a stack never crushes a widget
        // below the size its provider asked for.
        val members = _slots[index]
        val sharedHeight = members.maxOf { heightDp(it) }
        val sharedWidth = members.maxOf { widthDp(it) }
        members.forEach {
            setHeightDp(it, sharedHeight)
            setWidthDp(it, sharedWidth)
        }
        persist()
    }

    fun remove(id: Int) {
        val index = _slots.indexOfFirst { id in it }
        if (index == -1) return
        val remaining = _slots[index] - id
        if (remaining.isEmpty()) _slots.removeAt(index) else _slots[index] = remaining
        heights.remove(id)
        widths.remove(id)
        prefs.edit().remove(KEY_HEIGHT + id).remove(KEY_WIDTH + id).apply()
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

/**
 * One widget id whose bind result we're waiting on via the system bind prompt.
 * [anchorId] is the widget it should stack onto, or -1 to stand alone -- it
 * travels with the request because the bind and configure steps bounce through
 * other activities before the result comes back.
 */
private data class PendingBind(
    val id: Int,
    val provider: AppWidgetProviderInfo,
    val anchorId: Int,
)

/**
 * Returns a callback that opens the in-app widget picker. Picking a widget
 * there runs the full "add a widget" flow: allocate an id, bind it (silently
 * if already allowed, otherwise via the system bind prompt), then run the
 * provider's configuration screen if it has one.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberWidgetAdder(
    state: WidgetState,
    onBound: (anchorId: Int, newId: Int) -> Unit = { _, newId -> state.add(newId) },
): (Int) -> Unit {
    var pickerOpen by remember { mutableStateOf(false) }
    var pendingBind by remember { mutableStateOf<PendingBind?>(null) }
    var pendingConfigure by remember { mutableStateOf<PendingBind?>(null) }
    // Anchor for the pick currently in flight; -1 means "place standalone".
    var anchorForPick by remember { mutableStateOf(-1) }

    val configureLauncher = rememberLauncherForActivityResult(StartActivityForResult()) { result ->
        val pending = pendingConfigure
        pendingConfigure = null
        if (pending == null) return@rememberLauncherForActivityResult
        if (result.resultCode == Activity.RESULT_OK) onBound(pending.anchorId, pending.id)
        else runCatching { state.host.deleteAppWidgetId(pending.id) }
    }

    fun proceedAfterBind(id: Int, provider: AppWidgetProviderInfo, anchorId: Int) {
        val configureComponent = provider.configure
        if (configureComponent == null) {
            onBound(anchorId, id)
            return
        }
        pendingConfigure = PendingBind(id, provider, anchorId)
        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
            .setComponent(configureComponent)
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
        // Some configuration activities aren't exported to third-party hosts;
        // in that case just place the widget with its defaults.
        runCatching { configureLauncher.launch(intent) }.onFailure {
            pendingConfigure = null
            onBound(anchorId, id)
        }
    }

    val bindLauncher = rememberLauncherForActivityResult(StartActivityForResult()) { result ->
        val pending = pendingBind
        pendingBind = null
        if (pending == null) return@rememberLauncherForActivityResult
        if (result.resultCode == Activity.RESULT_OK) {
            proceedAfterBind(pending.id, pending.provider, pending.anchorId)
        } else {
            runCatching { state.host.deleteAppWidgetId(pending.id) }
        }
    }

    fun startBind(provider: AppWidgetProviderInfo, anchorId: Int) {
        val id = state.host.allocateAppWidgetId()
        val bound = runCatching {
            state.manager.bindAppWidgetIdIfAllowed(id, provider.provider)
        }.getOrDefault(false)
        if (bound) {
            proceedAfterBind(id, provider, anchorId)
        } else {
            pendingBind = PendingBind(id, provider, anchorId)
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
                startBind(provider, anchorForPick)
            },
        )
    }

    return { anchorId ->
        anchorForPick = anchorId
        pickerOpen = true
    }
}

/**
 * Reopens a placed widget's own configuration screen, for providers that have
 * one (Launcher3 exposes the same "reconfigure" action on a placed widget).
 */
@Composable
fun rememberWidgetReconfigurer(state: WidgetState): (Int) -> Unit {
    val launcher = rememberLauncherForActivityResult(StartActivityForResult()) { }
    return remember(state, launcher) {
        { id: Int ->
            val configure = state.info(id)?.configure
            if (configure != null) {
                val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
                    .setComponent(configure)
                    .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
                runCatching { launcher.launch(intent) }
            }
        }
    }
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
    val stackAdder = rememberWidgetAdder(state) { anchorId, newId ->
        if (anchorId == -1) state.add(newId) else state.addToStack(anchorId, newId)
    }
    val reconfigure = rememberWidgetReconfigurer(state)

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
                        detectTapGestures(
                            // Claim long presses too. Without this, the release
                            // of the very long press that opened resize mode is
                            // reported as a tap and closes it again instantly.
                            onLongPress = {},
                            onTap = { resizingId = -1 },
                        )
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
                        onAddToStack = { stackAdder(id) },
                        onReconfigure = { reconfigure(id) },
                        onMove = { delta -> moveWidgetSlot(state, id, delta) },
                    )
                } else {
                    StackedWidget(
                        state = state,
                        ids = slot,
                        resizingId = resizingId,
                        resizeModeActive = resizeModeActive,
                        onLongPress = { id -> resizingId = id },
                        onExitResize = { resizingId = -1 },
                        onAddToStack = { id -> stackAdder(id) },
                        onReconfigure = { id -> reconfigure(id) },
                        onMove = { id, delta -> moveWidgetSlot(state, id, delta) },
                    )
                }
            }
        }
    }
}

/** Moves the slot containing [id] by [delta] positions, clamped to the panel. */
private fun moveWidgetSlot(state: WidgetState, id: Int, delta: Int) {
    val from = state.slotIndexOf(id)
    if (from == -1) return
    state.moveSlot(from, (from + delta).coerceIn(0, state.slots.lastIndex))
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
    onReconfigure: (Int) -> Unit,
    onMove: (Int, Int) -> Unit,
) {
    var currentIndex by remember(ids.first()) { mutableIntStateOf(0) }
    val safeIndex = currentIndex.coerceIn(0, ids.lastIndex)
    var dragAccumulatorPx by remember(ids.first()) { mutableFloatStateOf(0f) }
    // Which way the last flip went, so the animation slides the way the
    // finger did instead of always coming up from the bottom.
    var flippingForward by remember(ids.first()) { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .draggable(
                    orientation = Orientation.Vertical,
                    // While a widget is being resized the frame owns the drag.
                    enabled = !resizeModeActive,
                    state = rememberDraggableState { delta -> dragAccumulatorPx += delta },
                    onDragStopped = {
                        when {
                            // Swipe up: next widget in the stack.
                            dragAccumulatorPx <= -SWAP_DRAG_THRESHOLD_PX -> {
                                flippingForward = true
                                currentIndex = (safeIndex + 1) % ids.size
                            }
                            // Swipe down: previous one, wrapping backwards.
                            dragAccumulatorPx >= SWAP_DRAG_THRESHOLD_PX -> {
                                flippingForward = false
                                currentIndex = (safeIndex - 1 + ids.size) % ids.size
                            }
                        }
                        dragAccumulatorPx = 0f
                    },
                ),
        ) {
            AnimatedContent(
                targetState = safeIndex,
                transitionSpec = {
                    if (flippingForward) {
                        slideInVertically(animationSpec = tween(220)) { h -> h } togetherWith
                            slideOutVertically(animationSpec = tween(220)) { h -> -h }
                    } else {
                        slideInVertically(animationSpec = tween(220)) { h -> -h } togetherWith
                            slideOutVertically(animationSpec = tween(220)) { h -> h }
                    }
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
                    onReconfigure = { onReconfigure(id) },
                    onMove = { delta -> onMove(id, delta) },
                    onStackSwipe = { totalDy ->
                        when {
                            totalDy <= -SWAP_DRAG_THRESHOLD_PX -> {
                                flippingForward = true
                                currentIndex = (safeIndex + 1) % ids.size
                            }
                            totalDy >= SWAP_DRAG_THRESHOLD_PX -> {
                                flippingForward = false
                                currentIndex = (safeIndex - 1 + ids.size) % ids.size
                            }
                        }
                    },
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
    onReconfigure: () -> Unit = {},
    onMove: (Int) -> Unit = {},
    onStackSwipe: ((Float) -> Unit)? = null,
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
    (view as? LongPressWidgetHostView)?.onStackSwipe = onStackSwipe

    // Fractional drag remainders so slow, small drag deltas still accumulate
    // into whole-dp resize steps instead of being truncated away every frame.
    var topRemainder by remember(id) { mutableStateOf(0f) }
    var bottomRemainder by remember(id) { mutableStateOf(0f) }
    val requestedWidthDp = state.widthDp(id)
    // Side handles report px; converting against the panel width keeps the
    // fraction-based width model independent of screen size.
    var panelWidthPx by remember(id) { mutableStateOf(1f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .onSizeChanged { panelWidthPx = it.width.toFloat().coerceAtLeast(1f) },
        contentAlignment = Alignment.Center,
    ) {
      val widthFraction = with(density) {
          (requestedWidthDp.dp.toPx() / panelWidthPx).coerceIn(MIN_WIDTH_FRACTION, 1f)
      }
      Box(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
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
                onReconfigure = onReconfigure.takeIf { info.configure != null },
                onMove = onMove,
                onDragLeftPx = { deltaPx ->
                    state.resizeWidth(id, -with(density) { deltaPx.toDp().value }.toInt())
                },
                onDragRightPx = { deltaPx ->
                    state.resizeWidth(id, with(density) { deltaPx.toDp().value }.toInt())
                },
            )
        }
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
    onReconfigure: (() -> Unit)? = null,
    onMove: (Int) -> Unit = {},
    onDragLeftPx: (Float) -> Unit = {},
    onDragRightPx: (Float) -> Unit = {},
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
            if (onReconfigure != null) {
                ResizeChip("Configure", onReconfigure)
            }
            ResizeChip("Stack", onStack)
            ResizeChip("Remove", onRemove, destructive = true)
        }
        Row(
            modifier = Modifier.align(Alignment.BottomCenter).offset(y = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ResizeChip("Move up", onClick = { onMove(-1) })
            ResizeChip("Move down", onClick = { onMove(1) })
        }
        ResizeHandle(
            modifier = Modifier.align(Alignment.TopCenter).offset(y = (-6).dp),
            onDrag = onDragTopPx,
        )
        ResizeHandle(
            modifier = Modifier.align(Alignment.BottomCenter).offset(y = 6.dp),
            onDrag = onDragBottomPx,
        )
        // Side handles narrow/widen the widget, the horizontal counterpart of
        // Launcher3's spanX resizing.
        SideResizeHandle(
            modifier = Modifier.align(Alignment.CenterStart).offset(x = (-6).dp),
            onDrag = onDragLeftPx,
        )
        SideResizeHandle(
            modifier = Modifier.align(Alignment.CenterEnd).offset(x = 6.dp),
            onDrag = onDragRightPx,
        )
    }
}

@Composable
private fun ResizeChip(label: String, onClick: () -> Unit, destructive: Boolean = false) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (destructive) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.secondaryContainer
        },
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (destructive) {
                MaterialTheme.colorScheme.onErrorContainer
            } else {
                MaterialTheme.colorScheme.onSecondaryContainer
            },
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
        )
    }
}

/** Left/right counterpart of [ResizeHandle]; reports horizontal drag deltas. */
@Composable
private fun SideResizeHandle(modifier: Modifier = Modifier, onDrag: (Float) -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(width = 48.dp, height = 120.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount)
                    },
                )
            },
    ) {
        Box(
            modifier = Modifier
                .size(width = 12.dp, height = 32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.primary),
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

/** Search field at the top of the widget picker. */
@Composable
private fun WidgetSearchField(query: String, onQueryChange: (String) -> Unit) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth().height(48.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.width(12.dp))
            Box(Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        text = "Search widgets",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

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
        val displayDensity = context.resources.displayMetrics.density
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
                        cellWidthDp = (provider.minWidth / displayDensity).toInt(),
                        cellHeightDp = (provider.minHeight / displayDensity).toInt(),
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
    var query by remember { mutableStateOf("") }
    var expandedPackages by remember { mutableStateOf(emptySet<String>()) }

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
            WidgetSearchField(query = query, onQueryChange = { query = it })
            Spacer(Modifier.height(12.dp))

            val loaded = groups?.let { all ->
                val q = query.trim()
                if (q.isEmpty()) {
                    all
                } else {
                    // Match either the app or the individual widget, keeping
                    // only the matching widgets within a group.
                    all.mapNotNull { group ->
                        if (group.appLabel.contains(q, ignoreCase = true)) {
                            group
                        } else {
                            val items = group.items.filter {
                                it.label.contains(q, ignoreCase = true)
                            }
                            if (items.isEmpty()) null else group.copy(items = items)
                        }
                    }
                }
            }
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
                        text = if (query.isBlank()) {
                            "No widgets available"
                        } else {
                            "No widgets match \"${query.trim()}\""
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                else -> {
                    // A lazy list that scrolls within the sheet's own bounds,
                    // rather than forcing the sheet itself to full height.
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            // widget_list_horizontal_margin
                            .padding(horizontal = 11.dp),
                    ) {
                        loaded.forEach { group ->
                            val expanded = group.packageName in expandedPackages ||
                                query.isNotBlank()
                            item(key = "header:${group.packageName}") {
                                WidgetPickerGroupHeader(
                                    group = group,
                                    expanded = expanded,
                                    onToggle = {
                                        expandedPackages = if (expanded) {
                                            expandedPackages - group.packageName
                                        } else {
                                            expandedPackages + group.packageName
                                        }
                                    },
                                )
                            }
                            if (expanded) {
                                // Launcher3 lays widgets out in a table of at
                                // most MAX_ITEMS_IN_ROW (3) equal-weight cells.
                                val rows = group.items.chunked(WIDGETS_PER_ROW)
                                items(
                                    rows,
                                    key = { row -> "row:" + row.first().provider.provider.flattenToString() },
                                ) { row ->
                                    WidgetCellRow(row = row, onPick = onPick)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WidgetPickerGroupHeader(
    group: WidgetPickerGroup,
    expanded: Boolean,
    onToggle: () -> Unit,
) {
    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
    ) {
        Row(
            // widget_list_header_view_vertical_padding
            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(Modifier.width(16.dp))
            if (group.appIcon != null) {
                Image(
                    painter = BitmapPainter(group.appIcon),
                    contentDescription = null,
                    // launcher:appIconSize
                    modifier = Modifier.size(48.dp),
                )
            } else {
                Spacer(Modifier.size(48.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = group.appLabel,
                    // WidgetListHeader.Title: 16sp, weight 500
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = if (group.items.size == 1) "1 widget" else "${group.items.size} widgets",
                    // WidgetListHeader.SubTitle: 14sp, weight 400
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(Modifier.width(16.dp))
            Icon(
                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                // widgets_tray_expand_button is drawn at .6 alpha
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            )
            Spacer(Modifier.width(16.dp))
        }
    }
}

/** Up to [WIDGETS_PER_ROW] equal-width cells, mirroring WidgetsListTableView. */
@Composable
private fun WidgetCellRow(row: List<WidgetPickerItem>, onPick: (AppWidgetProviderInfo) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        row.forEach { item ->
            Box(Modifier.weight(1f)) {
                WidgetPickerCell(item = item, onClick = { onPick(item.provider) })
            }
        }
        // Keep a part-filled last row aligned with the full rows above it.
        repeat(WIDGETS_PER_ROW - row.size) {
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun WidgetPickerCell(item: WidgetPickerItem, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            // widget_cell_horizontal_padding / widget_cell_vertical_padding
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (item.preview != null) {
                Image(
                    painter = BitmapPainter(item.preview),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        Text(
            text = item.label,
            // widget_cell_title_font_size
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "${item.cellWidthDp} x ${item.cellHeightDp} dp",
            // widget_cell_dims_font_size
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}
