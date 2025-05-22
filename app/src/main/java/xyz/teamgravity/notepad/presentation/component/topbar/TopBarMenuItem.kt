package xyz.teamgravity.notepad.presentation.component.topbar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import xyz.teamgravity.notepad.presentation.component.image.IconPlain
import xyz.teamgravity.notepad.presentation.component.text.TextPlain

@Composable
fun TopBarMenuItem(
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    @StringRes label: Int,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true
) {
    DropdownMenuItem(
        text = {
            TextPlain(
                id = label
            )
        },
        onClick = {
            onDismiss()
            onClick()
        },
        leadingIcon = {
            IconPlain(
                icon = icon,
                contentDescription = label
            )
        },
        trailingIcon = trailingIcon,
        enabled = enabled
    )
}