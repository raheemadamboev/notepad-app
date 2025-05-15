package xyz.teamgravity.notepad.data.local.preferences

import xyz.teamgravity.coresdkandroid.preferences.PreferencesKey

enum class AppPreferencesKey(
    override val key: String,
    override val default: Any?,
    override val encrypted: Boolean
) : PreferencesKey {
    AutoSave(
        key = "xyz.teamgravity.notepad.AutoSave",
        default = false,
        encrypted = false
    );
}