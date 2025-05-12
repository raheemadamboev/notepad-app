package xyz.teamgravity.notepad.presentation.screen.pinlock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import xyz.teamgravity.pin_lock_compose.PinManager
import javax.inject.Inject

@HiltViewModel
class PinLockViewModel @Inject constructor(
) : ViewModel() {

    var pinLockState: PinLockState by mutableStateOf(PinLockState.Content)
        private set

    var pinLockEnabled: Boolean by mutableStateOf(PinManager.pinExists())
        private set

    var pinLockDialogShown: Boolean by mutableStateOf(false)
        private set

    private fun changePinLockState(state: PinLockState) {
        pinLockState = state
    }

    private fun showPinLockDialog() {
        pinLockDialogShown = true
    }

    private fun hidePinLockDialog() {
        pinLockDialogShown = false
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun onPinLockEnabledChange() {
        pinLockEnabled = PinManager.pinExists()
        if (pinLockEnabled) changePinLockState(PinLockState.Remove) else showPinLockDialog()
    }

    fun onPinLockChange() {
        if (pinLockEnabled) changePinLockState(PinLockState.Change)
    }

    fun onPinLockDialogConfirm() {
        hidePinLockDialog()
        changePinLockState(PinLockState.Create)
    }

    fun onPinLockDialogDismiss() {
        hidePinLockDialog()
    }

    fun onPinLockCorrect() {
        if (pinLockState == PinLockState.Remove) PinManager.clearPin()
        pinLockEnabled = PinManager.pinExists()
        changePinLockState(PinLockState.Content)
    }

    ///////////////////////////////////////////////////////////////////////////
    // MISC
    ///////////////////////////////////////////////////////////////////////////

    enum class PinLockState {
        Content,
        Create,
        Remove,
        Change;
    }
}