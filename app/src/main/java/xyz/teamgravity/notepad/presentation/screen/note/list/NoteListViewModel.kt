package xyz.teamgravity.notepad.presentation.screen.note.list

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import xyz.teamgravity.coresdkandroid.update.UpdateManager
import xyz.teamgravity.notepad.data.local.preferences.AppPreferences
import xyz.teamgravity.notepad.data.local.preferences.AppPreferencesKey
import xyz.teamgravity.notepad.data.model.NoteModel
import xyz.teamgravity.notepad.data.repository.NoteRepository
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val preferences: AppPreferences,
    private val update: UpdateManager
) : ViewModel() {

    val notes: Flow<PagingData<NoteModel>> = repository.getAllNotes().cachedIn(viewModelScope)

    val autoSave: StateFlow<Boolean> = preferences.getAutoSave().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = AppPreferencesKey.AutoSave.default as Boolean
    )

    var menuExpanded: Boolean by mutableStateOf(false)
        private set

    var updateAvailableType: UpdateManager.Type by mutableStateOf(UpdateManager.Type.None)
        private set

    var updateDownloadedShown: Boolean by mutableStateOf(false)
        private set

    var deleteAllShown: Boolean by mutableStateOf(false)
        private set

    private val _event = Channel<NoteListEvent>()
    val event: Flow<NoteListEvent> = _event.receiveAsFlow()

    init {
        observe()
    }

    private fun observe() {
        observeUpdateEvent()
    }

    private suspend fun handleUpdateEvent(event: UpdateManager.UpdateEvent) {
        when (event) {
            is UpdateManager.UpdateEvent.Available -> {
                updateAvailableType = event.type
            }

            UpdateManager.UpdateEvent.StartDownload -> {
                _event.send(NoteListEvent.DownloadAppUpdate)
            }

            UpdateManager.UpdateEvent.Downloaded -> {
                updateDownloadedShown = true
            }
        }
    }

    private fun observeUpdateEvent() {
        viewModelScope.launch {
            update.event.collect { event ->
                handleUpdateEvent(event)
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun onUpdateCheck() {
        update.monitor()
    }

    fun onUpdateDownload(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        update.downloadAppUpdate(launcher)
    }

    fun onUpdateAvailableDismiss() {
        updateAvailableType = UpdateManager.Type.None
    }

    fun onUpdateAvailableConfirm() {
        viewModelScope.launch {
            _event.send(NoteListEvent.DownloadAppUpdate)
        }
    }

    fun onUpdateDownloadedDismiss() {
        updateDownloadedShown = false
    }

    fun onUpdateInstall() {
        update.installAppUpdate()
    }

    fun onAutoSaveChange() {
        onMenuCollapse()
        viewModelScope.launch(NonCancellable) {
            preferences.upsertAutoSave(!autoSave.value)
        }
    }

    fun onMenuExpand() {
        menuExpanded = true
    }

    fun onMenuCollapse() {
        menuExpanded = false
    }

    fun onDeleteAllShow() {
        deleteAllShown = true
        onMenuCollapse()
    }

    fun onDeleteAllDismiss() {
        deleteAllShown = false
    }

    fun onDeleteAll() {
        onDeleteAllDismiss()
        viewModelScope.launch(NonCancellable) {
            repository.deleteAllNotes()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Misc
    ///////////////////////////////////////////////////////////////////////////

    enum class NoteListEvent {
        DownloadAppUpdate;
    }
}