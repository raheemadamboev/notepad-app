package xyz.teamgravity.notepad.presentation.component.pinlock

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import xyz.teamgravity.coresdkcompose.text.TextPlain
import xyz.teamgravity.notepad.R

@Composable
fun PinLockDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            TextPlain(
                id = R.string.warning
            )
        },
        text = {
            TextPlain(
                id = R.string.pin_lock_warning
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                TextPlain(
                    id = R.string.got_it
                )
            }
        }
    )
}