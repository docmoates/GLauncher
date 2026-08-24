package com.pocketforge.app

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Minimum upward drag (px) before a swipe counts as a swap instead of a stray touch. */
private const val SWAP_DRAG_THRESHOLD_PX = 120f

/**
 * Renders one [WidgetStack]. When the stack holds more than one widget, an upward swipe
 * advances to the next widget underneath, cycling back to the top after the last one.
 */
@Composable
fun WidgetStackView(stack: WidgetStack, modifier: Modifier = Modifier) {
    var currentIndex by remember(stack.id) { mutableIntStateOf(0) }
    var dragAccumulatorPx by remember(stack.id) { mutableFloatStateOf(0f) }
    val canSwap = stack.widgets.size > 1

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .let {
                    if (canSwap) {
                        it.draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState { delta ->
                                dragAccumulatorPx += delta
                            },
                            onDragStopped = {
                                if (dragAccumulatorPx <= -SWAP_DRAG_THRESHOLD_PX) {
                                    currentIndex = (currentIndex + 1) % stack.widgets.size
                                }
                                dragAccumulatorPx = 0f
                            },
                        )
                    } else it
                },
        ) {
            AnimatedContent(
                targetState = currentIndex,
                transitionSpec = {
                    (slideInVertically(animationSpec = tween(220)) { height -> height } togetherWith
                        slideOutVertically(animationSpec = tween(220)) { height -> -height })
                },
                label = "widget-swap",
            ) { index ->
                WidgetCard(widget = stack.widgets[index])
            }
        }

        if (canSwap) {
            StackIndicator(
                count = stack.widgets.size,
                selectedIndex = currentIndex,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 6.dp),
            )
        }
    }
}

@Composable
private fun WidgetCard(widget: WidgetData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(widget.size.heightDp.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(widget.color)
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                text = widget.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
            Text(
                text = widget.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
            )
        }
    }
}

@Composable
private fun StackIndicator(count: Int, selectedIndex: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
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
