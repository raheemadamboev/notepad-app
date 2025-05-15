package xyz.teamgravity.notepad.presentation.component.pinlock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.presentation.theme.White
import xyz.teamgravity.pin_lock_compose.ChangePinLock

@Composable
fun NoteChangePinLock(
    onPinChanged: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        ChangePinLock(
            title = { authenticated ->
                Text(
                    text = stringResource(id = if (authenticated) R.string.create_new_pin else R.string.enter_your_pin),
                    color = White,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            color = MaterialTheme.colorScheme.primary,
            onPinIncorrect = {},
            onPinChanged = onPinChanged
        )
    }
}