package xyz.teamgravity.notepad.presentation.component.image

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun IconPlain(
    @DrawableRes icon: Int,
    @StringRes contentDescription: Int
) {
    Icon(
        painter = painterResource(id = icon),
        contentDescription = stringResource(id = contentDescription),
        modifier = Modifier.size(20.dp)
    )
}

@Composable
fun IconPlain(
    icon: ImageVector,
    @StringRes contentDescription: Int
) {
    Icon(
        imageVector = icon,
        contentDescription = stringResource(id = contentDescription),
        modifier = Modifier.size(20.dp)
    )
}