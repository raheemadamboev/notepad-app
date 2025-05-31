package xyz.teamgravity.notepad.presentation.screen.note.trash

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.NoteViewScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import com.ramcosta.composedestinations.result.onResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.teamgravity.coresdkcompose.button.IconButtonPlain
import xyz.teamgravity.coresdkcompose.observe.ObserveEvent
import xyz.teamgravity.coresdkcompose.paging.shouldShowEmptyState
import xyz.teamgravity.coresdkcompose.text.TextImageInfo
import xyz.teamgravity.coresdkcompose.text.TextPlain
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.presentation.component.dialog.NoteAlertDialog
import xyz.teamgravity.notepad.presentation.component.grid.NoteGrid
import xyz.teamgravity.notepad.presentation.component.grid.noteItems
import xyz.teamgravity.notepad.presentation.component.topbar.TopBar
import xyz.teamgravity.notepad.presentation.component.topbar.TopBarMoreMenuNoteTrash
import xyz.teamgravity.notepad.presentation.navigation.MainNavGraph
import xyz.teamgravity.notepad.presentation.screen.note.view.NoteViewResult

@Destination<MainNavGraph>
@Composable
fun NoteTrashScreen(
    scope: CoroutineScope = rememberCoroutineScope(),
    snackbar: SnackbarHostState = remember { SnackbarHostState() },
    noteViewRecipient: ResultRecipient<NoteViewScreenDestination, NoteViewResult>,
    navigator: DestinationsNavigator,
    viewmodel: NoteTrashViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val notes = viewmodel.notes.collectAsLazyPagingItems()
    val shouldShowEmptyState by notes.shouldShowEmptyState()

    noteViewRecipient.onResult(
        onValue = { result ->
            val id = when (result) {
                NoteViewResult.Deleted -> R.string.note_deleted_permanently
                NoteViewResult.Restored -> R.string.note_restored
            }
            scope.launch {
                snackbar.showSnackbar(
                    message = context.getString(id),
                    duration = SnackbarDuration.Short
                )
            }
        }
    )

    ObserveEvent(
        flow = viewmodel.event,
        onEvent = { event ->
            when (event) {
                is NoteTrashViewModel.NoteTrashEvent.ShowMessage -> {
                    snackbar.showSnackbar(
                        message = context.getString(event.message),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    )

    Scaffold(
        topBar = {
            TopBar(
                title = {
                    TextPlain(
                        id = R.string.trash
                    )
                },
                navigationIcon = {
                    IconButtonPlain(
                        onClick = {
                            dispatcher?.onBackPressed() ?: navigator.navigateUp()
                        },
                        icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
                        contentDescription = R.string.cd_back_button
                    )
                },
                actions = {
                    TopBarMoreMenuNoteTrash(
                        expanded = viewmodel.menuExpanded,
                        enabled = notes.itemSnapshotList.isNotEmpty(),
                        onExpand = viewmodel::onMenuExpand,
                        onDismiss = viewmodel::onMenuCollapse,
                        onDeleteAll = viewmodel::onDeleteAllShow,
                        onRestoreAll = viewmodel::onRestoreAllShow
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbar
            ) { data ->
                Snackbar(
                    snackbarData = data
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->
        NoteGrid(
            contentPadding = padding
        ) {
            item(
                span = StaggeredGridItemSpan.FullLine
            ) {
                Text(
                    text = stringResource(R.string.trash_info),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
            noteItems(
                notes = notes,
                onClick = { note ->
                    navigator.navigate(NoteViewScreenDestination(id = note.id!!))
                }
            )
        }
        if (shouldShowEmptyState) {
            TextImageInfo(
                icon = R.drawable.ic_delete,
                message = R.string.no_notes_in_trash
            )
        }
        if (viewmodel.deleteAllShown) {
            NoteAlertDialog(
                title = R.string.confirm_deletion,
                message = R.string.wanna_delete_all_permanently,
                onDismiss = viewmodel::onDeleteAllDismiss,
                onConfirm = viewmodel::onDeleteAll
            )
        }
        if (viewmodel.restoreAllShown) {
            NoteAlertDialog(
                title = R.string.confirm_restoration,
                message = R.string.wanna_restore_all_notes,
                onDismiss = viewmodel::onRestoreAllDismiss,
                onConfirm = viewmodel::onRestoreAll
            )
        }
    }
}