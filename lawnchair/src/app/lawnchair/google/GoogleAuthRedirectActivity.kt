package app.lawnchair.google

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.android.launcher3.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Receives the OAuth redirect from the browser after the user signs in with
 * Google, exchanges the authorization code for tokens, and finishes.
 */
class GoogleAuthRedirectActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent?.data
        if (uri == null) {
            finish()
            return
        }
        val appContext = applicationContext
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            val success = GoogleAccountManager.getInstance(appContext).handleAuthRedirect(uri)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    appContext,
                    if (success) R.string.google_account_sign_in_success else R.string.google_account_sign_in_failed,
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
        finish()
    }
}
