package xyz.teamgravity.notepad.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException


class Preferences(context: Context) {

    companion object {
        /**
         *  Preferences name.
         */
        const val PREFS = "prefs"

        /**
         * Auto-save enabled.
         */
        private val AUTO_SAVE = booleanPreferencesKey("autoSave")
        const val DEFAULT_AUTO_SAVE = false
    }

    private val Context.store by preferencesDataStore(name = PREFS)
    private val store = context.store

    ///////////////////////////////////////////////////////////////////////////
    // UPDATE
    ///////////////////////////////////////////////////////////////////////////

    suspend fun updateAutoSave(value: Boolean) {
        withContext(Dispatchers.IO) {
            store.edit { it[AUTO_SAVE] = value }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // GET
    ///////////////////////////////////////////////////////////////////////////

    val autoSave: Flow<Boolean> = store.data
        .catch { emit(handleIOException(it)) }
        .map { it[AUTO_SAVE] ?: DEFAULT_AUTO_SAVE }

    ///////////////////////////////////////////////////////////////////////////
    // MISC
    ///////////////////////////////////////////////////////////////////////////

    private fun handleIOException(t: Throwable): Preferences {
        return if (t is IOException) emptyPreferences() else throw t
    }
}