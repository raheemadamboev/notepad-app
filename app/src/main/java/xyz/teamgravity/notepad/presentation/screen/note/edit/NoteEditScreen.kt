package xyz.teamgravity.notepad.presentation.screen.note.edit

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import xyz.teamgravity.coresdkcompose.observe.ObserveEvent
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.core.util.Helper
import xyz.teamgravity.notepad.presentation.component.button.IconButtonPlain
import xyz.teamgravity.notepad.presentation.component.button.NoteFloatingActionButton
import xyz.teamgravity.notepad.presentation.component.dialog.NoteAlertDialog
import xyz.teamgravity.notepad.presentation.component.text.TextPlain
import xyz.teamgravity.notepad.presentation.component.textfield.NotepadTextField
import xyz.teamgravity.notepad.presentation.component.topbar.TopBar
import xyz.teamgravity.notepad.presentation.component.topbar.TopBarMoreMenuNoteEdit
import xyz.teamgravity.notepad.presentation.navigation.MainNavGraph

@Destination<MainNavGraph>(navArgs = NoteEditScreenArgs::class)
@Composable
fun NoteEditScreen(
    navigator: DestinationsNavigator,
    viewmodel: NoteEditViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    ObserveEvent(
        flow = viewmodel.event,
        onEvent = { event ->
            when (event) {
                NoteEditViewModel.NoteEditEvent.NoteUpdated -> {
                    navigator.popBackStack()
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
                        onClick = navigator::navigateUp,
                        icon = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = R.string.cd_back_button
                    )
                },
                actions = {
                    TopBarMoreMenuNoteEdit(
                        expanded = viewmodel.menuExpanded,
                        onExpand = viewmodel::onMenuExpand,
                        onDismiss = viewmodel::onMenuCollapse,
                        onDelete = viewmodel::onDeleteShow,
                        onShare = {
                            Helper.shareNote(
                                context = context,
                                note = viewmodel.sharedNote
                            )
                            viewmodel.onMenuCollapse()
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            if (!viewmodel.autoSave) {
                NoteFloatingActionButton(
                    onClick = viewmodel::onUpdateNote,
                    icon = Icons.Default.Done,
                    contentDescription = R.string.cd_done_button
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->
        NotepadTextField(
            title = viewmodel.title,
            onTitleChange = viewmodel::onTitleChange,
            body = viewmodel.body,
            onBodyChange = viewmodel::onBodyChange,
            modifier = Modifier.padding(padding)
        )
        if (viewmodel.deleteShown) {
            NoteAlertDialog(
                title = R.string.confirm_deletion,
                message = R.string.wanna_delete_note,
                onDismiss = viewmodel::onDeleteDismiss,
                onConfirm = viewmodel::onDeleteNote
            )
        }
    }
}

data class NoteEditScreenArgs(
    val id: Long
)