package app.lawnchair.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.content.FileProvider
import com.android.launcher3.BuildConfig
import java.io.File
import java.security.MessageDigest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Checks updates.xmethod.org for a newer build than the one currently
 * installed, downloads it, verifies its SHA-256, and hands it to the system
 * installer. This is a from-scratch, self-hosted equivalent of Lawnchair's
 * own NightlyBuildsRepository (which points at GitHub Releases) - same
 * FileProvider authority and "nightly_updates" cache dir the manifest
 * already declares, same install-permission flow, but backed by our own
 * static JSON endpoint instead of the GitHub API.
 */
@Serializable
data class ReleaseInfo(
    val versionCode: Long,
    val versionName: String,
    val releaseNotes: String = "",
    val downloadUrl: String,
    val sha256: String = "",
    val fileSize: Long = 0,
)

sealed class UpdateState {
    data object Idle : UpdateState()
    data object Checking : UpdateState()
    data object UpToDate : UpdateState()
    data class Available(val release: ReleaseInfo) : UpdateState()
    data class Downloading(val progress: Float) : UpdateState()
    data class Downloaded(val file: File, val release: ReleaseInfo) : UpdateState()
    data class Failed(val message: String) : UpdateState()
}

private const val UPDATE_MANIFEST_URL = "https://updates.xmethod.org/latest.json"
private const val TAG = "UpdateChecker"

class UpdateChecker(private val appContext: Context) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val http = OkHttpClient.Builder().build()
    private val json = Json { ignoreUnknownKeys = true }

    val state: StateFlow<UpdateState>
        field = MutableStateFlow<UpdateState>(UpdateState.Idle)

    fun checkForUpdate() {
        scope.launch {
            state.update { UpdateState.Checking }
            try {
                val req = Request.Builder().url(UPDATE_MANIFEST_URL).get().build()
                http.newCall(req).execute().use { resp ->
                    if (!resp.isSuccessful) {
                        state.update { UpdateState.Failed("Server returned ${resp.code}") }
                        return@launch
                    }
                    val body = resp.body?.string().orEmpty()
                    val release = json.decodeFromString(ReleaseInfo.serializer(), body)
                    state.update {
                        if (release.versionCode > BuildConfig.VERSION_CODE) {
                            UpdateState.Available(release)
                        } else {
                            UpdateState.UpToDate
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Update check failed", e)
                state.update { UpdateState.Failed(e.message ?: "Network error") }
            }
        }
    }

    fun downloadUpdate() {
        val current = state.value
        if (current !is UpdateState.Available) return
        val release = current.release

        scope.launch {
            state.update { UpdateState.Downloading(0f) }
            try {
                val dir = File(appContext.cacheDir, "updates").apply { mkdirs() }
                val dest = File(dir, "GLauncher-update.apk").apply { delete() }

                val req = Request.Builder().url(release.downloadUrl).get().build()
                http.newCall(req).execute().use { resp ->
                    if (!resp.isSuccessful) {
                        state.update { UpdateState.Failed("Download failed (${resp.code})") }
                        return@launch
                    }
                    val respBody = resp.body ?: run {
                        state.update { UpdateState.Failed("Empty response") }
                        return@launch
                    }
                    val total = respBody.contentLength().takeIf { it > 0 }
                    val digest = MessageDigest.getInstance("SHA-256")
                    respBody.byteStream().use { input ->
                        dest.outputStream().use { output ->
                            val buf = ByteArray(64 * 1024)
                            var done = 0L
                            var read: Int
                            while (input.read(buf).also { read = it } != -1) {
                                output.write(buf, 0, read)
                                digest.update(buf, 0, read)
                                done += read
                                if (total != null) {
                                    state.update { UpdateState.Downloading((done.toFloat() / total).coerceIn(0f, 1f)) }
                                }
                            }
                        }
                    }
                    if (release.sha256.isNotBlank()) {
                        val computed = digest.digest().joinToString("") { "%02x".format(it) }
                        if (!computed.equals(release.sha256, ignoreCase = true)) {
                            dest.delete()
                            state.update { UpdateState.Failed("Checksum mismatch - download corrupted") }
                            return@launch
                        }
                    }
                    state.update { UpdateState.Downloaded(dest, release) }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Download failed", e)
                state.update { UpdateState.Failed(e.message ?: "Download error") }
            }
        }
    }

    fun installUpdate(file: File) {
        if (!appContext.packageManager.canRequestPackageInstalls()) {
            val intent = Intent(
                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                Uri.parse("package:${appContext.packageName}"),
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            appContext.startActivity(intent)
            return
        }
        val uri = FileProvider.getUriForFile(
            appContext, "${BuildConfig.APPLICATION_ID}.fileprovider", file,
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        appContext.startActivity(intent)
    }
}
