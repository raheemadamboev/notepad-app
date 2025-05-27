package xyz.teamgravity.notepad.presentation.screen.note.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import xyz.teamgravity.coresdkcompose.observe.ObserveEvent
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.presentation.component.button.IconButtonPlain
import xyz.teamgravity.notepad.presentation.component.dialog.NoteAlertDialog
import xyz.teamgravity.notepad.presentation.component.text.TextPlain
import xyz.teamgravity.notepad.presentation.component.topbar.TopBar
import xyz.teamgravity.notepad.presentation.component.topbar.TopBarMoreMenuNoteView
import xyz.teamgravity.notepad.presentation.navigation.MainNavGraph

@Destination<MainNavGraph>(navArgs = NoteViewScreenArgs::class)
@Composable
fun NoteViewScreen(
    scroll: ScrollState = rememberScrollState(),
    navigator: ResultBackNavigator<NoteViewResult>,
    viewmodel: NoteViewViewModel = hiltViewModel()
) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    ObserveEvent(
        flow = viewmodel.event,
        onEvent = { event ->
            when (event) {
                is NoteViewViewModel.NoteViewEvent.NavigateBack -> {
                    navigator.navigateBack(event.result)
                }
            }
        }
    )

    Scaffold(
        topBar = {
            TopBar(
                title = {
                    TextPlain(
                        id = R.string.app_name
                    )
                },
                navigationIcon = {
                    IconButtonPlain(
                        onClick = {
                            dispatcher?.onBackPressed() ?: navigator.navigateBack()
                        },
                        icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
                        contentDescription = R.string.cd_back_button
                    )
                },
                actions = {
                    TopBarMoreMenuNoteView(
                        expanded = viewmodel.menuExpanded,
                        enabled = !viewmodel.loading,
                        onExpand = viewmodel::onMenuExpand,
                        onDismiss = viewmodel::onMenuCollapse,
                        onDelete = viewmodel::onDeleteShow,
                        onRestore = viewmodel::onRestoreShow
                    )
                }
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->
        if (!viewmodel.loading) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scroll)
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(
                    Modifier.height(16.dp)
                )
                Text(
                    text = stringResource(R.string.x_last_modified, viewmodel.editedTime)
                )
                Text(
                    text = stringResource(R.string.x_deleted, viewmodel.deletedTime)
                )
                if (viewmodel.title.isNotBlank()) {
                    HorizontalDivider()
                    Text(
                        text = viewmodel.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (viewmodel.body.isNotBlank()) {
                    HorizontalDivider()
                    Text(
                        text = viewmodel.body,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        if (viewmodel.deleteShown) {
            NoteAlertDialog(
                title = R.string.confirm_deletion,
                message = R.string.wanna_delete_note_permanently,
                onDismiss = viewmodel::onDeleteDismiss,
                onConfirm = viewmodel::onDelete
            )
        }
        if (viewmodel.restoreShown) {
            NoteAlertDialog(
                title = R.string.confirm_restoration,
                message = R.string.wanna_restore_note,
                onDismiss = viewmodel::onRestoreDismiss,
                onConfirm = viewmodel::onRestore
            )
        }
    }
}

data class NoteViewScreenArgs(
    val id: Long
)

enum class NoteViewResult {
    Deleted,
    Restored;
}