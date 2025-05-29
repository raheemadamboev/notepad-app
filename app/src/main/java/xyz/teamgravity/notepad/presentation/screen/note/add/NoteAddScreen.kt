package xyz.teamgravity.notepad.presentation.screen.note.add

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import xyz.teamgravity.coresdkcompose.observe.ObserveEvent
import xyz.teamgravity.coresdkcompose.text.TextPlain
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.presentation.component.button.IconButtonPlain
import xyz.teamgravity.notepad.presentation.component.button.NoteFloatingActionButton
import xyz.teamgravity.notepad.presentation.component.textfield.NotepadTextField
import xyz.teamgravity.notepad.presentation.component.topbar.TopBar
import xyz.teamgravity.notepad.presentation.navigation.MainNavGraph

@Destination<MainNavGraph>
@Composable
fun NoteAddScreen(
    navigator: DestinationsNavigator,
    viewmodel: NoteAddViewModel = hiltViewModel()
) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    ObserveEvent(
        flow = viewmodel.event,
        onEvent = { event ->
            when (event) {
                NoteAddViewModel.NoteAddEvent.NavigateBack -> {
                    navigator.popBackStack()
                }
            }
        }
    )

    LifecycleEventEffect(
        event = Lifecycle.Event.ON_PAUSE,
        onEvent = viewmodel::onAutoSave
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
                            dispatcher?.onBackPressed() ?: navigator.navigateUp()
                        },
                        icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
                        contentDescription = R.string.cd_back_button
                    )
                }
            )
        },
        floatingActionButton = {
            if (!viewmodel.autoSave) {
                NoteFloatingActionButton(
                    onClick = viewmodel::onSaveNote,
                    icon = Icons.Rounded.Done,
                    contentDescription = R.string.cd_add_note
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
    }
}