package xyz.teamgravity.notepad.presentation.screen.note.add

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.presentation.component.button.IconButtonPlain
import xyz.teamgravity.notepad.presentation.component.button.NoteFloatingActionButton
import xyz.teamgravity.notepad.presentation.component.text.TextPlain
import xyz.teamgravity.notepad.presentation.component.textfield.NotepadTextField
import xyz.teamgravity.notepad.presentation.component.topbar.TopBar
import xyz.teamgravity.notepad.presentation.navigation.MainNavGraph

@Destination<MainNavGraph>
@Composable
fun NoteAddScreen(
    navigator: DestinationsNavigator,
    viewmodel: NoteAddViewModel = hiltViewModel(),
) {

    LaunchedEffect(key1 = viewmodel.event) {
        viewmodel.event.collectLatest { event ->
            when (event) {
                NoteAddViewModel.NoteAddEvent.NoteAdded -> {
                    navigator.popBackStack()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = { TextPlain(id = R.string.app_name) },
                navigationIcon = {
                    IconButtonPlain(
                        onClick = navigator::popBackStack,
                        icon = Icons.Default.ArrowBackIos,
                        contentDescription = R.string.cd_back_button
                    )
                }
            )
        },
        floatingActionButton = {
            if (!viewmodel.autoSave) {
                NoteFloatingActionButton(
                    onClick = viewmodel::onSaveNote,
                    icon = Icons.Default.Done,
                    contentDescription = R.string.cd_add_note
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            NotepadTextField(
                title = viewmodel.title,
                onTitleChange = viewmodel::onTitleChange,
                body = viewmodel.body,
                onBodyChange = viewmodel::onBodyChange
            )
        }
    }
}