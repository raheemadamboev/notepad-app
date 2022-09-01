package xyz.teamgravity.notepad.presentation.component.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import xyz.teamgravity.notepad.R

@Composable
fun NotepadTextField(
    title: String,
    onTitleChange: (title: String) -> Unit,
    body: String,
    onBodyChange: (body: String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NoteTextField(
            value = title,
            onValueChange = onTitleChange,
            placeholder = R.string.title
        )
        NoteTextField(
            value = body,
            onValueChange = onBodyChange,
            placeholder = R.string.note,
            modifier = Modifier.fillMaxSize()
        )
    }
}