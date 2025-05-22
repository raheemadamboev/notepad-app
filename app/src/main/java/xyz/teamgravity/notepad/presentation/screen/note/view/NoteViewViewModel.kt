package xyz.teamgravity.notepad.presentation.screen.note.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramcosta.composedestinations.generated.destinations.NoteViewScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import xyz.teamgravity.notepad.core.extension.format
import xyz.teamgravity.notepad.data.model.NoteModel
import xyz.teamgravity.notepad.data.repository.NoteRepository
import javax.inject.Inject

@HiltViewModel
class NoteViewViewModel @Inject constructor(
    private val repository: NoteRepository,
    handle: SavedStateHandle
) : ViewModel() {

    private val args: NoteViewScreenArgs = NoteViewScreenDestination.argsFrom(handle)

    private var note: NoteModel? = null

    var loading: Boolean by mutableStateOf(true)
        private set

    var title: String by mutableStateOf("")
        private set

    var body: String by mutableStateOf("")
        private set

    var editedTime: String by mutableStateOf("")
        private set

    var deletedTime: String by mutableStateOf("")
        private set

    var menuExpanded: Boolean by mutableStateOf(false)
        private set

    var deleteShown: Boolean by mutableStateOf(false)
        private set

    var restoreShown: Boolean by mutableStateOf(false)
        private set

    private val _event = Channel<NoteViewEvent>()
    val event: Flow<NoteViewEvent> = _event.receiveAsFlow()

    init {
        getNote()
    }

    private fun getNote() {
        viewModelScope.launch {
            val note = repository.getNote(args.id).first() ?: return@launch
            title = note.title
            body = note.body
            editedTime = note.edited.format()
            deletedTime = note.deleted!!.format()
            this@NoteViewViewModel.note = note
            loading = false
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

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

    fun onRestoreShow() {
        restoreShown = true
    }

    fun onRestoreDismiss() {
        restoreShown = false
    }

    fun onDelete() {
        onDeleteDismiss()
        val note = note ?: return
        viewModelScope.launch {
            repository.deleteNote(note)
            _event.send(NoteViewEvent.NavigateBack(NoteViewResult.Deleted))
        }
    }

    fun onRestore() {
        onRestoreDismiss()
        val note = note ?: return
        viewModelScope.launch {
            repository.updateNote(note.copy(deleted = null))
            _event.send(NoteViewEvent.NavigateBack(NoteViewResult.Restored))
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Misc
    ///////////////////////////////////////////////////////////////////////////

    sealed interface NoteViewEvent {
        data class NavigateBack(val result: NoteViewResult) : NoteViewEvent
    }
}