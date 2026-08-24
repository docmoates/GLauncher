package com.pocketforge.app

import android.app.Activity
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.os.Build
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
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

/* Resize frame metrics, taken from Launcher3's app_widget_resize_frame.xml:
 * the handle asset ic_widget_resize_handle is 19dp (76px at xxxhdpi), the
 * frame is a 2dp stroke at the system widget background radius, and
 * widget_handle_margin (13dp) against resize_frame_margin (23dp) puts each
 * handle's centre on the frame line rather than outside it. */
private val RESIZE_DOT_SIZE = 19.dp
private val RESIZE_DOT_TOUCH_TARGET = 48.dp
private val RESIZE_FRAME_STROKE = 2.dp
private val RESIZE_FRAME_RADIUS = 28.dp

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

    /** Reports the finger's screen position while dragging a lifted widget. */
    var onDragMove: ((Float, Float) -> Unit)? = null

    /** The drag finished; the listener decides where the widget landed. */
    var onDragEnd: (() -> Unit)? = null

    private var longPressFired = false
    private var swiping = false
    private var downX = 0f
    private var downY = 0f
    private val fireLongPress = Runnable {
        longPressFired = true
        onLongPressListener?.invoke()
    }

    /**
     * All gesture handling happens here rather than in
     * onInterceptTouchEvent/onTouchEvent. Intercepting only becomes possible
     * once an event arrives after the long-press timer fires, and with a still
     * finger the next event is ACTION_UP -- which Android converts into a
     * cancel for the child and never routes to the parent's onTouchEvent, so
     * the release was being lost entirely. dispatchTouchEvent sees every event
     * unconditionally.
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val slop = ViewConfiguration.get(context).scaledTouchSlop
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                longPressFired = false
                swiping = false
                downX = ev.x
                downY = ev.y
                // Widgets own their vertical gestures (flip a stack, drag to
                // move), so ancestors must not claim them first -- otherwise
                // the home screen's swipe-up-to-all-apps wins.
                parent?.requestDisallowInterceptTouchEvent(true)
                postDelayed(fireLongPress, ViewConfiguration.getLongPressTimeout().toLong())
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = abs(ev.x - downX)
                val dy = abs(ev.y - downY)
                if (!longPressFired && (dx > slop || dy > slop)) {
                    removeCallbacks(fireLongPress)
                }
                if (longPressFired) {
                    onDragMove?.invoke(ev.rawX, ev.rawY)
                    return true
                }
                // Only claim a stack flip once it is clearly a deliberate
                // vertical drag, so the widget's own content keeps ordinary
                // taps and horizontal gestures.
                if (onStackSwipe != null && dy > slop * 2 && dy > dx) {
                    swiping = true
                }
                if (swiping) return true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                removeCallbacks(fireLongPress)
                parent?.requestDisallowInterceptTouchEvent(false)
                if (longPressFired) {
                    longPressFired = false
                    onDragEnd?.invoke()
                    return true
                }
                if (swiping) {
                    if (ev.actionMasked == MotionEvent.ACTION_UP) {
                        onStackSwipe?.invoke(ev.y - downY)
                    }
                    swiping = false
                    return true
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * Widget content (scrollable lists, grids) asks its host to stop
     * intercepting as soon as it starts scrolling. Honouring that would stop
     * onInterceptTouchEvent being consulted at all, so long-press-to-drag and
     * the stack flip would never see the rest of the gesture. Keep our own
     * interception rights and just pass the request up the tree.
     */
    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        parent?.requestDisallowInterceptTouchEvent(disallowIntercept)
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
        if (!canResizeHorizontally(id)) return
        val lower = slotMembers(id).maxOf { minResizeWidthDp(it) }
        val upper = slotMembers(id).minOf { maxResizeWidthDp(it) }.coerceAtLeast(lower)
        val next = (widthDp(id) + deltaDp).coerceIn(lower, upper)
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

    /**
     * Launcher3 only shows the handles for axes a widget actually supports
     * (AppWidgetResizeFrame checks resizeMode before hiding them), so a widget
     * that declares no vertical resizing shouldn't offer a vertical dot.
     */
    fun canResizeVertically(id: Int): Boolean =
        info(id)?.let { it.resizeMode and AppWidgetProviderInfo.RESIZE_VERTICAL != 0 } ?: false

    fun canResizeHorizontally(id: Int): Boolean =
        info(id)?.let { it.resizeMode and AppWidgetProviderInfo.RESIZE_HORIZONTAL != 0 } ?: false

    /** Smallest size the provider will accept, falling back to its natural size. */
    private fun minResizeWidthDp(id: Int): Int {
        val i = info(id) ?: return MIN_WIDTH_DP
        val px = if (i.minResizeWidth > 0) i.minResizeWidth else i.minWidth
        return (px / density).toInt().coerceAtLeast(MIN_WIDTH_DP)
    }

    private fun minResizeHeightDp(id: Int): Int {
        val i = info(id) ?: return MIN_HEIGHT_DP
        val px = if (i.minResizeHeight > 0) i.minResizeHeight else i.minHeight
        return (px / density).toInt().coerceIn(MIN_HEIGHT_DP, MAX_HEIGHT_DP)
    }

    private fun maxResizeWidthDp(id: Int): Int {
        val i = info(id) ?: return Int.MAX_VALUE
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || i.maxResizeWidth <= 0) {
            return Int.MAX_VALUE
        }
        return (i.maxResizeWidth / density).toInt()
    }

    private fun maxResizeHeightDp(id: Int): Int {
        val i = info(id) ?: return MAX_HEIGHT_DP
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || i.maxResizeHeight <= 0) {
            return MAX_HEIGHT_DP
        }
        return (i.maxResizeHeight / density).toInt().coerceAtMost(MAX_HEIGHT_DP)
    }

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

    /**
     * Drops [id] onto [targetId]'s slot, the iOS-style "drag one widget on top
     * of another to stack them" gesture. The moved widget leaves its old slot,
     * and the combined slot resizes to its largest member.
     */
    fun stackOnto(id: Int, targetId: Int) {
        if (id == targetId) return
        val from = _slots.indexOfFirst { id in it }
        val to = _slots.indexOfFirst { targetId in it }
        if (from == -1 || to == -1 || from == to) return
        _slots[from] = _slots[from] - id
        _slots[to] = _slots[to] + id
        if (_slots[from].isEmpty()) _slots.removeAt(from)
        val members = _slots.first { targetId in it }
        val sharedHeight = members.maxOf { heightDp(it) }
        val sharedWidth = members.maxOf { widthDp(it) }
        members.forEach {
            setHeightDp(it, sharedHeight)
            setWidthDp(it, sharedWidth)
        }
        persist()
    }

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
        if (!canResizeVertically(id)) return
        val lower = slotMembers(id).maxOf { minResizeHeightDp(it) }
        val upper = slotMembers(id).minOf { maxResizeHeightDp(it) }.coerceAtLeast(lower)
        val next = (heightDp(id) + deltaDp).coerceIn(lower, upper)
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
    // The lifted widget and where the finger currently is, in screen coords.
    var draggingId by remember { mutableStateOf(-1) }
    var dragStart by remember { mutableStateOf<Offset?>(null) }
    var dragNow by remember { mutableStateOf(Offset.Zero) }
    var moved by remember { mutableStateOf(false) }
    // Where each slot sits on screen, so a drop can be matched to a target.
    val slotBounds = remember { mutableStateMapOf<Int, Rect>() }
    var removeBounds by remember { mutableStateOf(Rect.Zero) }

    LaunchedEffect(state.ids.toList()) {
        if (resizingId != -1 && resizingId !in state.ids) resizingId = -1
    }

    fun finishDrag() {
        val dragged = draggingId
        val pointer = dragNow
        draggingId = -1
        dragStart = null
        if (dragged == -1) return
        if (!moved) {
            // A lift with no movement just opens resize mode.
            resizingId = dragged
            return
        }
        when {
            removeBounds.contains(pointer) -> state.remove(dragged)
            else -> {
                val target = slotBounds.entries
                    .firstOrNull { (first, rect) -> rect.contains(pointer) && first != dragged }
                if (target != null) state.stackOnto(dragged, target.key)
            }
        }
    }

    val resizeModeActive = resizingId != -1
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (resizeModeActive) {
                    Modifier.pointerInput(resizingId) {
                        detectTapGestures(
                            // Claim long presses too, otherwise the release of
                            // the press that opened resize mode reads as a tap
                            // and closes it again immediately.
                            onLongPress = {},
                            onTap = { resizingId = -1 },
                        )
                    }
                } else {
                    Modifier
                },
            ),
    ) {
        if (draggingId != -1) {
            RemoveDropTarget(
                active = removeBounds.contains(dragNow),
                modifier = Modifier.onGloballyPositioned { removeBounds = it.boundsInWindow() },
            )
        }

        state.slots.forEach { slot ->
            key(slot.first()) {
                val anchor = slot.first()
                Box(
                    modifier = Modifier.onGloballyPositioned {
                        slotBounds[anchor] = it.boundsInWindow()
                    },
                ) {
                    val dropTarget = draggingId != -1 &&
                        draggingId !in slot &&
                        slotBounds[anchor]?.contains(dragNow) == true

                    val commonDrag: (Int) -> Pair<(Float, Float) -> Unit, () -> Unit> = { id ->
                        Pair(
                            { rawX: Float, rawY: Float ->
                                val point = Offset(rawX, rawY)
                                if (dragStart == null) dragStart = point
                                dragNow = point
                                val from = dragStart
                                if (from != null && (point - from).getDistance() > 24f) moved = true
                            },
                            { finishDrag() },
                        )
                    }

                    if (slot.size == 1) {
                        val id = anchor
                        val (onMove, onEnd) = commonDrag(id)
                        HostedWidget(
                            state = state,
                            id = id,
                            isResizing = resizingId == id,
                            resizeModeActive = resizeModeActive,
                            isDragging = draggingId == id,
                            isDropTarget = dropTarget,
                            dragOffset = if (draggingId == id) dragNow - (dragStart ?: dragNow)
                                else Offset.Zero,
                            onLongPress = {
                                draggingId = id
                                dragStart = null
                                moved = false
                            },
                            onExitResize = { resizingId = -1 },
                            onDragMove = onMove,
                            onDragEnd = onEnd,
                        )
                    } else {
                        StackedWidget(
                            state = state,
                            ids = slot,
                            resizingId = resizingId,
                            resizeModeActive = resizeModeActive,
                            draggingId = draggingId,
                            isDropTarget = dropTarget,
                            dragOffset = dragNow - (dragStart ?: dragNow),
                            onLongPress = { id ->
                                draggingId = id
                                dragStart = null
                                moved = false
                            },
                            onExitResize = { resizingId = -1 },
                            onDragMove = { rawX, rawY ->
                                val point = Offset(rawX, rawY)
                                if (dragStart == null) dragStart = point
                                dragNow = point
                                val from = dragStart
                                if (from != null && (point - from).getDistance() > 24f) moved = true
                            },
                            onDragEnd = { finishDrag() },
                        )
                    }
                }
            }
        }
    }
}

