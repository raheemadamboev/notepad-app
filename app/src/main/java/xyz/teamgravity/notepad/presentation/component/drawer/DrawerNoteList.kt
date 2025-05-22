package xyz.teamgravity.notepad.presentation.component.drawer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.teamgravity.coresdkandroid.android.BuildUtil
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.presentation.component.image.IconPlain
import xyz.teamgravity.notepad.presentation.component.text.TextPlain

@Composable
fun DrawerNoteList(
    drawer: DrawerState,
    scope: CoroutineScope,
    scroll: ScrollState = rememberScrollState(),
    onPinLock: () -> Unit,
    onTrash: () -> Unit,
    onLanguage: () -> Unit,
    onSupport: () -> Unit,
    onShare: () -> Unit,
    onRate: () -> Unit,
    onSourceCode: () -> Unit,
    onAbout: () -> Unit
) {
    ModalDrawerSheet(
        drawerContentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scroll)
                .padding(horizontal = 8.dp)
        ) {
            Spacer(
                modifier = Modifier.height(6.dp)
            )
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(12.dp)
            )
            DrawerItem(
                icon = R.drawable.ic_lock,
                label = R.string.pin_lock,
                onClick = onPinLock,
                drawer = drawer,
                scope = scope
            )
            DrawerItem(
                icon = R.drawable.ic_delete,
                label = R.string.recently_deleted_notes,
                onClick = onTrash,
                drawer = drawer,
                scope = scope
            )
            if (BuildUtil.atLeastTiramisu()) {
                DrawerItem(
                    icon = R.drawable.ic_language,
                    label = R.string.change_language,
                    onClick = onLanguage,
                    drawer = drawer,
                    scope = scope
                )
            }
            DrawerItem(
                icon = R.drawable.ic_customer_service,
                label = R.string.support,
                onClick = onSupport,
                drawer = drawer,
                scope = scope
            )
            DrawerItem(
                icon = R.drawable.ic_share,
                label = R.string.share,
                onClick = onShare,
                drawer = drawer,
                scope = scope
            )
            DrawerItem(
                icon = R.drawable.ic_star,
                label = R.string.rate,
                onClick = onRate,
                drawer = drawer,
                scope = scope
            )
            DrawerItem(
                icon = R.drawable.ic_github,
                label = R.string.source_code,
                onClick = onSourceCode,
                drawer = drawer,
                scope = scope
            )
            DrawerItem(
                icon = R.drawable.ic_info,
                label = R.string.about_me,
                onClick = onAbout,
                drawer = drawer,
                scope = scope
            )
        }
    }
}

@Composable
private fun DrawerItem(
    @DrawableRes icon: Int,
    @StringRes label: Int,
    onClick: () -> Unit,
    drawer: DrawerState,
    scope: CoroutineScope
) {
    NavigationDrawerItem(
        selected = false,
        colors = NavigationDrawerItemDefaults.colors(
            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
            unselectedTextColor = MaterialTheme.colorScheme.onBackground
        ),
        icon = {
            IconPlain(
                icon = icon,
                contentDescription = label
            )
        },
        label = {
            TextPlain(
                id = label
            )
        },
        onClick = {
            scope.launch {
                drawer.close()
                onClick()
            }
        }
    )
}