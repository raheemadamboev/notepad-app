package xyz.teamgravity.notepad.presentation.screen.pinlock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import xyz.teamgravity.pin_lock_compose.PinManager
import javax.inject.Inject

@HiltViewModel
class PinLockViewModel @Inject constructor(
    private val handle: SavedStateHandle,
) : ViewModel() {

    companion object {
        private const val PIN_LOCK_STATE = "pin_lock_state"
        private val DEFAULT_PIN_LOCK_STATE = PinLockState.Content

        private const val PIN_LOCK_DIALOG_SHOWN = "pin_lock_dialog_shown"
        private const val DEFAULT_PIN_LOCK_DIALOG_SHOWN = false
    }

    var pinLockState: PinLockState by mutableStateOf(handle[PIN_LOCK_STATE] ?: DEFAULT_PIN_LOCK_STATE)
        private set

    var pinLockEnabled: Boolean by mutableStateOf(PinManager.pinExists())
        private set

    var pinLockDialogShown: Boolean by mutableStateOf(handle[PIN_LOCK_DIALOG_SHOWN] ?: DEFAULT_PIN_LOCK_DIALOG_SHOWN)
        private set

    private fun changePinLockState(state: PinLockState) {
        pinLockState = state
        handle[PIN_LOCK_STATE] = state
    }

    private fun showPinLockDialog() {
        pinLockDialogShown = true
        handle[PIN_LOCK_DIALOG_SHOWN] = true
    }

    private fun hidePinLockDialog() {
        pinLockDialogShown = false
        handle[PIN_LOCK_DIALOG_SHOWN] = false
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
        Change
    }
}