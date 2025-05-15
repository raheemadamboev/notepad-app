package xyz.teamgravity.notepad.presentation.component.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.presentation.component.button.IconButtonPlain
import xyz.teamgravity.notepad.presentation.component.image.IconPlain
import xyz.teamgravity.notepad.presentation.component.text.TextPlain

@Composable
fun TopBarMoreMenuNoteList(
    expanded: Boolean,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    autoSave: Boolean,
    onAutoSave: () -> Unit,
    onPinLock: () -> Unit,
    onDeleteAll: () -> Unit,
    onSupport: () -> Unit,
    onShare: () -> Unit,
    onRate: () -> Unit,
    onSourceCode: () -> Unit,
    onAbout: () -> Unit
) {
    IconButtonPlain(
        onClick = onExpand,
        icon = Icons.Default.MoreVert,
        contentDescription = R.string.cd_more_vertical
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = {
                TextPlain(
                    id = R.string.auto_save
                )
            },
            onClick = onAutoSave,
            trailingIcon = {
                if (autoSave) {
                    IconPlain(
                        icon = Icons.Default.Done,
                        contentDescription = R.string.auto_save
                    )
                }
            },
            leadingIcon = {
                IconPlain(
                    icon = R.drawable.ic_save,
                    contentDescription = R.string.auto_save
                )
            }
        )
        DropdownMenuItem(
            text = {
                TextPlain(
                    id = R.string.pin_lock
                )
            },
            onClick = onPinLock,
            leadingIcon = {
                IconPlain(
                    icon = R.drawable.ic_lock,
                    contentDescription = R.string.pin_lock
                )
            }
        )
        DropdownMenuItem(
            text = {
                TextPlain(
                    id = R.string.delete_all_notes
                )
            },
            onClick = onDeleteAll,
            leadingIcon = {
                IconPlain(
                    icon = R.drawable.ic_delete,
                    contentDescription = R.string.delete_all_notes
                )
            }
        )
        DropdownMenuItem(
            text = {
                TextPlain(
                    id = R.string.support
                )
            },
            onClick = onSupport,
            leadingIcon = {
                IconPlain(
                    icon = R.drawable.ic_customer_service,
                    contentDescription = R.string.support
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
        DropdownMenuItem(
            text = {
                TextPlain(
                    id = R.string.rate
                )
            },
            onClick = onRate,
            leadingIcon = {
                IconPlain(
                    icon = R.drawable.ic_star,
                    contentDescription = R.string.rate
                )
            }
        )
        DropdownMenuItem(
            text = {
                TextPlain(
                    id = R.string.source_code
                )
            },
            onClick = onSourceCode,
            leadingIcon = {
                IconPlain(
                    icon = R.drawable.ic_github,
                    contentDescription = R.string.source_code
                )
            }
        )
        DropdownMenuItem(
            text = {
                TextPlain(
                    id = R.string.about_me
                )
            },
            onClick = onAbout,
            leadingIcon = {
                IconPlain(
                    icon = R.drawable.ic_info,
                    contentDescription = R.string.about_me
                )
            }
        )
    }
}