package xyz.teamgravity.notepad.presentation.screen.note.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xyz.teamgravity.notepad.data.local.preferences.Preferences
import xyz.teamgravity.notepad.data.model.NoteModel
import xyz.teamgravity.notepad.data.repository.NoteRepository
import xyz.teamgravity.pin_lock_compose.PinManager
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val repository: NoteRepository,
    private val preferences: Preferences,
) : ViewModel() {

    companion object {
        private const val MENU_EXPANDED = "menu_expanded"
        private const val DEFAULT_MENU_EXPANDED = false

        private const val DELETE_ALL_DIALOG_SHOWN = "delete_all_dialog_shown"
        private const val DEFAULT_DELETE_ALL_DIALOG_SHOWN = false
    }

    var pinLockShown: Boolean by mutableStateOf(false)
        private set

    var notes: List<NoteModel> by mutableStateOf(emptyList())
        private set

    var autoSave: Boolean by mutableStateOf(Preferences.DEFAULT_AUTO_SAVE)
        private set

    var menuExpanded: Boolean by mutableStateOf(handle.get<Boolean>(MENU_EXPANDED) ?: DEFAULT_MENU_EXPANDED)
        private set

    var deleteAllDialogShown: Boolean by mutableStateOf(handle.get<Boolean>(DELETE_ALL_DIALOG_SHOWN) ?: DEFAULT_DELETE_ALL_DIALOG_SHOWN)
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
            preferences.autoSave.collectLatest { autoSave ->
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
            preferences.updateAutoSave(!autoSave)
        }
    }

    fun onMenuExpand() {
        menuExpanded = true
        handle[MENU_EXPANDED] = true
    }

    fun onMenuCollapse() {
        menuExpanded = false
        handle[MENU_EXPANDED] = false
    }

    fun onDeleteAllDialogShow() {
        deleteAllDialogShown = true
        handle[DELETE_ALL_DIALOG_SHOWN] = true
        onMenuCollapse()
    }

    fun onDeleteAllDialogDismiss() {
        deleteAllDialogShown = false
        handle[DELETE_ALL_DIALOG_SHOWN] = false
    }

    fun onDeleteAll() {
        onDeleteAllDialogDismiss()
        viewModelScope.launch(NonCancellable) {
            repository.deleteAllNotes()
        }
    }
}