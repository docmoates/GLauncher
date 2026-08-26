# GLauncher TODO

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
