package xyz.teamgravity.notepad.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import xyz.teamgravity.notepad.data.model.NoteModel
import xyz.teamgravity.notepad.data.repository.NoteRepository
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    var notes: List<NoteModel> by mutableStateOf(emptyList())
        private set

    var expanded: Boolean by mutableStateOf(false)
        private set

    var deleteAllDialog: Boolean by mutableStateOf(false)
        private set

    init {
        observe()
    }

    fun onMenuExpand() {
        expanded = true
    }

    fun onMenuCollapse() {
        expanded = false
    }

    fun onDeleteAllDialogShow() {
        deleteAllDialog = true
        onMenuCollapse()
    }

    fun onDeleteAllDialogDismiss() {
        deleteAllDialog = false
    }

    fun onDeleteAll() {
        onDeleteAllDialogDismiss()
        viewModelScope.launch(NonCancellable) {
            repository.deleteAllNotes()
        }
    }

    private fun observe() {
        observeNotes()
    }

    private fun observeNotes() {
        viewModelScope.launch {
            repository.getAllNotes().collectLatest { notes ->
                this@NoteListViewModel.notes = notes
            }
        }
    }
}