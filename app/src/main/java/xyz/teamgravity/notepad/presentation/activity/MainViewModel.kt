package xyz.teamgravity.notepad.presentation.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.teamgravity.notepad.core.util.manager.TrashManager
import xyz.teamgravity.pin_lock_compose.PinManager
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val trash: TrashManager
) : ViewModel() {

    var loading: Boolean = true
        private set

    var navigation: Navigation by mutableStateOf(Navigation.None)
        private set

    init {
        viewModelScope.launch {
            deleteExpiredNotes()
            getNavigation()
            loading = false
        }
    }

    private suspend fun deleteExpiredNotes() {
        withContext(NonCancellable) {
            trash.deleteExpiredNotes()
        }
    }

    private suspend fun getNavigation() {
        navigation = if (PinManager.pinExists()) Navigation.PinLock else Navigation.Content
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun onPinCorrect() {
        navigation = Navigation.Content
    }

    ///////////////////////////////////////////////////////////////////////////
    // Misc
    ///////////////////////////////////////////////////////////////////////////

    enum class Navigation {
        None,
        PinLock,
        Content;
    }
}