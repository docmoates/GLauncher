#!/usr/bin/env bash
#
# Publish the current GLauncher debug APK to updates.xmethod.org so the
# in-app "Check for Updates" (Settings > More > Software Update) picks it
# up. Reads versionName/versionCode straight from the built APK with aapt,
# computes its SHA-256, and deploys both the APK and the latest.json
# manifest via scp (the "xmethod" SSH host alias must be configured).
#
# Usage:
#   tools/publish-update.sh [release-notes]
#
# The APK itself is found automatically under
# build/outputs/apk/lawnWithQuickstepGithub/debug/ (build it first with
# `./gradlew :assembleLawnWithQuickstepGithubDebug`).
set -euo pipefail

NOTES="${1:-}"
REMOTE_HOST="xmethod"
REMOTE_DIR="/var/www/glauncher-updates"
BASE_URL="https://updates.xmethod.org"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

APK="$(find "$REPO_ROOT/build/outputs/apk/lawnWithQuickstepGithub/debug" -name '*.apk' | head -1)"
[ -n "$APK" ] || { echo "✗ No APK found - build first: ./gradlew :assembleLawnWithQuickstepGithubDebug" >&2; exit 1; }

ANDROID_HOME="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
AAPT="$(ls "$ANDROID_HOME"/build-tools/*/aapt 2>/dev/null | sort -V | tail -1)"
[ -n "$AAPT" ] || { echo "✗ aapt not found under $ANDROID_HOME/build-tools" >&2; exit 1; }

BADGING="$("$AAPT" dump badging "$APK")"
VNAME="$(printf '%s\n' "$BADGING" | grep -oE "versionName='[^']*'" | head -1 | cut -d"'" -f2)"
VCODE="$(printf '%s\n' "$BADGING" | grep -oE "versionCode='[^']*'" | head -1 | cut -d"'" -f2)"
[ -n "$VNAME" ] && [ -n "$VCODE" ] || { echo "✗ Could not read version from APK" >&2; exit 1; }

SHA="$(shasum -a 256 "$APK" | awk '{print $1}')"
SIZE="$(stat -f%z "$APK" 2>/dev/null || stat -c%s "$APK")"

echo "→ GLauncher v$VNAME ($VCODE)  $(du -h "$APK" | cut -f1)"

MANIFEST="$(mktemp)"
trap 'rm -f "$MANIFEST"' EXIT
python3 - "$MANIFEST" <<PYEOF
import json, sys
json.dump({
    "versionCode": $VCODE,
    "versionName": "$VNAME",
    "releaseNotes": """$NOTES""",
    "downloadUrl": "$BASE_URL/GLauncher.apk",
    "sha256": "$SHA",
    "fileSize": $SIZE,
}, open(sys.argv[1], "w"), indent=2)
PYEOF

scp "$APK" "$REMOTE_HOST:$REMOTE_DIR/GLauncher.apk"
scp "$MANIFEST" "$REMOTE_HOST:$REMOTE_DIR/latest.json"

echo "  ✓ published: $BASE_URL/latest.json"
curl -sf "$BASE_URL/latest.json" | python3 -m json.tool
