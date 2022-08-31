package xyz.teamgravity.notepad.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.core.util.Helper
import xyz.teamgravity.notepad.presentation.component.button.NoteFloatingActionButton
import xyz.teamgravity.notepad.presentation.component.card.CardNote
import xyz.teamgravity.notepad.presentation.component.dialog.NoteAlertDialog
import xyz.teamgravity.notepad.presentation.component.text.TextPlain
import xyz.teamgravity.notepad.presentation.component.topbar.TopBar
import xyz.teamgravity.notepad.presentation.component.topbar.TopBarMoreMenuNoteList
import xyz.teamgravity.notepad.presentation.navigation.MainNavGraph
import xyz.teamgravity.notepad.presentation.screen.destinations.*
import xyz.teamgravity.notepad.presentation.viewmodel.NoteListViewModel

@MainNavGraph(start = true)
@Destination
@Composable
fun NoteListScreen(
    navigator: DestinationsNavigator,
    viewmodel: NoteListViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(
                title = { TextPlain(id = R.string.app_name) },
                actions = {
                    TopBarMoreMenuNoteList(
                        expanded = viewmodel.expanded,
                        onExpand = viewmodel::onMenuExpanded,
                        onDismiss = viewmodel::onMenuCollapsed,
                        onDeleteAll = viewmodel::onDeleteAllDialogShow,
                        onSettings = {
                            navigator.navigate(SettingsScreenDestination)
                            viewmodel.onMenuCollapsed()
                        },
                        onSupport = {
                            navigator.navigate(SupportScreenDestination)
                            viewmodel.onMenuCollapsed()
                        },
                        onShare = {
                            Helper.shareApp(context)
                            viewmodel.onMenuCollapsed()
                        },
                        onRate = {
                            Helper.rateApp(context)
                            viewmodel.onMenuCollapsed()
                        },
                        onSourceCode = {
                            Helper.viewSourceCode(context)
                            viewmodel.onMenuCollapsed()
                        },
                        onTodo = {
                            Helper.viewToDoPage(context)
                            viewmodel.onMenuCollapsed()
                        },
                        onAbout = {
                            navigator.navigate(AboutScreenDestination)
                            viewmodel.onMenuCollapsed()
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            NoteFloatingActionButton(
                onClick = { navigator.navigate(NoteAddScreenDestination) },
                icon = Icons.Default.Add,
                contentDescription = R.string.cd_add_note
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            contentPadding = padding,
            columns = GridCells.Adaptive(150.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(viewmodel.notes) { note ->
                CardNote(
                    note = note,
                    onClick = { navigator.navigate(NoteEditScreenDestination(note = it)) }
                )
            }
        }
        if (viewmodel.deleteAllDialog) {
            NoteAlertDialog(
                title = R.string.confirm_deletion,
                message = R.string.wanna_delete_all,
                onDismiss = viewmodel::onDeleteAllDialogDismiss,
                onConfirm = viewmodel::onDeleteAll
            )
        }
    }
}