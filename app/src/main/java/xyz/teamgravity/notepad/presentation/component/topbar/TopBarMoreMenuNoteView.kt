package xyz.teamgravity.notepad.presentation.component.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import xyz.teamgravity.coresdkcompose.button.IconButtonPlain
import xyz.teamgravity.coresdkcompose.menu.GDropdownMenuItem
import xyz.teamgravity.notepad.R

@Composable
fun TopBarMoreMenuNoteView(
    expanded: Boolean,
    enabled: Boolean,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onRestore: () -> Unit
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
            onClick = onDelete,
            icon = R.drawable.ic_delete,
            label = R.string.delete,
            enabled = enabled
        )
        GDropdownMenuItem(
            onDismiss = onDismiss,
            onClick = onRestore,
            icon = R.drawable.ic_reset,
            label = R.string.restore,
            enabled = enabled
        )
    }
}