package xyz.teamgravity.notepad.presentation.screen.note.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import javax.inject.Inject

@HiltViewModel
class NoteAddViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val preferences: AppPreferences,
    private val autoSaver: AutoSaver
) : ViewModel() {

    var title: String by mutableStateOf("")
        private set

    var body: String by mutableStateOf("")
        private set

    var autoSave: Boolean by mutableStateOf(AppPreferencesKey.AutoSave.default as Boolean)
        private set

    private val _event = Channel<NoteAddEvent>()
    val event: Flow<NoteAddEvent> = _event.receiveAsFlow()

    init {
        initializeAutoSaver()
    }

    private fun initializeAutoSaver() {
        viewModelScope.launch {
            autoSave = preferences.getAutoSave().first()
            if (autoSave) {
                autoSaver.start(
                    resolution = AutoSaver.EmptyResolution.Delete,
                    note = null,
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

    fun onAutoSave() {
        if (autoSave) {
            autoSaver.save(
                title = title,
                body = body
            )
        }
    }

    fun onSaveNote() {
        viewModelScope.launch {
            if (title.isNotBlank() || body.isNotBlank()) {
                repository.insertNote(
                    NoteModel(
                        title = title,
                        body = body
                    )
                )
            }

            _event.send(NoteAddEvent.NavigateBack)
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

    enum class NoteAddEvent {
        NavigateBack;
    }
}