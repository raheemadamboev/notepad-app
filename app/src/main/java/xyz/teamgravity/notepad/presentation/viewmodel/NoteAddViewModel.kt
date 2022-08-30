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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import xyz.teamgravity.notepad.data.model.NoteModel
import xyz.teamgravity.notepad.data.repository.NoteRepository
import javax.inject.Inject

@HiltViewModel
class NoteAddViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val repository: NoteRepository
) : ViewModel() {

    companion object {
        private const val NOTE_TITLE = "note_title"
        private const val DEFAULT_NOTE_TITLE = ""

        private const val NOTE_BODY = "note_body"
        private const val DEFAULT_NOTE_BODY = ""
    }

    private val _event = Channel<NoteAddEvent>()
    val event: Flow<NoteAddEvent> = _event.receiveAsFlow()

    var title: String by mutableStateOf(handle.get<String>(NOTE_TITLE) ?: DEFAULT_NOTE_TITLE)
        private set

    var body: String by mutableStateOf(handle.get<String>(NOTE_BODY) ?: DEFAULT_NOTE_BODY)
        private set

    fun onTitleChange(value: String) {
        title = value
        handle[NOTE_TITLE] = value
    }

    fun onBodyChange(value: String) {
        body = value
        handle[NOTE_BODY] = value
    }

    fun onSaveNote() {
        viewModelScope.launch {
            repository.insertNote(
                NoteModel(
                    title = title,
                    body = body,
                )
            )

            _event.send(NoteAddEvent.NoteAdded)
        }
    }

    sealed class NoteAddEvent {
        object NoteAdded : NoteAddEvent()
    }
}