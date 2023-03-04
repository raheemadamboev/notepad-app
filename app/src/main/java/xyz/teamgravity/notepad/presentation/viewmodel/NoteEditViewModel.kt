package xyz.teamgravity.notepad.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import xyz.teamgravity.notepad.core.util.AutoSaver
import xyz.teamgravity.notepad.data.local.preferences.Preferences
import xyz.teamgravity.notepad.data.repository.NoteRepository
import xyz.teamgravity.notepad.presentation.screen.destinations.NoteEditScreenDestination
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NoteEditViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val repository: NoteRepository,
    private val preferences: Preferences,
    private val saver: AutoSaver,
) : ViewModel() {

    companion object {
        private const val NOTE_TITLE = "note_title"
        private const val NOTE_BODY = "note_body"

        private const val MENU_EXPANDED = "menu_expanded"
        private const val DEFAULT_MENU_EXPANDED = false

        private const val DELETE_DIALOG_SHOWN = "delete_dialog_shown"
        private const val DEFAULT_DELETE_DIALOG_SHOWN = false
    }

    private val args = NoteEditScreenDestination.argsFrom(handle)

    private val _event = Channel<NoteEditEvent>()
    val event: Flow<NoteEditEvent> = _event.receiveAsFlow()

    var title: String by mutableStateOf(handle.get<String>(NOTE_TITLE) ?: args.note.title)
        private set

    var body: String by mutableStateOf(handle.get<String>(NOTE_BODY) ?: args.note.body)
        private set

    var menuExpanded: Boolean by mutableStateOf(handle.get<Boolean>(MENU_EXPANDED) ?: DEFAULT_MENU_EXPANDED)
        private set

    var deleteDialogShown: Boolean by mutableStateOf(handle.get<Boolean>(DELETE_DIALOG_SHOWN) ?: DEFAULT_DELETE_DIALOG_SHOWN)
        private set

    var autoSave: Boolean by mutableStateOf(Preferences.DEFAULT_AUTO_SAVE)
        private set

    val sharedNote: String
        get() = "$title\n\n$body"

    init {
        initializeAutoSaver()
    }

    private fun initializeAutoSaver() {
        viewModelScope.launch {
            autoSave = preferences.autoSave.first()
            if (autoSave) {
                saver.start(
                    note = args.note,
                    title = { title },
                    body = { body }
                )
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun onTitleChange(value: String) {
        title = value
        handle[NOTE_TITLE] = value
    }

    fun onBodyChange(value: String) {
        body = value
        handle[NOTE_BODY] = value
    }

    fun onMenuExpand() {
        menuExpanded = true
        handle[MENU_EXPANDED] = true
    }

    fun onMenuCollapse() {
        menuExpanded = false
        handle[MENU_EXPANDED] = false
    }

    fun onDeleteDialogShow() {
        deleteDialogShown = true
        handle[DELETE_DIALOG_SHOWN] = true
        onMenuCollapse()
    }

    fun onDeleteDialogDismiss() {
        deleteDialogShown = false
        handle[DELETE_DIALOG_SHOWN] = false
    }

    fun onUpdateNote() {
        viewModelScope.launch {
            repository.updateNote(
                args.note.copy(
                    title = title,
                    body = body,
                    edited = Date()
                )
            )

            _event.send(NoteEditEvent.NoteUpdated)
        }
    }

    fun onDeleteNote() {
        viewModelScope.launch {
            onDeleteDialogDismiss()

            repository.deleteNote(args.note)
            if (autoSave) saver.close()

            _event.send(NoteEditEvent.NoteUpdated)
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (autoSave) {
            saver.saveAndClose(
                title = title,
                body = body
            )
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // MISC
    ///////////////////////////////////////////////////////////////////////////

    sealed class NoteEditEvent {
        object NoteUpdated : NoteEditEvent()
    }
}