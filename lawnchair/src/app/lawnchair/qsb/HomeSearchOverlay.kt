package app.lawnchair.qsb

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import com.android.launcher3.views.BaseDragLayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.lawnchair.LawnchairLauncher
import app.lawnchair.search.adapter.SearchAdapterItem
import app.lawnchair.search.adapter.SPACE
import app.lawnchair.search.adapter.SPACE_MINI
import app.lawnchair.search.adapter.SearchTargetCompat
import app.lawnchair.search.algorithms.LawnchairSearchAlgorithm
import app.lawnchair.ui.theme.LawnchairTheme
import com.android.launcher3.allapps.BaseAllAppsAdapter
import com.android.launcher3.search.SearchCallback

/**
 * A search surface that drops down from the home screen search bar.
 *
 * The workspace search bar lives in a fixed-height pinned cell of the
 * workspace grid, so results cannot expand inside its own container. This
 * hosts them in an overlay added to the launcher's DragLayer instead,
 * anchored directly under the bar, which keeps the results visually attached
 * to it without opening the app drawer's search sheet.
 */
object HomeSearchOverlay {

    private const val TAG_ID = "lawnchair_home_search_overlay"

    fun show(launcher: LawnchairLauncher, anchor: android.view.View) {
        val dragLayer = launcher.dragLayer
        if (dragLayer.findViewWithTag<ComposeView>(TAG_ID) != null) return

        // DragLayer-relative coordinates: the anchor's window position is not
        // the DragLayer's, so measure both and subtract.
        val anchorPos = IntArray(2)
        val layerPos = IntArray(2)
        anchor.getLocationOnScreen(anchorPos)
        dragLayer.getLocationOnScreen(layerPos)
        val anchorTopPx = anchorPos[1] - layerPos[1]
        val anchorLeftPx = anchorPos[0] - layerPos[0]
        val anchorWidthPx = anchor.width
        val anchorHeightPx = anchor.height

        // The static pill is not an input, so hide it and put the live field
        // exactly where it was: to the user, the bar itself becomes editable.
        anchor.visibility = android.view.View.INVISIBLE
        anchorView = anchor

        val composeView = ComposeView(launcher).apply {
            tag = TAG_ID
            // DragLayer is an InsettableFrameLayout: it pushes window insets
            // onto children as margins unless they opt out. This overlay
            // positions itself against raw DragLayer coordinates, so it must.
            layoutParams = BaseDragLayer.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            ).apply { ignoreInsets = true }
            // The overlay lives in the DragLayer, outside any back dispatcher,
            // so take the back key directly.
            isFocusableInTouchMode = true
            setOnKeyListener { _, keyCode, event ->
                if (keyCode == android.view.KeyEvent.KEYCODE_BACK &&
                    event.action == android.view.KeyEvent.ACTION_UP
                ) {
                    dismiss(launcher)
                    true
                } else {
                    false
                }
            }
            setContent {
                LawnchairTheme {
                    HomeSearchContent(
                        anchorTopPx = anchorTopPx,
                        anchorLeftPx = anchorLeftPx,
                        anchorWidthPx = anchorWidthPx,
                        anchorHeightPx = anchorHeightPx,
                        onDismiss = { dismiss(launcher) },
                    )
                }
            }
        }
        dragLayer.addView(composeView)
        composeView.requestFocus()
    }

    private var anchorView: android.view.View? = null

    fun dismiss(launcher: LawnchairLauncher): Boolean {
        val dragLayer = launcher.dragLayer
        val view = dragLayer.findViewWithTag<ComposeView>(TAG_ID) ?: return false
        dragLayer.removeView(view)
        anchorView?.visibility = android.view.View.VISIBLE
        anchorView = null
        return true
    }

    fun isShowing(launcher: LawnchairLauncher): Boolean =
        launcher.dragLayer.findViewWithTag<ComposeView>(TAG_ID) != null
}

