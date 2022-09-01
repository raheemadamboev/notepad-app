package xyz.teamgravity.notepad.presentation.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import xyz.teamgravity.notepad.presentation.component.misc.StaggeredVerticalGrid
import xyz.teamgravity.notepad.presentation.component.text.TextPlain
import xyz.teamgravity.notepad.presentation.component.topbar.TopBar
import xyz.teamgravity.notepad.presentation.component.topbar.TopBarMoreMenuNoteList
import xyz.teamgravity.notepad.presentation.navigation.MainNavGraph
import xyz.teamgravity.notepad.presentation.screen.destinations.AboutScreenDestination
import xyz.teamgravity.notepad.presentation.screen.destinations.NoteAddScreenDestination
import xyz.teamgravity.notepad.presentation.screen.destinations.NoteEditScreenDestination
import xyz.teamgravity.notepad.presentation.screen.destinations.SupportScreenDestination
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
                        expanded = viewmodel.menuExpanded,
                        onExpand = viewmodel::onMenuExpand,
                        onDismiss = viewmodel::onMenuCollapse,
                        autoSave = viewmodel.autoSave,
                        onAutoSave = viewmodel::onAutoSaveChange,
                        onDeleteAll = viewmodel::onDeleteAllDialogShow,
                        onSupport = {
                            navigator.navigate(SupportScreenDestination)
                            viewmodel.onMenuCollapse()
                        },
                        onShare = {
                            Helper.shareApp(context)
                            viewmodel.onMenuCollapse()
                        },
                        onRate = {
                            Helper.rateApp(context)
                            viewmodel.onMenuCollapse()
                        },
                        onSourceCode = {
                            Helper.viewSourceCode(context)
                            viewmodel.onMenuCollapse()
                        },
                        onTodo = {
                            Helper.viewToDoPage(context)
                            viewmodel.onMenuCollapse()
                        },
                        onAbout = {
                            navigator.navigate(AboutScreenDestination)
                            viewmodel.onMenuCollapse()
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
        LazyColumn(contentPadding = padding) {
            item {
                StaggeredVerticalGrid(maxColumnWidth = 200.dp) {
                    viewmodel.notes.forEach { note ->
                        CardNote(
                            note = note,
                            onClick = { navigator.navigate(NoteEditScreenDestination(note = it)) }
                        )
                    }
                }
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