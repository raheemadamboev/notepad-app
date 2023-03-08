package xyz.teamgravity.notepad.presentation.component.pinlock

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.presentation.theme.White
import xyz.teamgravity.pin_lock_compose.PinLock

@Composable
fun NotePinLock(
    onPinCorrect: () -> Unit
) {
    PinLock(
        title = { pinExists ->
            Text(
                text = stringResource(id = if (pinExists) R.string.enter_your_pin else R.string.create_new_pin),
                color = White,
                style = MaterialTheme.typography.titleLarge
            )
        },
        color = MaterialTheme.colorScheme.primary,
        onPinCorrect = onPinCorrect,
        onPinIncorrect = {},
        onPinCreated = onPinCorrect,
    )
}