package xyz.teamgravity.notepad.presentation.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import xyz.teamgravity.pin_lock_compose.PinManager
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {

    var navigation: Navigation by mutableStateOf(Navigation.None)
        private set

    init {
        getNavigation()
    }

    private fun getNavigation() {
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