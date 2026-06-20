# ForgeApp (PocketForge template)

A minimal Jetpack Compose Android app. PocketForge dispatches the **Claude Build**
workflow against this repo: Claude Code edits the app, commits, pushes, and CI
publishes the compiled APK as a GitHub Release.

- Build locally: `./gradlew :app:assembleDebug` (or open in Android Studio).
- CI: `.github/workflows/claude-build.yml` (triggered by PocketForge).
- Agent guide: [`CLAUDE.md`](CLAUDE.md).

> This repo is meant to be used as a **GitHub template**. New apps are created
> from it by PocketForge ("New app"), which calls the template-generate API.
