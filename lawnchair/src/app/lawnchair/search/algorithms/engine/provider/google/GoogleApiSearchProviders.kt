package app.lawnchair.search.algorithms.engine.provider.google

import android.content.Context
import android.net.Uri
import android.util.Log
import app.lawnchair.google.GoogleAccountManager
import app.lawnchair.preferences.PreferenceManager
import app.lawnchair.search.algorithms.engine.SearchProvider
import app.lawnchair.search.algorithms.engine.SearchResult
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

/**
 * Shared plumbing for search providers backed by Google REST APIs.
 * All requests are authorized with the account connected in
 * Drawer search settings (see [GoogleAccountManager]).
 */
abstract class GoogleApiSearchProvider : SearchProvider {

    /** Minimum query length before hitting the network, to conserve API quota. */
    private val minQueryLength = 3

    protected abstract fun isEnabled(prefs: PreferenceManager): Boolean

    protected abstract suspend fun fetch(
        context: Context,
        query: String,
        accessToken: String,
    ): List<GoogleItem>

    override fun search(context: Context, query: String): Flow<List<SearchResult>> = flow {
        val prefs = PreferenceManager.getInstance(context)
        val accountManager = GoogleAccountManager.getInstance(context)
        if (query.length < minQueryLength ||
            !isEnabled(prefs) ||
            !accountManager.isSignedIn
        ) {
            emit(emptyList())
            return@flow
        }
        val token = accountManager.getAccessToken()
        if (token == null) {
            emit(emptyList())
            return@flow
        }
        val items = try {
            // Cap per kind, so one section can't starve another (e.g. Docs vs. Sheets).
            fetch(context, query, token)
                .groupBy { it.kind }
                .values
                .flatMap { it.take(MAX_RESULTS) }
        } catch (e: Exception) {
            Log.e(TAG, "Search failed for $id", e)
            emptyList()
        }
        emit(items.map { SearchResult.GoogleItem(it) })
    }.flowOn(Dispatchers.IO)

    protected fun getJson(url: String, accessToken: String): JSONObject? = runCatching {
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $accessToken")
            .build()
        httpClient.newCall(request).execute().use { response ->
            val text = response.body?.string()
            if (!response.isSuccessful) {
                Log.w(TAG, "$id request failed (${response.code}): ${text?.take(200)}")
                return null
            }
            JSONObject(text ?: return null)
        }
    }.onFailure { Log.e(TAG, "$id request error", it) }.getOrNull()

    companion object {
        private const val TAG = "GoogleApiSearch"
        const val MAX_RESULTS = 5
        private val httpClient = OkHttpClient()

        val allProviders: List<GoogleApiSearchProvider> = listOf(
            GmailSearchProvider,
            GoogleDriveSearchProvider,
            GoogleCalendarSearchProvider,
            GoogleContactsSearchProvider,
            GoogleTasksSearchProvider,
            YouTubeSearchProvider,
        )
    }
}

/** Searches Gmail messages via the Gmail API. */
object GmailSearchProvider : GoogleApiSearchProvider() {
    override val id = "google_gmail"

    override fun isEnabled(prefs: PreferenceManager) = prefs.searchResultGmail.get()

    override suspend fun fetch(context: Context, query: String, accessToken: String): List<GoogleItem> {
        val listUrl = "https://gmail.googleapis.com/gmail/v1/users/me/messages" +
            "?maxResults=$MAX_RESULTS&q=${Uri.encode(query)}"
        val messages = getJson(listUrl, accessToken)?.optJSONArray("messages") ?: return emptyList()

        return (0 until messages.length()).mapNotNull { i ->
            val messageId = messages.optJSONObject(i)?.optString("id")?.takeIf { it.isNotEmpty() }
                ?: return@mapNotNull null
            val detailUrl = "https://gmail.googleapis.com/gmail/v1/users/me/messages/$messageId" +
                "?format=metadata&metadataHeaders=Subject&metadataHeaders=From"
            val detail = getJson(detailUrl, accessToken) ?: return@mapNotNull null

            var subject: String? = null
            var from: String? = null
            val headers = detail.optJSONObject("payload")?.optJSONArray("headers")
            if (headers != null) {
                for (j in 0 until headers.length()) {
                    val header = headers.optJSONObject(j) ?: continue
                    when (header.optString("name").lowercase()) {
                        "subject" -> subject = header.optString("value")
                        "from" -> from = header.optString("value")
                    }
                }
            }
            GoogleItem(
                kind = GoogleItemKind.GMAIL,
                id = messageId,
                title = subject?.takeIf { it.isNotBlank() } ?: detail.optString("snippet"),
                subtitle = from,
                url = "https://mail.google.com/mail/u/0/#all/$messageId",
            )
        }
    }
}

