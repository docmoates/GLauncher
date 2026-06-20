# Repository guide for Claude Code

You are editing a **single-module Android app** (Kotlin + Jetpack Compose) that
is compiled in CI by `gradle :app:assembleDebug` and shipped to a phone as an APK.
Work toward the user's prompt while keeping these invariants true.

## Project layout
- `app/src/main/java/com/pocketforge/app/MainActivity.kt` — entry point + root UI.
- `app/src/main/res/` — resources (strings, themes, drawables).
- `app/build.gradle.kts` — module config + dependencies.

## Rules
1. **Keep it compiling.** The build runs `gradle :app:assembleDebug`. Don't leave
   the project in a state that won't assemble. Prefer small, complete changes.
2. **Use Jetpack Compose + Material 3** for UI. Add new Composables/files as needed.
3. **Add dependencies** in `app/build.gradle.kts` using the existing Compose BOM
   style when a feature needs them. Use stable versions.
4. **Do not change** `namespace` / `applicationId` (`com.pocketforge.app`),
   `minSdk` (24), Java 17, or anything under `.github/workflows/`.
5. **Do not run Gradle or git yourself** — CI commits, builds, and releases after you finish.
6. Keep the change **focused** on the prompt. Don't refactor unrelated code.
7. If you add permissions, declare them in `AndroidManifest.xml`.

## Style
- Match the existing code's formatting and Kotlin idioms.
- Prefer `mutableStateOf` / `remember` for simple UI state; introduce a ViewModel
  only when state outlives recomposition or needs to survive config changes.
