package xyz.teamgravity.notepad.presentation.component.dialog

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import xyz.teamgravity.coresdkcompose.text.TextPlain
import xyz.teamgravity.notepad.R

@Composable
fun NoteAlertDialog(
    @StringRes title: Int,
    @StringRes message: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            TextPlain(
                id = title
            )
        },
        text = {
            TextPlain(
                id = message
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                TextPlain(
                    id = R.string.no
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                TextPlain(
                    id = R.string.yes
                )
            }
        }
    )
}