/**
 * Searches Google Docs, Sheets, and Slides by file name via the Drive API.
 * Each document type has its own preference toggle and results section.
 */
object GoogleDriveSearchProvider : GoogleApiSearchProvider() {
    override val id = "google_drive"

    private const val MIME_DOCS = "application/vnd.google-apps.document"
    private const val MIME_SHEETS = "application/vnd.google-apps.spreadsheet"
    private const val MIME_SLIDES = "application/vnd.google-apps.presentation"

    override fun isEnabled(prefs: PreferenceManager) = prefs.searchResultGoogleDocs.get() ||
        prefs.searchResultGoogleSheets.get() ||
        prefs.searchResultGoogleSlides.get()

    override suspend fun fetch(context: Context, query: String, accessToken: String): List<GoogleItem> {
        val prefs = PreferenceManager.getInstance(context)
        val mimeTypes = buildList {
            if (prefs.searchResultGoogleDocs.get()) add(MIME_DOCS)
            if (prefs.searchResultGoogleSheets.get()) add(MIME_SHEETS)
            if (prefs.searchResultGoogleSlides.get()) add(MIME_SLIDES)
        }
        if (mimeTypes.isEmpty()) return emptyList()

        val escapedQuery = query.replace("\\", "\\\\").replace("'", "\\'")
        val mimeFilter = mimeTypes.joinToString(" or ") { "mimeType='$it'" }
        val driveQuery = "name contains '$escapedQuery' and trashed=false and ($mimeFilter)"
        val url = "https://www.googleapis.com/drive/v3/files" +
            "?pageSize=${MAX_RESULTS * mimeTypes.size}" +
            "&fields=${Uri.encode("files(id,name,mimeType,webViewLink)")}" +
            "&q=${Uri.encode(driveQuery)}"
        val files = getJson(url, accessToken)?.optJSONArray("files") ?: return emptyList()

        return (0 until files.length()).mapNotNull { i ->
            val file = files.optJSONObject(i) ?: return@mapNotNull null
            val fileId = file.optString("id").takeIf { it.isNotEmpty() } ?: return@mapNotNull null
            val kind = when (file.optString("mimeType")) {
                MIME_DOCS -> GoogleItemKind.DOCS
                MIME_SHEETS -> GoogleItemKind.SHEETS
                MIME_SLIDES -> GoogleItemKind.SLIDES
                else -> return@mapNotNull null
            }
            GoogleItem(
                kind = kind,
                id = fileId,
                title = file.optString("name"),
                subtitle = null,
                url = file.optString("webViewLink")
                    .takeIf { it.isNotEmpty() }
                    ?: "https://drive.google.com/open?id=$fileId",
            )
        }
    }
}

/** Searches upcoming and recent events on the primary Google Calendar. */
object GoogleCalendarSearchProvider : GoogleApiSearchProvider() {
    override val id = "google_calendar"

    override fun isEnabled(prefs: PreferenceManager) = prefs.searchResultGoogleCalendar.get()

    override suspend fun fetch(context: Context, query: String, accessToken: String): List<GoogleItem> {
        val timeMin = Instant.now().minus(30, ChronoUnit.DAYS).toString()
        val url = "https://www.googleapis.com/calendar/v3/calendars/primary/events" +
            "?maxResults=$MAX_RESULTS&singleEvents=true&orderBy=startTime" +
            "&timeMin=${Uri.encode(timeMin)}&q=${Uri.encode(query)}"
        val events = getJson(url, accessToken)?.optJSONArray("items") ?: return emptyList()

        return (0 until events.length()).mapNotNull { i ->
            val event = events.optJSONObject(i) ?: return@mapNotNull null
            val eventId = event.optString("id").takeIf { it.isNotEmpty() } ?: return@mapNotNull null
            val start = event.optJSONObject("start")?.let {
                it.optString("dateTime").takeIf { s -> s.isNotEmpty() } ?: it.optString("date")
            }
            GoogleItem(
                kind = GoogleItemKind.CALENDAR,
                id = eventId,
                title = event.optString("summary")
                    .takeIf { it.isNotEmpty() } ?: eventId,
                subtitle = start?.replace("T", " ")?.substringBefore("+")?.substringBefore("Z"),
                url = event.optString("htmlLink")
                    .takeIf { it.isNotEmpty() }
                    ?: "https://calendar.google.com/",
            )
        }
    }
}

/** Searches Google Contacts via the People API. */
object GoogleContactsSearchProvider : GoogleApiSearchProvider() {
    override val id = "google_contacts"

