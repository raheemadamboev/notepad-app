package xyz.teamgravity.notepad.presentation.component.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import xyz.teamgravity.coresdkandroid.android.BuildUtil
import xyz.teamgravity.coresdkcompose.button.IconButtonPlain
import xyz.teamgravity.coresdkcompose.image.IconPlain
import xyz.teamgravity.coresdkcompose.menu.GDropdownMenuItem
import xyz.teamgravity.notepad.R

@Composable
fun TopBarMoreMenuNoteList(
    expanded: Boolean,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    autoSave: Boolean,
    onAutoSave: () -> Unit,
    deleteAllEnabled: Boolean,
    onDeleteAll: () -> Unit,
    onPinLock: () -> Unit,
    onLanguage: () -> Unit
) {
    IconButtonPlain(
        onClick = onExpand,
        icon = Icons.Rounded.MoreVert,
        contentDescription = R.string.cd_more_vertical
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        GDropdownMenuItem(
            onDismiss = onDismiss,
            onClick = onAutoSave,
            icon = R.drawable.ic_save,
            label = R.string.auto_save,
            trailingIcon = {
                if (autoSave) {
                    IconPlain(
                        icon = Icons.Rounded.Done,
                        contentDescription = R.string.auto_save
                    )
                }
            }
        )
        GDropdownMenuItem(
            onDismiss = onDismiss,
            onClick = onDeleteAll,
            icon = R.drawable.ic_delete,
            label = R.string.delete_all_notes,
            enabled = deleteAllEnabled
        )
        GDropdownMenuItem(
            onDismiss = onDismiss,
            onClick = onPinLock,
            icon = R.drawable.ic_lock,
            label = R.string.pin_lock
        )
        if (BuildUtil.atLeastTiramisu()) {
            GDropdownMenuItem(
                onDismiss = onDismiss,
                onClick = onLanguage,
                icon = R.drawable.ic_language,
                label = R.string.change_language
            )
        }
    }
}