package xyz.teamgravity.notepad.presentation.component.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import xyz.teamgravity.coresdkcompose.menu.GDropdownMenuItem
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.presentation.component.button.IconButtonPlain

@Composable
fun TopBarMoreMenuNoteTrash(
    expanded: Boolean,
    enabled: Boolean,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    onDeleteAll: () -> Unit,
    onRestoreAll: () -> Unit
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
            onClick = onDeleteAll,
            icon = R.drawable.ic_delete,
            label = R.string.delete_all,
            enabled = enabled
        )
        GDropdownMenuItem(
            onDismiss = onDismiss,
            onClick = onRestoreAll,
            icon = R.drawable.ic_reset,
            label = R.string.restore_all,
            enabled = enabled
        )
    }
}