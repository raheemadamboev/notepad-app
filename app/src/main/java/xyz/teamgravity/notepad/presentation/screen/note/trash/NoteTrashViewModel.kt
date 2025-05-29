package xyz.teamgravity.notepad.presentation.screen.note.trash

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.core.util.manager.TrashManager
import xyz.teamgravity.notepad.data.model.NoteModel
import xyz.teamgravity.notepad.data.repository.NoteRepository
import javax.inject.Inject

@HiltViewModel
class NoteTrashViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val trash: TrashManager
) : ViewModel() {

    val notes: Flow<PagingData<NoteModel>> = repository.getDeletedNotes().cachedIn(viewModelScope)

    var menuExpanded: Boolean by mutableStateOf(false)
        private set

    var deleteAllShown: Boolean by mutableStateOf(false)
        private set

    var restoreAllShown: Boolean by mutableStateOf(false)
        private set

    private val _event = Channel<NoteTrashEvent>()
    val event: Flow<NoteTrashEvent> = _event.receiveAsFlow()

    init {
        deleteExpiredNotes()
    }

    private fun deleteExpiredNotes() {
        viewModelScope.launch(NonCancellable) {
            trash.deleteExpiredNotes()
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

    fun onDeleteAllShow() {
        deleteAllShown = true
    }

    fun onDeleteAllDismiss() {
        deleteAllShown = false
    }

    fun onRestoreAllShow() {
        restoreAllShown = true
    }

    fun onRestoreAllDismiss() {
        restoreAllShown = false
    }

    fun onDeleteAll() {
        onDeleteAllDismiss()
        viewModelScope.launch {
            withContext(NonCancellable) {
                repository.deleteAllDeletedNotes()
            }
            _event.send(NoteTrashEvent.ShowMessage(R.string.all_notes_deleted_trash))
        }
    }

    fun onRestoreAll() {
        onRestoreAllDismiss()
        viewModelScope.launch {
            withContext(NonCancellable) {
                repository.restoreDeletedNotes()
            }
            _event.send(NoteTrashEvent.ShowMessage(R.string.all_deleted_notes_restored))
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Misc
    ///////////////////////////////////////////////////////////////////////////

    sealed interface NoteTrashEvent {
        data class ShowMessage(@StringRes val message: Int) : NoteTrashEvent
    }
}