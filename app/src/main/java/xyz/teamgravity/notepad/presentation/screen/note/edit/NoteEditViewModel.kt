package xyz.teamgravity.notepad.presentation.screen.note.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramcosta.composedestinations.generated.destinations.NoteEditScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import xyz.teamgravity.notepad.core.util.AutoSaver
import xyz.teamgravity.notepad.data.local.preferences.AppPreferences
import xyz.teamgravity.notepad.data.local.preferences.AppPreferencesKey
import xyz.teamgravity.notepad.data.model.NoteModel
import xyz.teamgravity.notepad.data.repository.NoteRepository
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NoteEditViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val preferences: AppPreferences,
    private val autoSaver: AutoSaver,
    handle: SavedStateHandle
) : ViewModel() {

    private val args: NoteEditScreenArgs = NoteEditScreenDestination.argsFrom(handle)

    private var note: NoteModel? = null

    var title: String by mutableStateOf("")
        private set

    var body: String by mutableStateOf("")
        private set

    var menuExpanded: Boolean by mutableStateOf(false)
        private set

    var deleteShown: Boolean by mutableStateOf(false)
        private set

    var autoSave: Boolean by mutableStateOf(AppPreferencesKey.AutoSave.default as Boolean)
        private set

    private val _event = Channel<NoteEditEvent>()
    val event: Flow<NoteEditEvent> = _event.receiveAsFlow()

    val sharedNote: String
        get() = "$title\n\n$body"

    init {
        getNote()
    }

    private fun getNote() {
        viewModelScope.launch {
            val note = repository.getNote(args.id).first() ?: return@launch
            title = note.title
            body = note.body
            this@NoteEditViewModel.note = note
            initializeAutoSaver(note)
        }
    }

    private fun initializeAutoSaver(note: NoteModel) {
        viewModelScope.launch {
            autoSave = preferences.getAutoSave().first()
            if (autoSave) {
                autoSaver.start(
                    resolution = AutoSaver.EmptyResolution.MoveToTrash,
                    note = note,
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
    }

    fun onBodyChange(value: String) {
        body = value
    }

    fun onMenuExpand() {
        menuExpanded = true
    }

    fun onMenuCollapse() {
        menuExpanded = false
    }

    fun onDeleteShow() {
        deleteShown = true
    }

    fun onDeleteDismiss() {
        deleteShown = false
    }

    fun onAutoSave() {
        if (autoSave) {
            autoSaver.save(
                title = title,
                body = body
            )
        }
    }

    fun onUpdateNote() {
        val note = note ?: return
        viewModelScope.launch {
            if (title.isBlank() && body.isBlank()) {
                repository.updateNote(
                    note.copy(
                        deleted = LocalDateTime.now()
                    )
                )
            } else {
                repository.updateNote(
                    note.copy(
                        title = title,
                        body = body,
                        edited = LocalDateTime.now()
                    )
                )
            }
            _event.send(NoteEditEvent.NoteUpdated)
        }
    }

    fun onDeleteNote() {
        val note = autoSaver.getCurrentNote() ?: note ?: return
        viewModelScope.launch {
            onDeleteDismiss()

            if (autoSave) autoSaver.close()
            repository.updateNote(
                note.copy(
                    deleted = LocalDateTime.now()
                )
            )

            _event.send(NoteEditEvent.NoteUpdated)
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (autoSave) {
            autoSaver.saveAndClose(
                title = title,
                body = body
            )
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Misc
    ///////////////////////////////////////////////////////////////////////////

    enum class NoteEditEvent {
        NoteUpdated;
    }
}