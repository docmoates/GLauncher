package app.lawnchair.google

import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.core.content.edit
import com.android.launcher3.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

/**
 * Thin client for the XMethod portal (portal.xmethod.org).
 *
 * The portal owns the Google OAuth client (id *and* secret) and stores the
 * Google refresh token encrypted server-side, so the launcher never needs its
 * own Google Cloud project and never holds a long-lived Google credential.
 * The launcher holds only the portal API key (`xm_…`) and short-lived Google
 * access tokens.
 *
 * Endpoints used (all under `<base>/api/v1/browser`):
 *  - `POST /login`                       → `{ token, user { email } }`
 *  - `GET  /oauth-config`                → `{ googleClientId }` (unauthenticated)
 *  - `POST /connections/google/exchange` → `{ accessToken, expiresIn, email }`
 *  - `POST /connections/google/refresh`  → `{ accessToken, expiresIn }`
 *  - `DELETE /connections/google`        → revokes + forgets the connection
 */
class XmethodPortal private constructor(private val context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val httpClient = OkHttpClient()

    private val baseUrl: String
        get() = context.getString(R.string.xmethod_portal_base_url).trimEnd('/')

    private val _portalEmail = MutableStateFlow(prefs.getString(KEY_PORTAL_EMAIL, null))

    /** E-mail of the signed-in portal account, or null when signed out. */
    val portalEmail: StateFlow<String?> = _portalEmail

    val token: String?
        get() = prefs.getString(KEY_PORTAL_TOKEN, null)

    val isSignedIn: Boolean
        get() = token != null

    /** True once a portal base URL is configured. Always true for stock builds. */
    val isConfigured: Boolean
        get() = baseUrl.isNotBlank()

    /**
     * Signs in to the portal with the user's XMethod credentials and stores the
     * returned API key.
     *
     * @return null on success, or a human-readable error message.
     */
    suspend fun signIn(email: String, password: String): String? = withContext(Dispatchers.IO) {
        val payload = deviceInfo()
            .put("email", email)
            .put("password", password)
        finishLogin("$baseUrl/api/v1/browser/login", payload, fallbackEmail = email)
    }

    /**
     * Signs in to the portal with a Google authorization code.
     *
     * This is the one-step path: the portal exchanges the code with its own
     * client secret, issues the portal API key *and* stores the Google refresh
     * token as the user's Google connection in the same round trip. That means
     * a single consent screen covers both portal sign-in and Google search —
     * no password, and no second "Connect Google" step.
     *
     * @return null on success, or a human-readable error message.
     */
    suspend fun loginWithGoogle(
        code: String,
        redirectUri: String,
        codeVerifier: String,
    ): String? = withContext(Dispatchers.IO) {
        val payload = deviceInfo()
            .put("code", code)
            .put("redirectUri", redirectUri)
            .put("codeVerifier", codeVerifier)
        finishLogin("$baseUrl/api/v1/browser/login/google", payload, fallbackEmail = null)
    }

    /** Posts a login payload and stores the resulting portal key. */
    private fun finishLogin(url: String, payload: JSONObject, fallbackEmail: String?): String? {
        val result = post(url, payload, authorized = false)
        val json = result.json
            ?: return result.error ?: context.getString(R.string.portal_sign_in_failed)

        val portalToken = json.optString("token").takeIf { it.isNotEmpty() }
            ?: return context.getString(R.string.portal_sign_in_failed)
        val accountEmail = json.optJSONObject("user")?.optString("email")?.takeIf { it.isNotEmpty() }
            ?: fallbackEmail

        prefs.edit {
            putString(KEY_PORTAL_TOKEN, portalToken)
            if (accountEmail != null) putString(KEY_PORTAL_EMAIL, accountEmail)
        }
        _portalEmail.value = accountEmail ?: context.getString(R.string.google_account_signed_in)
        return null
    }

    private fun deviceInfo(): JSONObject {
        val machineId = runCatching {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }.getOrNull() ?: "glauncher-unknown"
        return JSONObject()
            .put("machineId", machineId)
            .put("deviceName", "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}".trim())
            .put("os", "Android ${android.os.Build.VERSION.RELEASE}")
            .put("browserVersion", "GLauncher")
    }

    /** Forgets the portal session. Does not touch the Google connection. */
    fun signOut() {
        prefs.edit {
            remove(KEY_PORTAL_TOKEN)
            remove(KEY_PORTAL_EMAIL)
        }
        _portalEmail.value = null
    }

    /**
     * The Google OAuth client ID the portal wants clients to use. Public
     * identifier only; the secret never leaves the server.
     */
    suspend fun googleClientId(): String? = withContext(Dispatchers.IO) {
        runCatching {
            val request = Request.Builder().url("$baseUrl/api/v1/browser/oauth-config").build()
            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@use null
                JSONObject(response.body.string())
                    .optString("googleClientId")
                    .takeIf { it.isNotEmpty() }
            }
        }.onFailure { Log.e(TAG, "oauth-config failed", it) }.getOrNull()
    }

    /**
     * Hands the PKCE authorization code to the portal, which exchanges it with
     * Google using its own client secret and keeps the refresh token.
     */
    suspend fun exchangeGoogleCode(
        code: String,
        redirectUri: String,
        codeVerifier: String,
    ): GoogleToken? = withContext(Dispatchers.IO) {
        val payload = JSONObject()
            .put("code", code)
            .put("redirectUri", redirectUri)
            .put("codeVerifier", codeVerifier)
        val json = post("$baseUrl/api/v1/browser/connections/google/exchange", payload).json
            ?: return@withContext null
        val accessToken = json.optString("accessToken").takeIf { it.isNotEmpty() }
            ?: return@withContext null
        GoogleToken(
            accessToken = accessToken,
            expiresInSeconds = json.optLong("expiresIn", 3600),
            email = json.optString("email").takeIf { it.isNotEmpty() },
        )
    }

    /** Mints a fresh short-lived Google access token from the stored connection. */
    suspend fun refreshGoogleToken(): GoogleToken? = withContext(Dispatchers.IO) {
        val result = post("$baseUrl/api/v1/browser/connections/google/refresh", JSONObject())
        val json = result.json ?: run {
            // 401 reconnect_required / 404 not_connected both mean the user must
            // reconnect Google; the caller surfaces that by reporting no token.
            Log.w(TAG, "Google token refresh failed: ${result.error}")
            return@withContext null
        }
        val accessToken = json.optString("accessToken").takeIf { it.isNotEmpty() }
            ?: return@withContext null
        GoogleToken(accessToken, json.optLong("expiresIn", 3600), null)
    }

    /** Revokes the Google grant server-side and deletes the stored connection. */
    suspend fun disconnectGoogle(): Boolean = withContext(Dispatchers.IO) {
        val portalToken = token ?: return@withContext false
        runCatching {
            val request = Request.Builder()
                .url("$baseUrl/api/v1/browser/connections/google")
                .header("Authorization", "Bearer $portalToken")
                .delete()
                .build()
            httpClient.newCall(request).execute().use { it.isSuccessful }
        }.onFailure { Log.e(TAG, "Google disconnect failed", it) }.getOrDefault(false)
    }

    private fun post(url: String, payload: JSONObject, authorized: Boolean = true): PortalResult {
        if (authorized && token == null) return PortalResult(null, "Not signed in to the portal")
        return runCatching {
            val builder = Request.Builder()
                .url(url)
                .post(payload.toString().toRequestBody(JSON_MEDIA_TYPE))
            if (authorized) builder.header("Authorization", "Bearer $token")

            httpClient.newCall(builder.build()).execute().use { response ->
                val text = response.body.string()
                val json = runCatching { JSONObject(text) }.getOrNull()
                if (!response.isSuccessful) {
                    Log.w(TAG, "POST $url failed (${response.code}): ${text.take(200)}")
                    val message = json?.optString("message")?.takeIf { it.isNotEmpty() }
                        ?: json?.optString("error")?.takeIf { it.isNotEmpty() }
                    return@use PortalResult(null, message ?: "Request failed (${response.code})")
                }
                PortalResult(json, null)
            }
        }.onFailure { Log.e(TAG, "POST $url error", it) }
            .getOrElse { PortalResult(null, it.message) }
    }

    private data class PortalResult(val json: JSONObject?, val error: String?)

    data class GoogleToken(
        val accessToken: String,
        val expiresInSeconds: Long,
        val email: String?,
    )

    companion object {
        private const val TAG = "XmethodPortal"
        private const val PREFS_NAME = "xmethod_portal"
        private const val KEY_PORTAL_TOKEN = "portal_token"
        private const val KEY_PORTAL_EMAIL = "portal_email"
        private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

        @Volatile
        private var instance: XmethodPortal? = null

        fun getInstance(context: Context): XmethodPortal = instance ?: synchronized(this) {
            instance ?: XmethodPortal(context.applicationContext).also { instance = it }
        }
    }
}
