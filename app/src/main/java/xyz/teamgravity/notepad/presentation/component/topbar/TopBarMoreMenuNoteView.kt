package xyz.teamgravity.notepad.presentation.component.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.presentation.component.button.IconButtonPlain

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
        TopBarMenuItem(
            onDismiss = onDismiss,
            onClick = onDelete,
            icon = R.drawable.ic_delete,
            label = R.string.delete,
            enabled = enabled
        )
        TopBarMenuItem(
            onDismiss = onDismiss,
            onClick = onRestore,
            icon = R.drawable.ic_reset,
            label = R.string.restore,
            enabled = enabled
        )
    }
}