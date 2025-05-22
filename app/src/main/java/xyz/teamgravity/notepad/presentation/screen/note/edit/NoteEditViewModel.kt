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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
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
    private val saver: AutoSaver,
    handle: SavedStateHandle
) : ViewModel() {

    private val args: NoteEditScreenArgs = NoteEditScreenDestination.argsFrom(handle)

    private val note: StateFlow<NoteModel?> = repository.getNote(args.id).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

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
        observe()
    }

    private fun observe() {
        observeNote()
    }

    private fun handleNote(note: NoteModel?) {
        if (note == null) return
        title = note.title
        body = note.body
        initializeAutoSaver(note)
    }

    private fun initializeAutoSaver(note: NoteModel) {
        viewModelScope.launch {
            autoSave = preferences.getAutoSave().first()
            if (autoSave) {
                saver.start(
                    note = note,
                    title = { title },
                    body = { body }
                )
            }
        }
    }

    private fun observeNote() {
        viewModelScope.launch {
            note.collectLatest { note ->
                handleNote(note)
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

    fun onUpdateNote() {
        val note = note.value ?: return
        viewModelScope.launch {
            repository.updateNote(
                note.copy(
                    title = title,
                    body = body,
                    edited = LocalDateTime.now()
                )
            )

            _event.send(NoteEditEvent.NoteUpdated)
        }
    }

    fun onDeleteNote() {
        val note = note.value ?: return
        viewModelScope.launch {
            onDeleteDismiss()

            if (autoSave) saver.close()
            repository.updateNote(note.copy(deleted = LocalDateTime.now()))

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
    // Misc
    ///////////////////////////////////////////////////////////////////////////

    enum class NoteEditEvent {
        NoteUpdated;
    }
}