    override fun isEnabled(prefs: PreferenceManager) = prefs.searchResultGoogleContacts.get()

    override suspend fun fetch(context: Context, query: String, accessToken: String): List<GoogleItem> {
        val url = "https://people.googleapis.com/v1/people:searchContacts" +
            "?pageSize=$MAX_RESULTS&readMask=names,emailAddresses,phoneNumbers" +
            "&query=${Uri.encode(query)}"
        val results = getJson(url, accessToken)?.optJSONArray("results") ?: return emptyList()

        return (0 until results.length()).mapNotNull { i ->
            val person = results.optJSONObject(i)?.optJSONObject("person") ?: return@mapNotNull null
            val resourceName = person.optString("resourceName")
                .takeIf { it.isNotEmpty() } ?: return@mapNotNull null
            val name = person.optJSONArray("names")?.optJSONObject(0)
                ?.optString("displayName")?.takeIf { it.isNotEmpty() }
            val email = person.optJSONArray("emailAddresses")?.optJSONObject(0)?.optString("value")
            val phone = person.optJSONArray("phoneNumbers")?.optJSONObject(0)?.optString("value")
            GoogleItem(
                kind = GoogleItemKind.CONTACTS,
                id = resourceName,
                title = name ?: email ?: phone ?: return@mapNotNull null,
                subtitle = email ?: phone,
                url = "https://contacts.google.com/person/${resourceName.removePrefix("people/")}",
            )
        }
    }
}

/** Searches open Google Tasks across the user's task lists. */
object GoogleTasksSearchProvider : GoogleApiSearchProvider() {
    override val id = "google_tasks"

    private const val MAX_TASK_LISTS = 5

    override fun isEnabled(prefs: PreferenceManager) = prefs.searchResultGoogleTasks.get()

    override suspend fun fetch(context: Context, query: String, accessToken: String): List<GoogleItem> {
        val listsUrl = "https://tasks.googleapis.com/tasks/v1/users/@me/lists?maxResults=$MAX_TASK_LISTS"
        val taskLists = getJson(listsUrl, accessToken)?.optJSONArray("items") ?: return emptyList()

        val matches = mutableListOf<GoogleItem>()
        for (i in 0 until taskLists.length()) {
            if (matches.size >= MAX_RESULTS) break
            val taskList = taskLists.optJSONObject(i) ?: continue
            val listId = taskList.optString("id").takeIf { it.isNotEmpty() } ?: continue
            val listTitle = taskList.optString("title")

            val tasksUrl = "https://tasks.googleapis.com/tasks/v1/lists/$listId/tasks" +
                "?showCompleted=false&maxResults=100"
            val tasks = getJson(tasksUrl, accessToken)?.optJSONArray("items") ?: continue
            for (j in 0 until tasks.length()) {
                if (matches.size >= MAX_RESULTS) break
                val task = tasks.optJSONObject(j) ?: continue
                val title = task.optString("title")
                if (!title.contains(query, ignoreCase = true)) continue
                matches += GoogleItem(
                    kind = GoogleItemKind.TASKS,
                    id = task.optString("id"),
                    title = title,
                    subtitle = task.optString("due")
                        .takeIf { it.isNotEmpty() }
                        ?.substringBefore("T")
                        ?: listTitle,
                    url = "https://tasks.google.com/",
                )
            }
        }
        return matches
    }
}

/** Searches YouTube videos via the YouTube Data API. */
object YouTubeSearchProvider : GoogleApiSearchProvider() {
    override val id = "google_youtube"

    override fun isEnabled(prefs: PreferenceManager) = prefs.searchResultYoutube.get()

    override suspend fun fetch(context: Context, query: String, accessToken: String): List<GoogleItem> {
        val url = "https://www.googleapis.com/youtube/v3/search" +
            "?part=snippet&type=video&maxResults=$MAX_RESULTS&q=${Uri.encode(query)}"
        val items = getJson(url, accessToken)?.optJSONArray("items") ?: return emptyList()

        return (0 until items.length()).mapNotNull { i ->
            val item = items.optJSONObject(i) ?: return@mapNotNull null
            val videoId = item.optJSONObject("id")?.optString("videoId")
                ?.takeIf { it.isNotEmpty() } ?: return@mapNotNull null
            val snippet = item.optJSONObject("snippet") ?: return@mapNotNull null
            GoogleItem(
                kind = GoogleItemKind.YOUTUBE,
                id = videoId,
                title = snippet.optString("title"),
                subtitle = snippet.optString("channelTitle"),
                url = "https://www.youtube.com/watch?v=$videoId",
            )
        }
    }
}
