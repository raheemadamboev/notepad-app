package xyz.teamgravity.notepad.presentation.component.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import xyz.teamgravity.coresdkandroid.android.BuildUtil
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
                        icon = Icons.Rounded.Done,
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
        if (BuildUtil.atLeastTiramisu()) {
            DropdownMenuItem(
                text = {
                    TextPlain(
                        id = R.string.change_language
                    )
                },
                onClick = onLanguage,
                leadingIcon = {
                    IconPlain(
                        icon = R.drawable.ic_language,
                        contentDescription = R.string.change_language
                    )
                }
            )
        }
    }
}