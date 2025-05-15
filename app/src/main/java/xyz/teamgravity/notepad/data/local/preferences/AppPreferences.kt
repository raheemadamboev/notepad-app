package xyz.teamgravity.notepad.data.local.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.teamgravity.coresdkandroid.preferences.Preferences

class AppPreferences(
    private val preferences: Preferences
) {

    ///////////////////////////////////////////////////////////////////////////
    // Upsert
    ///////////////////////////////////////////////////////////////////////////

    suspend fun upsertAutoSave(value: Boolean) {
        preferences.upsertBoolean(
            key = AppPreferencesKey.AutoSave,
            value = value
        )
    }

    ///////////////////////////////////////////////////////////////////////////
    // Get
    ///////////////////////////////////////////////////////////////////////////

    fun getAutoSave(): Flow<Boolean> {
        return preferences.getBoolean(AppPreferencesKey.AutoSave).map { it ?: AppPreferencesKey.AutoSave.default as Boolean }
    }
}