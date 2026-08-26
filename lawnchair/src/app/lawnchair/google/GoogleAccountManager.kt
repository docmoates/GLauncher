package app.lawnchair.google

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.core.content.edit
import androidx.core.net.toUri
import com.android.launcher3.BuildConfig
import com.android.launcher3.R
import java.security.MessageDigest
import java.security.SecureRandom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

/**
 * Manages a single Google account connection for the launcher's Google-powered
 * search providers (Gmail, Drive, Calendar, Contacts, Tasks, YouTube).
 *
 * Uses the OAuth 2.0 authorization-code flow with PKCE for installed apps, so
 * no client secret is required. The OAuth client ID must be configured in
 * Google Cloud and pasted into `google_oauth_client_id` (see todo.md at the
 * repository root).
 */
class GoogleAccountManager private constructor(private val context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val httpClient = OkHttpClient()
    private val refreshMutex = Mutex()

    private val _signedInEmail = MutableStateFlow(prefs.getString(KEY_EMAIL, null))

    /** The e-mail address of the connected account, or null when signed out. */
    val signedInEmail: StateFlow<String?> = _signedInEmail

    val isSignedIn: Boolean
        get() = prefs.getString(KEY_REFRESH_TOKEN, null) != null

    private val clientId: String
        get() = context.getString(R.string.google_oauth_client_id)

    /** True once the OAuth client ID has been configured in Google Cloud. */
    val isConfigured: Boolean
        get() = clientId.isNotBlank()

    private val redirectUri: String
        get() = "${BuildConfig.APPLICATION_ID}:$REDIRECT_PATH"

    /**
     * Launches the browser-based Google sign-in flow. The redirect lands in
     * [GoogleAuthRedirectActivity], which calls [handleAuthRedirect].
     */
    fun beginSignIn(context: Context) {
        if (!isConfigured) return
        val verifier = generateCodeVerifier()
        prefs.edit { putString(KEY_PENDING_VERIFIER, verifier) }

        val authUri = AUTH_ENDPOINT.toUri().buildUpon()
            .appendQueryParameter("client_id", clientId)
            .appendQueryParameter("redirect_uri", redirectUri)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("scope", SCOPES.joinToString(" "))
            .appendQueryParameter("code_challenge", codeChallenge(verifier))
            .appendQueryParameter("code_challenge_method", "S256")
            .appendQueryParameter("prompt", "consent")
            .build()

        val intent = Intent(Intent.ACTION_VIEW, authUri)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * Completes the sign-in flow with the redirect URI Google sent back.
     * @return true when tokens were obtained and stored.
     */
    suspend fun handleAuthRedirect(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        val code = uri.getQueryParameter("code") ?: run {
            Log.w(TAG, "Auth redirect missing code: ${uri.getQueryParameter("error")}")
            return@withContext false
        }
        val verifier = prefs.getString(KEY_PENDING_VERIFIER, null) ?: return@withContext false

        val body = FormBody.Builder()
            .add("client_id", clientId)
            .add("code", code)
            .add("code_verifier", verifier)
            .add("grant_type", "authorization_code")
            .add("redirect_uri", redirectUri)
            .build()

        val json = postForm(TOKEN_ENDPOINT, body) ?: return@withContext false
        val accessToken = json.optString("access_token").takeIf { it.isNotEmpty() }
            ?: return@withContext false
        val refreshToken = json.optString("refresh_token").takeIf { it.isNotEmpty() }
        val expiresIn = json.optLong("expires_in", 3600)

        prefs.edit {
            putString(KEY_ACCESS_TOKEN, accessToken)
            if (refreshToken != null) putString(KEY_REFRESH_TOKEN, refreshToken)
            putLong(KEY_EXPIRY, System.currentTimeMillis() + expiresIn * 1000)
            remove(KEY_PENDING_VERIFIER)
        }

        fetchUserEmail(accessToken)?.let { email ->
            prefs.edit { putString(KEY_EMAIL, email) }
            _signedInEmail.value = email
        } ?: run { _signedInEmail.value = context.getString(R.string.google_account_signed_in) }
        true
    }

    /**
     * Returns a valid access token, refreshing it when expired.
     * Null when signed out or the refresh fails.
     */
    suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        refreshMutex.withLock {
            val expiry = prefs.getLong(KEY_EXPIRY, 0)
            val current = prefs.getString(KEY_ACCESS_TOKEN, null)
            if (current != null && System.currentTimeMillis() < expiry - EXPIRY_MARGIN_MS) {
                return@withLock current
            }
            val refreshToken = prefs.getString(KEY_REFRESH_TOKEN, null) ?: return@withLock null

            val body = FormBody.Builder()
                .add("client_id", clientId)
                .add("refresh_token", refreshToken)
                .add("grant_type", "refresh_token")
                .build()
            val json = postForm(TOKEN_ENDPOINT, body) ?: return@withLock null
            val accessToken = json.optString("access_token").takeIf { it.isNotEmpty() }
                ?: return@withLock null
            val expiresIn = json.optLong("expires_in", 3600)
            prefs.edit {
                putString(KEY_ACCESS_TOKEN, accessToken)
                putLong(KEY_EXPIRY, System.currentTimeMillis() + expiresIn * 1000)
            }
            accessToken
        }
    }

    /** Revokes the stored grant and forgets the account. */
    suspend fun signOut() = withContext(Dispatchers.IO) {
        val token = prefs.getString(KEY_REFRESH_TOKEN, null)
            ?: prefs.getString(KEY_ACCESS_TOKEN, null)
        if (token != null) {
            runCatching {
                val request = Request.Builder()
                    .url("$REVOKE_ENDPOINT?token=$token")
                    .post(FormBody.Builder().build())
                    .build()
                httpClient.newCall(request).execute().close()
            }
        }
        prefs.edit {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_EXPIRY)
            remove(KEY_EMAIL)
        }
        _signedInEmail.value = null
    }

    private fun fetchUserEmail(accessToken: String): String? = runCatching {
        val request = Request.Builder()
            .url(USERINFO_ENDPOINT)
            .header("Authorization", "Bearer $accessToken")
            .build()
        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            JSONObject(response.body?.string() ?: return null)
                .optString("email")
                .takeIf { it.isNotEmpty() }
        }
    }.getOrNull()

    private fun postForm(url: String, body: FormBody): JSONObject? = runCatching {
        val request = Request.Builder().url(url).post(body).build()
        httpClient.newCall(request).execute().use { response ->
            val text = response.body?.string()
            if (!response.isSuccessful) {
                Log.w(TAG, "Token request failed (${response.code}): $text")
                return null
            }
            JSONObject(text ?: return null)
        }
    }.onFailure { Log.e(TAG, "Token request error", it) }.getOrNull()

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
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_EXPIRY = "token_expiry"
        private const val KEY_EMAIL = "email"
        private const val KEY_PENDING_VERIFIER = "pending_code_verifier"

        private const val AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth"
        private const val TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token"
        private const val REVOKE_ENDPOINT = "https://oauth2.googleapis.com/revoke"
        private const val USERINFO_ENDPOINT = "https://openidconnect.googleapis.com/v1/userinfo"

        const val REDIRECT_PATH = "/oauth2redirect"

        private const val EXPIRY_MARGIN_MS = 60_000L
        private val BASE64_FLAGS =
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING

        private val SCOPES = listOf(
            "openid",
            "email",
            "https://www.googleapis.com/auth/gmail.readonly",
            "https://www.googleapis.com/auth/drive.metadata.readonly",
            "https://www.googleapis.com/auth/calendar.readonly",
            "https://www.googleapis.com/auth/contacts.readonly",
            "https://www.googleapis.com/auth/tasks.readonly",
            "https://www.googleapis.com/auth/youtube.readonly",
        )

        @Volatile
        private var instance: GoogleAccountManager? = null

        fun getInstance(context: Context): GoogleAccountManager = instance ?: synchronized(this) {
            instance ?: GoogleAccountManager(context.applicationContext).also { instance = it }
        }
    }
}
