package xyz.teamgravity.notepad.presentation.screen.pinlock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.pin_lock_compose.PinManager
import javax.inject.Inject

@HiltViewModel
class PinLockViewModel @Inject constructor(
) : ViewModel() {

    var pinLockState: PinLockState by mutableStateOf(PinLockState.Content)
        private set

    var pinLockEnabled: Boolean by mutableStateOf(false)
        private set

    var pinLockWarningShown: Boolean by mutableStateOf(false)
        private set

    init {
        getPinlockEnabled()
    }

    private fun getPinlockEnabled() {
        viewModelScope.launch {
            pinLockEnabled = PinManager.pinExists()
        }
    }

    private fun changePinLockState(state: PinLockState) {
        pinLockState = state
    }

    private fun showPinLockWarning() {
        pinLockWarningShown = true
    }

    private fun hidePinLockWarning() {
        pinLockWarningShown = false
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun onPinLockEnabledChange() {
        viewModelScope.launch {
            pinLockEnabled = PinManager.pinExists()
            if (pinLockEnabled) changePinLockState(PinLockState.Remove) else showPinLockWarning()
        }
    }

    fun onPinLockChange() {
        if (pinLockEnabled) changePinLockState(PinLockState.Change)
    }

    fun onPinLockWarningConfirm() {
        hidePinLockWarning()
        changePinLockState(PinLockState.Create)
    }

    fun onPinLockWarningDismiss() {
        hidePinLockWarning()
    }

    fun onPinLockCorrect() {
        viewModelScope.launch {
            if (pinLockState == PinLockState.Remove) PinManager.clearPin()
            pinLockEnabled = PinManager.pinExists()
            changePinLockState(PinLockState.Content)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Misc
    ///////////////////////////////////////////////////////////////////////////

    enum class PinLockState {
        Content,
        Create,
        Remove,
        Change;
    }
}