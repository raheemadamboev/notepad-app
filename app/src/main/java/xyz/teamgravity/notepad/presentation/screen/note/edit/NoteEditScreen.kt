package xyz.teamgravity.notepad.presentation.screen.note.edit

import android.os.Parcelable
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.parcelize.Parcelize
import xyz.teamgravity.coresdkcompose.observe.ObserveEvent
import xyz.teamgravity.coresdkcompose.text.TextPlain
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.core.util.Helper
import xyz.teamgravity.notepad.presentation.component.button.IconButtonPlain
import xyz.teamgravity.notepad.presentation.component.button.NoteFloatingActionButton
import xyz.teamgravity.notepad.presentation.component.dialog.NoteAlertDialog
import xyz.teamgravity.notepad.presentation.component.textfield.NotepadTextField
import xyz.teamgravity.notepad.presentation.component.topbar.TopBar
import xyz.teamgravity.notepad.presentation.component.topbar.TopBarMoreMenuNoteEdit
import xyz.teamgravity.notepad.presentation.navigation.MainNavGraph

@Destination<MainNavGraph>(navArgs = NoteEditScreenArgs::class)
@Composable
fun NoteEditScreen(
    navigator: ResultBackNavigator<NoteEditResult>,
    viewmodel: NoteEditViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    ObserveEvent(
        flow = viewmodel.event,
        onEvent = { event ->
            when (event) {
                NoteEditViewModel.NoteEditEvent.NavigateBack -> {
                    navigator.navigateBack()
                }

                is NoteEditViewModel.NoteEditEvent.NoteDeleted -> {
                    navigator.navigateBack(NoteEditResult.NoteDeleted(event.id))
                }
            }
        }
    )

    LifecycleEventEffect(
        event = Lifecycle.Event.ON_PAUSE,
        onEvent = viewmodel::onAutoSave
    )

    BackHandler(
        enabled = viewmodel.autoSave,
        onBack = viewmodel::onHandleBack
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
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            if (!viewmodel.autoSave) {
                NoteFloatingActionButton(
                    onClick = viewmodel::onUpdateNote,
                    icon = Icons.Rounded.Done,
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

@Parcelize
sealed interface NoteEditResult : Parcelable {

    @Parcelize
    data class NoteDeleted(val id: Long) : NoteEditResult
}