@Composable
private fun HomeSearchContent(
    anchorTopPx: Int,
    anchorLeftPx: Int,
    anchorWidthPx: Int,
    anchorHeightPx: Int,
    onDismiss: () -> Unit,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val density = LocalDensity.current
    val topDp = with(density) { anchorTopPx.toDp() }
    val fieldHeightDp = with(density) { anchorHeightPx.coerceAtLeast(1).toDp() }
    val leftDp = with(density) { anchorLeftPx.toDp() }
    val widthDp = with(density) { anchorWidthPx.toDp() }

    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf(emptyList<SearchAdapterItem>()) }
    val algorithm = remember { LawnchairSearchAlgorithm.create(context) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    LaunchedEffect(query) {
        if (query.isBlank()) {
            results = emptyList()
            return@LaunchedEffect
        }
        algorithm.doSearch(
            query,
            object : SearchCallback<BaseAllAppsAdapter.AdapterItem> {
                override fun onSearchResult(
                    query: String,
                    items: ArrayList<BaseAllAppsAdapter.AdapterItem>?,
                ) {
                    results = items.orEmpty().filterIsInstance<SearchAdapterItem>()
                }

                override fun clearSearchResult() {
                    results = emptyList()
                }
            },
        )
    }

    // Scrim: tapping anywhere outside the panel closes the overlay.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onDismiss),
    ) {
        Column(
            modifier = Modifier
                .padding(start = leftDp, top = topDp)
                .then(if (anchorWidthPx > 0) Modifier.width(widthDp) else Modifier.fillMaxWidth()),
        ) {
            SearchField(
                query = query,
                onQueryChange = { query = it },
                height = fieldHeightDp,
                modifier = Modifier.focusRequester(focusRequester),
            )
            if (results.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(28.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(vertical = 8.dp),
                ) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(results) { item ->
                            ResultRow(item = item, onLaunched = onDismiss)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    height: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        if (query.isEmpty()) {
            Text(
                text = androidx.compose.ui.res.stringResource(com.android.launcher3.R.string.all_apps_search_bar_hint),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
private fun ResultRow(item: SearchAdapterItem, onLaunched: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val target = item.searchTarget
    val action = target.searchAction

    // Section headers come through as ordinary targets; render them as labels.
    if (target.layoutType == com.android.app.search.LayoutType.TEXT_HEADER) {
        val title = action?.title?.toString().orEmpty()
        if (title.isNotBlank() && title != SPACE && title != SPACE_MINI) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 20.dp, top = 12.dp, bottom = 4.dp),
            )
        }
        return
    }
    if (action == null) return

    val iconDrawable = remember(action.id) {
        runCatching { action.icon?.loadDrawable(context) }.getOrNull()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val launched = runCatching {
                    when {
                        action.pendingIntent != null -> { action.pendingIntent.send(); true }
                        action.intent != null -> { context.startActivity(action.intent); true }
                        else -> false
                    }
                }.getOrDefault(false)
                if (launched) onLaunched()
            }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        iconDrawable?.let { drawable ->
            androidx.compose.foundation.Image(
                painter = DrawablePainter(drawable),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
            )
        }
        Column {
            Text(
                text = action.title.toString(),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 15.sp,
            )
            action.subtitle?.toString()?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 13.sp,
                )
            }
        }
    }
}

/** Minimal Drawable-backed painter, so platform icons can be drawn in Compose. */
private class DrawablePainter(private val drawable: Drawable) : Painter() {
    override val intrinsicSize: androidx.compose.ui.geometry.Size
        get() = androidx.compose.ui.geometry.Size(
            drawable.intrinsicWidth.coerceAtLeast(1).toFloat(),
            drawable.intrinsicHeight.coerceAtLeast(1).toFloat(),
        )

    override fun DrawScope.onDraw() {
        drawable.setBounds(0, 0, size.width.toInt(), size.height.toInt())
        drawable.draw(drawContext.canvas.nativeCanvas)
    }
}
