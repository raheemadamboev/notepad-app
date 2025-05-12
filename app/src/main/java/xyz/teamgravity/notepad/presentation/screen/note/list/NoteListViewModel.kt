package xyz.teamgravity.notepad.presentation.screen.note.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xyz.teamgravity.notepad.data.local.preferences.AppPreferences
import xyz.teamgravity.notepad.data.local.preferences.AppPreferencesKey
import xyz.teamgravity.notepad.data.model.NoteModel
import xyz.teamgravity.notepad.data.repository.NoteRepository
import xyz.teamgravity.pin_lock_compose.PinManager
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val preferences: AppPreferences,
) : ViewModel() {

    var pinLockShown: Boolean by mutableStateOf(false)
        private set

    var notes: List<NoteModel> by mutableStateOf(emptyList())
        private set

    var autoSave: Boolean by mutableStateOf(AppPreferencesKey.AutoSave.default as Boolean)
        private set

    var menuExpanded: Boolean by mutableStateOf(false)
        private set

    var deleteAllDialogShown: Boolean by mutableStateOf(false)
        private set

    init {
        checkPinLock()
        observe()
    }

    private fun checkPinLock() {
        pinLockShown = PinManager.pinExists()
    }

    private fun observe() {
        observeNotes()
        observeAutoSave()
    }

    private fun observeNotes() {
        viewModelScope.launch {
            repository.getAllNotes().collectLatest { notes ->
                this@NoteListViewModel.notes = notes
            }
        }
    }

    private fun observeAutoSave() {
        viewModelScope.launch {
            preferences.getAutoSave().collectLatest { autoSave ->
                this@NoteListViewModel.autoSave = autoSave
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun onPinCorrect() {
        pinLockShown = false
    }

    fun onAutoSaveChange() {
        onMenuCollapse()
        viewModelScope.launch(NonCancellable) {
            preferences.upsertAutoSave(!autoSave)
        }
    }

    fun onMenuExpand() {
        menuExpanded = true
    }

    fun onMenuCollapse() {
        menuExpanded = false
    }

    fun onDeleteAllDialogShow() {
        deleteAllDialogShown = true
        onMenuCollapse()
    }

    fun onDeleteAllDialogDismiss() {
        deleteAllDialogShown = false
    }

    fun onDeleteAll() {
        onDeleteAllDialogDismiss()
        viewModelScope.launch(NonCancellable) {
            repository.deleteAllNotes()
        }
    }
}