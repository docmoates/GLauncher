# GLauncher TODO

## Configure secrets for cloud publishing of in-app updates

A new GitHub Actions workflow, **"Publish in-app update"**
(`.github/workflows/publish_update.yml`), builds the APK in the cloud and
deploys it + `latest.json` to `updates.xmethod.org` — the same thing
`tools/publish-update.sh` does from your Mac, but runnable from anywhere
(including by Claude sessions). It needs these **repository secrets**
(GitHub → GLauncher repo → Settings → Secrets and variables → Actions):

- [ ] `KEYSTORE` — your signing keystore, base64-encoded
      (`base64 -i ~/.android/debug.keystore | pbcopy` if your installed
      builds are debug-signed from your Mac). **Must be the same key that
      signed the build currently on your phone**, or Android will refuse
      the update install.
- [ ] `KEYSTORE_PASSWORD` — keystore password (`android` for the default
      debug keystore)
- [ ] `KEY_ALIAS` — key alias (`androiddebugkey` for the debug keystore)
- [ ] `KEY_PASSWORD` — key password (`android` for the debug keystore)
- [ ] `XMETHOD_SSH_KEY` — private key contents for the `xmethod` SSH host
      (the one your `~/.ssh/config` alias uses)
- [ ] `XMETHOD_SSH_HOST` — the hostname behind the `xmethod` alias
- [ ] `XMETHOD_SSH_USER` — the SSH username for that host

Once set, publishing an update = run the "Publish in-app update" workflow
from the Actions tab (or ask Claude to trigger it). Version codes
auto-increment per run, so devices always see it as newer.

## Configure Google Cloud credentials for Google-powered search

The launcher now ships search providers for **Gmail, Google Docs, Sheets,
Slides, Calendar, Contacts, Tasks, and YouTube**, plus a **Sign in with
Google** button under *Home Settings → App Drawer → Search → Google account*.
The sign-in button stays disabled until you configure an OAuth client ID:

- [ ] **Create (or pick) a Google Cloud project** at
      <https://console.cloud.google.com/>.
- [ ] **Enable these APIs** under *APIs & Services → Library*:
  - [ ] Gmail API
  - [ ] Google Drive API (covers Docs, Sheets, and Slides search)
  - [ ] Google Calendar API
  - [ ] People API (Google Contacts)
  - [ ] Google Tasks API
  - [ ] YouTube Data API v3
- [ ] **Configure the OAuth consent screen** (*APIs & Services → OAuth
      consent screen*):
  - User type: **External**, publishing status **Testing** is fine for
        personal use — add your own Gmail address under **Test users**.
  - Add the scopes the app requests: `gmail.readonly`,
        `drive.metadata.readonly`, `calendar.readonly`, `contacts.readonly`,
        `tasks.readonly`, `youtube.readonly`, `openid`, `email`.
- [ ] **Create an OAuth client ID** (*APIs & Services → Credentials →
      Create credentials → OAuth client ID*):
  - Application type: **Android**
  - Package name: `app.lawnchair` (also create one for
        `app.lawnchair.debug` if you sideload debug builds, and
        `app.lawnchair.nightly` / `app.lawnchair.play` for those flavors)
  - SHA-1: from your signing key —
        `keytool -list -v -keystore <your-keystore>` (or
        `./gradlew signingReport`)
- [ ] **Paste the client ID** into
      `lawnchair/res/values/config.xml` →
      `<string name="google_oauth_client_id">…apps.googleusercontent.com</string>`
- [ ] **Rebuild and install**, then sign in from
      *Home Settings → App Drawer → Search → Google account*.

Notes:

- The app uses the OAuth authorization-code flow with PKCE for installed
  apps, so **no client secret** is needed or stored.
- The OAuth redirect returns to the app via the custom URI scheme
  `<applicationId>:/oauth2redirect` (e.g. `app.lawnchair:/oauth2redirect`).
  Android-type OAuth clients don't require registering redirect URIs.
- While the consent screen is in **Testing** mode, refresh tokens expire
  after 7 days — either re-sign-in weekly or publish the consent screen.
- YouTube Data API search costs 100 quota units per request (10,000/day
  free), so heavy YouTube searching can hit the daily quota.
