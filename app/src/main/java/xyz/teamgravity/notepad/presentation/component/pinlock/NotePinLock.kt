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
import xyz.teamgravity.pin_lock_compose.PinLock

@Composable
fun NotePinLock(
    onPinCorrect: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
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
            onPinCreated = onPinCorrect
        )
    }
}