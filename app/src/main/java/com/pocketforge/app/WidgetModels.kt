package com.pocketforge.app

import androidx.compose.ui.graphics.Color

/**
 * Widgets can only be stacked with other widgets that share the same [WidgetSize] —
 * that's what makes a swipe-to-swap between them visually seamless.
 */
enum class WidgetSize(val heightDp: Int) {
    SMALL(110),
    MEDIUM(170),
    LARGE(230),
}

data class WidgetData(
    val id: String,
    val title: String,
    val subtitle: String,
    val size: WidgetSize,
    val color: Color,
)

/** A single home-screen slot: one or more same-size widgets the user can swipe through. */
data class WidgetStack(
    val id: String,
    val size: WidgetSize,
    val widgets: List<WidgetData>,
)

fun sampleWidgetStacks(): List<WidgetStack> = listOf(
    WidgetStack(
        id = "stack-small",
        size = WidgetSize.SMALL,
        widgets = listOf(
            WidgetData("weather", "Weather", "72° · Sunny", WidgetSize.SMALL, Color(0xFF4C7BF3)),
            WidgetData("steps", "Steps", "6,412 today", WidgetSize.SMALL, Color(0xFF3AA86D)),
            WidgetData("battery", "Battery", "84% · Charging", WidgetSize.SMALL, Color(0xFFE0A62B)),
        ),
    ),
    WidgetStack(
        id = "stack-medium",
        size = WidgetSize.MEDIUM,
        widgets = listOf(
            WidgetData("calendar", "Calendar", "Team sync at 2:00 PM", WidgetSize.MEDIUM, Color(0xFF9C5CD6)),
            WidgetData("music", "Now Playing", "Lo-fi Focus Mix", WidgetSize.MEDIUM, Color(0xFFD6598C)),
        ),
    ),
    WidgetStack(
        id = "stack-large",
        size = WidgetSize.LARGE,
        widgets = listOf(
            WidgetData("notes", "Notes", "3 pinned notes", WidgetSize.LARGE, Color(0xFF37A0A8)),
        ),
    ),
)
