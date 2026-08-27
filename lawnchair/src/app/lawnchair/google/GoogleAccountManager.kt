package app.lawnchair.google

import android.content.Context
import android.content.Intent
import android.util.Base64
import android.util.Log
import androidx.core.content.edit
import androidx.core.net.toUri
import com.android.launcher3.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.ServerSocket
import java.net.URLDecoder
import java.security.MessageDigest
import java.security.SecureRandom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Manages the Google account connection behind the launcher's Google-powered
 * search providers (Gmail, Drive, Calendar, Contacts, Tasks, YouTube).
 *
 * Authorization is delegated entirely to the XMethod portal
 * (see [XmethodPortal]): the portal owns the Google OAuth client and holds the
 * refresh token encrypted server-side, so this launcher needs no Google Cloud
 * project of its own and never stores a long-lived Google credential. The only
 * things kept on device are the portal API key and a short-lived access token.
 *
 * The consent step uses the OAuth authorization-code flow with PKCE and a
 * loopback redirect (`http://127.0.0.1:<ephemeral-port>/callback`), matching
 * the desktop XMethod Browser. The portal's Google client is a Desktop-app
 * client, which accepts any loopback port without pre-registration.
 */
class GoogleAccountManager private constructor(private val context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val portal = XmethodPortal.getInstance(context)
    private val refreshMutex = Mutex()

    private val _signedInEmail = MutableStateFlow(prefs.getString(KEY_EMAIL, null))

    /** The e-mail address of the connected Google account, or null when not connected. */
    val signedInEmail: StateFlow<String?> = _signedInEmail

    /** The portal account the Google connection hangs off. */
    val portalEmail: StateFlow<String?> = portal.portalEmail

    /** True when the user has signed in to the XMethod portal. */
    val isPortalSignedIn: Boolean
        get() = portal.isSignedIn

    /** True when a Google connection is available for search to use. */
    val isSignedIn: Boolean
        get() = portal.isSignedIn && prefs.getBoolean(KEY_CONNECTED, false)

    /** True once a portal is configured to authorize against. */
    val isConfigured: Boolean
        get() = portal.isConfigured

    /** Signs in to the XMethod portal. Returns null on success, else an error message. */
    suspend fun signInToPortal(email: String, password: String): String? {
        val error = portal.signIn(email, password)
        if (error == null) {
            // The portal may already hold a Google connection made from another
            // XMethod client (the desktop browser). If so, adopt it silently so
            // the user isn't asked to consent a second time.
            probeExistingConnection()
        }
        return error
    }

    /**
     * Runs the Google consent flow and hands the resulting code to the portal.
     * Returns null on success, or a human-readable error message.
     *
     * Must be called off the main thread; it blocks on the loopback callback
     * until the user finishes (or abandons) consent in the browser.
     */
    suspend fun connectGoogle(context: Context): String? = withContext(Dispatchers.IO) {
        if (!portal.isSignedIn) return@withContext context.getString(R.string.portal_not_signed_in)
        runConsent(context) { code, redirectUri, verifier ->
            val token = portal.exchangeGoogleCode(code, redirectUri, verifier)
                ?: return@runConsent context.getString(R.string.google_account_sign_in_failed)
            store(token)
            null
        }
    }

    /**
     * One-step sign-in: a single Google consent screen both signs the user in
     * to the XMethod portal and connects Google for search. No password, and
     * no separate [connectGoogle] step afterwards.
     *
     * Returns null on success, or a human-readable error message.
     */
    suspend fun signInWithGoogle(context: Context): String? = withContext(Dispatchers.IO) {
        runConsent(context) { code, redirectUri, verifier ->
            val error = portal.loginWithGoogle(code, redirectUri, verifier)
            if (error != null) return@runConsent error
            // The portal stored the Google refresh token as part of that same
            // exchange, so a refresh here yields the access token search needs.
            if (!probeExistingConnection()) {
                return@runConsent context.getString(R.string.google_account_sign_in_failed)
            }
            null
        }
    }

    /**
     * Runs the Google consent flow with PKCE over a loopback redirect and
     * hands the resulting code to [onCode].
     *
     * Must be called off the main thread; it blocks on the loopback callback
     * until the user finishes (or abandons) consent in the browser.
     */
    private suspend fun runConsent(
        context: Context,
        onCode: suspend (code: String, redirectUri: String, verifier: String) -> String?,
    ): String? {
        val clientId = portal.googleClientId()
            ?: return context.getString(R.string.google_account_portal_unconfigured)

        val verifier = generateCodeVerifier()
        ServerSocket(0, 1, InetAddress.getByName("127.0.0.1")).use { server ->
            server.soTimeout = CONSENT_TIMEOUT_MS
            val redirectUri = "http://127.0.0.1:${server.localPort}/callback"

            val authUri = AUTH_ENDPOINT.toUri().buildUpon()
                .appendQueryParameter("client_id", clientId)
                .appendQueryParameter("redirect_uri", redirectUri)
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("scope", SCOPES.joinToString(" "))
                .appendQueryParameter("code_challenge", codeChallenge(verifier))
                .appendQueryParameter("code_challenge_method", "S256")
                .appendQueryParameter("access_type", "offline")
                .appendQueryParameter("prompt", "consent")
                .build()

            runCatching {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, authUri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                )
            }.onFailure {
                Log.e(TAG, "No browser available for Google consent", it)
                return context.getString(R.string.google_account_no_browser)
            }

            val callback = awaitCallback(server)
                ?: return context.getString(R.string.google_account_sign_in_failed)
            callback.error?.let {
                Log.w(TAG, "Google consent returned error: $it")
                return context.getString(R.string.google_account_sign_in_failed)
            }
            val code = callback.code
                ?: return context.getString(R.string.google_account_sign_in_failed)

            return onCode(code, redirectUri, verifier)
        }
    }

    /**
     * Returns a valid Google access token, minting a fresh one through the
     * portal when the cached one is stale. Null when not connected.
     */
    suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        refreshMutex.withLock {
            if (!portal.isSignedIn) return@withLock null
            val expiry = prefs.getLong(KEY_EXPIRY, 0)
            val current = prefs.getString(KEY_ACCESS_TOKEN, null)
            if (current != null && System.currentTimeMillis() < expiry - EXPIRY_MARGIN_MS) {
                return@withLock current
            }
            val token = portal.refreshGoogleToken()
            if (token == null) {
                // The portal dropped the connection (revoked or expired grant);
                // stop reporting connected so the UI prompts a reconnect.
                markDisconnected()
                return@withLock null
            }
            store(token)
            token.accessToken
        }
    }

    /** Disconnects Google server-side and forgets it locally. */
    suspend fun disconnectGoogle() = withContext(Dispatchers.IO) {
        portal.disconnectGoogle()
        markDisconnected()
    }

    /** Signs out of the portal, which also drops the local Google state. */
    suspend fun signOut() = withContext(Dispatchers.IO) {
        markDisconnected()
        portal.signOut()
    }

    /**
     * Asks the portal for a token without any consent UI. Succeeds when a
     * Google connection already exists for this portal account.
     */
    suspend fun probeExistingConnection(): Boolean = withContext(Dispatchers.IO) {
        val token = portal.refreshGoogleToken() ?: return@withContext false
        store(token)
        true
    }

    private fun store(token: XmethodPortal.GoogleToken) {
        prefs.edit {
            putString(KEY_ACCESS_TOKEN, token.accessToken)
            putLong(KEY_EXPIRY, System.currentTimeMillis() + token.expiresInSeconds * 1000)
            putBoolean(KEY_CONNECTED, true)
            if (token.email != null) putString(KEY_EMAIL, token.email)
        }
        _signedInEmail.value = token.email
            ?: prefs.getString(KEY_EMAIL, null)
            ?: portal.portalEmail.value
            ?: context.getString(R.string.google_account_signed_in)
    }

    private fun markDisconnected() {
        prefs.edit {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_EXPIRY)
            remove(KEY_EMAIL)
            putBoolean(KEY_CONNECTED, false)
        }
        _signedInEmail.value = null
    }

    /**
     * Serves the loopback callback until the browser hits `/callback` with a
     * code or an error. Ignores unrelated requests (favicon and the like).
     */
    private fun awaitCallback(server: ServerSocket): Callback? = runCatching {
        var result: Callback? = null
        while (result == null) {
            server.accept().use { socket ->
                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                val requestLine = reader.readLine()
                val target = requestLine?.split(' ')?.getOrNull(1)
                val params = parseQuery(target?.substringAfter('?', "").orEmpty())
                val code = params["code"]
                val error = params["error"]

                val message = when {
                    code != null -> context.getString(R.string.google_account_sign_in_success)
                    error != null -> context.getString(R.string.google_account_sign_in_failed)
                    else -> ""
                }
                socket.getOutputStream().apply {
                    write(httpResponse(message).toByteArray())
                    flush()
                }
                if (code != null || error != null) {
                    result = Callback(code, error)
                }
            }
        }
        result
    }.onFailure { Log.w(TAG, "Loopback callback failed or timed out", it) }.getOrNull()

    private fun parseQuery(query: String): Map<String, String> = query
        .split('&')
        .mapNotNull { part ->
            val name = part.substringBefore('=', "").takeIf { it.isNotEmpty() } ?: return@mapNotNull null
            val value = runCatching {
                URLDecoder.decode(part.substringAfter('=', ""), "UTF-8")
            }.getOrDefault("")
            name to value
        }
        .toMap()

    private fun httpResponse(message: String): String {
        val body = "<!doctype html><html><head><meta charset=\"utf-8\">" +
            "<title>GLauncher</title></head><body style=\"font-family:sans-serif;" +
            "text-align:center;padding:3rem\"><h2>$message</h2>" +
            "<p>You can close this tab and return to GLauncher.</p></body></html>"
        return buildString {
            append("HTTP/1.1 200 OK\r\n")
            append("Content-Type: text/html; charset=utf-8\r\n")
            append("Content-Length: ${body.toByteArray().size}\r\n")
            append("Connection: close\r\n\r\n")
            append(body)
        }
    }

    private data class Callback(val code: String?, val error: String?)

    private fun generateCodeVerifier(): String {
        val bytes = ByteArray(64)
        SecureRandom().nextBytes(bytes)
        return Base64.encodeToString(bytes, BASE64_FLAGS)
    }

    private fun codeChallenge(verifier: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(verifier.toByteArray())
        return Base64.encodeToString(digest, BASE64_FLAGS)
    }

    companion object {
        private const val TAG = "GoogleAccountManager"

        private const val PREFS_NAME = "google_account"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_EXPIRY = "token_expiry"
        private const val KEY_EMAIL = "email"
        private const val KEY_CONNECTED = "connected"

        private const val AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth"

        private const val EXPIRY_MARGIN_MS = 60_000L
        private const val CONSENT_TIMEOUT_MS = 5 * 60 * 1000
        private val BASE64_FLAGS = Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING

        /**
         * The scope set the portal's Google client is already configured for
         * (mirrors SCOPE_MAP in the XMethod Browser's `src/main/google-auth.js`).
         * Requesting the same set means one consent covers every XMethod client
         * and the launcher can adopt a connection made from the desktop app.
         */
        private val SCOPES = listOf(
            "openid",
            "email",
            "profile",
            "https://www.googleapis.com/auth/gmail.modify",
            "https://www.googleapis.com/auth/gmail.labels",
            "https://www.googleapis.com/auth/calendar",
            "https://www.googleapis.com/auth/drive",
            "https://www.googleapis.com/auth/contacts",
            "https://www.googleapis.com/auth/tasks",
            "https://www.googleapis.com/auth/youtube.readonly",
        )

        @Volatile
        private var instance: GoogleAccountManager? = null

        fun getInstance(context: Context): GoogleAccountManager = instance ?: synchronized(this) {
            instance ?: GoogleAccountManager(context.applicationContext).also { instance = it }
        }
    }
}
