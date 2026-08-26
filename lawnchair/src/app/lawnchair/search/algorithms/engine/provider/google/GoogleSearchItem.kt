package app.lawnchair.search.algorithms.engine.provider.google

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.android.launcher3.R

/**
 * The kind of Google service a search result came from. Each kind renders as
 * its own section in the search results, with its own icon and toggle.
 */
enum class GoogleItemKind(
    @StringRes val headerRes: Int,
    @DrawableRes val iconRes: Int,
) {
    GMAIL(R.string.search_pref_result_gmail_title, R.drawable.ic_google_gmail),
    DOCS(R.string.search_pref_result_google_docs_title, R.drawable.ic_google_docs),
    SHEETS(R.string.search_pref_result_google_sheets_title, R.drawable.ic_google_sheets),
    SLIDES(R.string.search_pref_result_google_slides_title, R.drawable.ic_google_slides),
    CALENDAR(R.string.search_pref_result_google_calendar_title, R.drawable.ic_google_calendar),
    CONTACTS(R.string.search_pref_result_google_contacts_title, R.drawable.ic_google_contacts),
    TASKS(R.string.search_pref_result_google_tasks_title, R.drawable.ic_google_tasks),
    YOUTUBE(R.string.search_pref_result_youtube_title, R.drawable.ic_youtube),
}

/**
 * A single result from a Google API search. Tapping it opens [url].
 */
data class GoogleItem(
    val kind: GoogleItemKind,
    val id: String,
    val title: String,
    val subtitle: String?,
    val url: String,
)
