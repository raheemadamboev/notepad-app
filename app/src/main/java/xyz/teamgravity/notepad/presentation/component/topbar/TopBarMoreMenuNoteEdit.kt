package xyz.teamgravity.notepad.presentation.component.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.presentation.component.button.IconButtonPlain
import xyz.teamgravity.notepad.presentation.component.image.IconPlain
import xyz.teamgravity.notepad.presentation.component.text.TextPlain

@Composable
fun TopBarMoreMenuNoteEdit(
    expanded: Boolean,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit
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
        DropdownMenuItem(
            text = {
                TextPlain(
                    id = R.string.delete
                )
            },
            onClick = onDelete,
            leadingIcon = {
                IconPlain(
                    icon = R.drawable.ic_delete,
                    contentDescription = R.string.cd_delete_button
                )
            }
        )
        DropdownMenuItem(
            text = {
                TextPlain(
                    id = R.string.share
                )
            },
            onClick = onShare,
            leadingIcon = {
                IconPlain(
                    icon = R.drawable.ic_share,
                    contentDescription = R.string.share
                )
            }
        )
    }
}