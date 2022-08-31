package xyz.teamgravity.notepad.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = xyz.teamgravity.notepad.data.local.preferences.Preferences.PREFS)

class Preferences(context: Context) {

    companion object {
        /**
         *  Preferences name
         */
        const val PREFS = "prefs"

        /**
         * Auto-save enabled
         */
        private val AUTO_SAVE = booleanPreferencesKey("autoSave")
        private const val DEFAULT_AUTO_SAVE = false
    }

    private val store = context.dataStore

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
        .catch { handleIOException(it) }
        .map { it[AUTO_SAVE] ?: DEFAULT_AUTO_SAVE }

    ///////////////////////////////////////////////////////////////////////////
    // MISC
    ///////////////////////////////////////////////////////////////////////////

    private fun handleIOException(t: Throwable): Preferences {
        return if (t is IOException) emptyPreferences() else throw t
    }
}