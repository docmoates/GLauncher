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
- [ ] `XMETHOD_SSH_KEY` — **the only deploy secret you must add.** Contents
      of `~/.ssh/id_rsa` on your Mac (the key the `xmethod` SSH alias uses):
      `cat ~/.ssh/id_rsa` → paste the whole `-----BEGIN…END-----` block.
- `XMETHOD_SSH_HOST` / `XMETHOD_SSH_USER` — **not needed.** The workflow
      already defaults to `wiki.xmethod.net` / `azureuser` (the Azure VM that
      serves updates.xmethod.org via nginx from `/var/www/glauncher-updates/`).
      Only set these secrets if that infrastructure ever moves.

Confirmed by probing the live endpoint: `https://updates.xmethod.org/latest.json`
is served by nginx on the Azure VM and already hosts published GLauncher
builds, so the deploy path and manifest format are known-good.

Once set, publishing an update = run the "Publish in-app update" workflow
from the Actions tab (or ask Claude to trigger it). Version codes
auto-increment per run, so devices always see it as newer.

## Google-powered search (configured — no action needed)

Gmail, Docs, Sheets, Slides, Calendar, Contacts, Tasks and YouTube search are
authorized through the **XMethod portal** (`https://portal.xmethod.org`), not a
per-app Google Cloud project. The portal owns the Google OAuth client (id *and*
secret) and stores the Google refresh token encrypted server-side, so there is
no client ID to paste into this repo and no Cloud Console work to do.

How it works (`XmethodPortal.kt` + `GoogleAccountManager.kt`):

1. *Home Settings → App Drawer → Search → XMethod account → Sign in to XMethod*
   posts your portal e-mail/password to `POST /api/v1/browser/login` and stores
   the returned `xm_…` API key.
2. Sign-in immediately probes `POST /api/v1/browser/connections/google/refresh`.
   If you already connected Google from the XMethod Browser desktop app, the
   launcher adopts that connection and you are done — no second consent screen.
3. Otherwise *Connect Google* fetches the public client id from
   `GET /api/v1/browser/oauth-config`, runs the Google consent flow with PKCE and
   a loopback redirect (`http://127.0.0.1:<ephemeral-port>/callback` — the
   portal's Google client is a Desktop-app client, so any loopback port is
   accepted), and hands the code to
   `POST /api/v1/browser/connections/google/exchange`. The portal exchanges it
   with its own client secret and returns only a short-lived access token.
4. Search refreshes that token through the portal as needed. The device never
   holds a Google refresh token.

Notes:

- The launcher requests the same scope set as the XMethod Browser
  (`SCOPE_MAP` in its `src/main/google-auth.js`), so one consent covers every
  XMethod client. That set is broader than the launcher's read-only needs; if
  you want the launcher narrowed to `*.readonly` scopes, those scopes have to be
  added to the portal's OAuth consent screen first or Google rejects them.
- *Sign out* of XMethod clears the portal key and the cached Google token.
  *Disconnect* on the Google row additionally calls
  `DELETE /api/v1/browser/connections/google`, which revokes the grant with
  Google and deletes the stored connection.
