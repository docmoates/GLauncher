package com.pocketforge.app

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlin.math.pow

/*
 * The all-apps swipe, ported from Launcher3 / Lawnchair
 * (AllAppsTransitionController + AllAppsSwipeController).
 *
 * The whole interaction is driven by a single progress value that tracks the
 * finger, exactly like AllAppsTransitionController.mProgress:
 *   progress == 1f  ->  drawer pulled all the way down (closed)
 *   progress == 0f  ->  drawer pulled all the way up (open)
 * Everything else (scrim, content fade, commit decision) is derived from it,
 * which is what makes the gesture feel continuous instead of a threshold that
 * suddenly fires an animation.
 */

/** AllAppsSwipeController.ALL_APPS_STATE_TRANSITION_MANUAL */
private const val STATE_TRANSITION_MANUAL = 0.4f

/** AllAppsTransitionController.SWIPE_DRAG_COMMIT_THRESHOLD = 1 - manual transition. */
private const val SWIPE_DRAG_COMMIT_THRESHOLD = 1f - STATE_TRANSITION_MANUAL

/** AllAppsSwipeController.SCRIM_FADE_START_MANUAL */
private const val SCRIM_FADE_START_MANUAL = 0.117f

/** AllAppsSwipeController content fade clamping thresholds. */
private const val CONTENT_FADE_MAX_CLAMP = 0.8f
private const val CONTENT_FADE_MIN_CLAMP = 0.5f

/** AllAppsTransitionController.INTERP_COEFF, i.e. Interpolators.DECELERATE_1_7. */
private const val INTERP_COEFF = 1.7f

/** Past this fling speed the swipe direction wins regardless of distance. */
private const val FLING_VELOCITY_PX_PER_SEC = 1000f

private const val SETTLE_DURATION_MS = 320

/** DecelerateInterpolator(1.7f): 1 - (1 - t)^(2 * 1.7). */
private val Decelerate17 = Easing { t -> 1f - (1f - t).pow(2f * INTERP_COEFF) }

@Stable
class AllAppsSwipeState {
    /** 1 = closed, 0 = open. Mirrors AllAppsTransitionController.mProgress. */
    val progress = Animatable(1f)

    /** Distance the drawer travels, i.e. Launcher3's shiftRange. */
    var shiftRangePx by mutableFloatStateOf(1f)

    /** True once the drawer is more than half way up; drives back handling and bar icons. */
    val isOpen: Boolean get() = progress.value < 0.5f

    /** Fully settled closed, so the drawer doesn't need to be drawn at all. */
    val isFullyClosed: Boolean get() = progress.value >= 1f

    /** Scrim over the wallpaper, ramping in from SCRIM_FADE_START_MANUAL. */
    val scrimAlpha: Float
        get() {
            val start = 1f - SCRIM_FADE_START_MANUAL
            return ((start - progress.value) / start).coerceIn(0f, 1f)
        }

    /** App icons/search fade in late, between the two content clamp thresholds. */
    val contentAlpha: Float
        get() = ((CONTENT_FADE_MAX_CLAMP - progress.value) /
            (CONTENT_FADE_MAX_CLAMP - CONTENT_FADE_MIN_CLAMP)).coerceIn(0f, 1f)

    /** Moves the drawer with the finger. [dy] is a raw vertical drag delta in px. */
    suspend fun dragBy(dy: Float) {
        if (shiftRangePx <= 0f) return
        progress.snapTo((progress.value + dy / shiftRangePx).coerceIn(0f, 1f))
    }

    /**
     * Releases the drag: a fast enough fling wins outright, otherwise commit
     * once the drag passed Launcher3's manual-transition threshold.
     */
    suspend fun settle(velocityPxPerSec: Float) {
        val target = when {
            velocityPxPerSec < -FLING_VELOCITY_PX_PER_SEC -> 0f
            velocityPxPerSec > FLING_VELOCITY_PX_PER_SEC -> 1f
            progress.value < SWIPE_DRAG_COMMIT_THRESHOLD -> 0f
            else -> 1f
        }
        animateTo(target)
    }

    suspend fun open() = animateTo(0f)

    suspend fun close() = animateTo(1f)

    private suspend fun animateTo(target: Float) {
        progress.animateTo(target, tween(SETTLE_DURATION_MS, easing = Decelerate17))
    }
}

@Composable
fun rememberAllAppsSwipeState(): AllAppsSwipeState = remember { AllAppsSwipeState() }

/**
 * Lets a downward drag inside the open drawer close it, but only once its list
 * is already scrolled to the top -- otherwise the grid scrolls normally. This
 * is the nested-scroll equivalent of how Launcher3 hands the gesture back and
 * forth between the all-apps list and the state transition.
 */
@Composable
fun rememberDrawerNestedScroll(
    state: AllAppsSwipeState,
    gridState: LazyGridState,
    onSettle: (Float) -> Unit,
    onDrag: (Float) -> Unit,
): NestedScrollConnection = remember(state, gridState) {
    object : NestedScrollConnection {
        private fun atTop() =
            gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0

        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            // Pulling down with the list already at the top, or continuing to
            // pull down a partly-open drawer: drive the transition instead.
            val dy = available.y
            if (dy > 0f && (state.progress.value > 0f || atTop())) {
                onDrag(dy)
                return Offset(0f, dy)
            }
            // Pushing a partly-open drawer back up finishes opening it first.
            if (dy < 0f && state.progress.value > 0f) {
                onDrag(dy)
                return Offset(0f, dy)
            }
            return Offset.Zero
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            if (state.progress.value > 0f) {
                onSettle(available.y)
                return available
            }
            return Velocity.Zero
        }
    }
}

/** Vertical orientation constant, kept here so callers don't import gestures directly. */
val AllAppsDragOrientation = Orientation.Vertical