/** Drop here to delete, the way Pixel shows a Remove target while dragging. */
@Composable
private fun RemoveDropTarget(active: Boolean, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(50),
        color = if (active) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
        },
        modifier = modifier
            .padding(bottom = 12.dp)
            .height(48.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Remove",
                style = MaterialTheme.typography.labelLarge,
                color = if (active) {
                    MaterialTheme.colorScheme.onErrorContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            )
        }
    }
}

/**
 * A slot holding more than one widget: swiping up on the visible widget
 * cycles to the next one in the stack, swiping down goes back.
 */
@Composable
private fun StackedWidget(
    state: WidgetState,
    ids: List<Int>,
    resizingId: Int,
    resizeModeActive: Boolean,
    draggingId: Int,
    isDropTarget: Boolean,
    dragOffset: Offset,
    onLongPress: (Int) -> Unit,
    onExitResize: () -> Unit,
    onDragMove: (Float, Float) -> Unit,
    onDragEnd: () -> Unit,
) {
    var currentIndex by remember(ids.first()) { mutableIntStateOf(0) }
    val safeIndex = currentIndex.coerceIn(0, ids.lastIndex)
    // Which way the last flip went, so the animation slides the way the
    // finger did instead of always entering from the bottom.
    var flippingForward by remember(ids.first()) { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
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
                    isDragging = draggingId == id,
                    isDropTarget = isDropTarget,
                    dragOffset = if (draggingId == id) dragOffset else Offset.Zero,
                    onLongPress = { onLongPress(id) },
                    onExitResize = onExitResize,
                    onDragMove = onDragMove,
                    onDragEnd = onDragEnd,
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

/** Minimum vertical drag (px) before a stack swipe counts as a flip. */
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
    isDragging: Boolean = false,
    isDropTarget: Boolean = false,
    dragOffset: Offset = Offset.Zero,
    onLongPress: () -> Unit,
    onExitResize: () -> Unit = {},
    onDragMove: (Float, Float) -> Unit = { _, _ -> },
    onDragEnd: () -> Unit = {},
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
    // RemoteViews consume every touch, so Compose gesture detectors on a
    // parent never fire; long press, drag and flip are all intercepted inside
    // the host view instead.
    (view as? LongPressWidgetHostView)?.let {
        it.onLongPressListener = onLongPress
        it.onStackSwipe = onStackSwipe
        it.onDragMove = onDragMove
        it.onDragEnd = onDragEnd
    }

    var topRemainder by remember(id) { mutableStateOf(0f) }
    var bottomRemainder by remember(id) { mutableStateOf(0f) }
    val requestedWidthDp = state.widthDp(id)
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
            .graphicsLayer {
                // The lifted widget follows the finger and floats above the rest.
                translationX = dragOffset.x
                translationY = dragOffset.y
                val lift = if (isDragging) 1.06f else 1f
                scaleX = lift
                scaleY = lift
                alpha = if (isDragging) 0.92f else 1f
            }
            .then(
                if (isDropTarget) {
                    Modifier.border(
                        3.dp,
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(20.dp),
                    )
                } else {
                    Modifier
                },
            ),
    ) {
        if (view != null) {
            AndroidView(
                modifier = Modifier.fillMaxWidth().height(heightDp.dp),
                factory = { view },
                update = { v ->
                    val widthDp = with(density) { (panelWidthPx * widthFraction).toDp().value }
                    runCatching {
                        // Bundle.EMPTY is immutable; passing it here crashes with an
                        // ArrayMap.allocArrays UnsupportedOperationException deep
                        // inside the framework. Always use a fresh Bundle.
                        @Suppress("DEPRECATION")
                        v.updateAppWidgetSize(
                            Bundle(),
                            widthDp.toInt(),
                            heightDp,
                            widthDp.toInt(),
                            heightDp,
                        )
                    }
                },
            )
        } else {
            WidgetErrorPlaceholder(label = label, onRemove = { state.remove(id) })
        }

        if (resizeModeActive && !isResizing) {
            Surface(
                modifier = Modifier.fillMaxWidth().height(heightDp.dp),
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.35f),
            ) {}
        }

        if (isResizing) {
            ResizeFrameOverlay(
                heightDp = heightDp,
                verticalResizeActive = state.canResizeVertically(id),
                horizontalResizeActive = state.canResizeHorizontally(id),
                onTapInside = onExitResize,
                onDragTopPx = { deltaPx ->
                    topRemainder += with(density) { deltaPx.toDp().value }
                    val whole = topRemainder.toInt()
                    if (whole != 0) {
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
 * The resize frame: a rounded outline around the widget with a round dot
 * handle centred on each edge, dragged to resize. Nothing else -- moving,
 * stacking and removing are all done by dragging the widget itself.
 */
@Composable
private fun ResizeFrameOverlay(
    heightDp: Int,
    verticalResizeActive: Boolean,
    horizontalResizeActive: Boolean,
    onDragTopPx: (Float) -> Unit,
    onDragBottomPx: (Float) -> Unit,
    onDragLeftPx: (Float) -> Unit,
    onDragRightPx: (Float) -> Unit,
    onTapInside: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxWidth().height(heightDp.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightDp.dp)
                .border(
                    RESIZE_FRAME_STROKE,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(RESIZE_FRAME_RADIUS),
                )
                .pointerInput(Unit) {
                    detectTapGestures(onLongPress = {}, onTap = { onTapInside() })
                },
        )
        // The touch targets stay wholly inside the frame -- Compose bounds hit
        // testing to the parent, so a handle centred on the edge would only be
        // grabbable on its inner half. Launcher3 avoids this by making its frame
        // container larger than the widget (resize_frame_margin); here the frame
        // matches the widget, so the dot is drawn against the edge instead.
        if (verticalResizeActive) {
            DotHandle(
                modifier = Modifier.align(Alignment.TopCenter),
                vertical = true,
                dotAlignment = Alignment.TopCenter,
                onDrag = onDragTopPx,
            )
            DotHandle(
                modifier = Modifier.align(Alignment.BottomCenter),
                vertical = true,
                dotAlignment = Alignment.BottomCenter,
                onDrag = onDragBottomPx,
            )
        }
        if (horizontalResizeActive) {
            DotHandle(
                modifier = Modifier.align(Alignment.CenterStart),
                vertical = false,
                dotAlignment = Alignment.CenterStart,
                onDrag = onDragLeftPx,
            )
            DotHandle(
                modifier = Modifier.align(Alignment.CenterEnd),
                vertical = false,
                dotAlignment = Alignment.CenterEnd,
                onDrag = onDragRightPx,
            )
        }
    }
}

/** A round resize dot with a generous invisible touch target around it. */
@Composable
private fun DotHandle(
    modifier: Modifier = Modifier,
    vertical: Boolean,
    dotAlignment: Alignment = Alignment.Center,
    onDrag: (Float) -> Unit,
) {
    Box(
        contentAlignment = dotAlignment,
        modifier = modifier
            .size(RESIZE_DOT_TOUCH_TARGET)
            .pointerInput(vertical) {
                if (vertical) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            onDrag(dragAmount)
                        },
                    )
                } else {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            onDrag(dragAmount)
                        },
                    )
                }
            },
    ) {
        Box(
            modifier = Modifier
                .size(RESIZE_DOT_SIZE)
                .clip(CircleShape)
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
