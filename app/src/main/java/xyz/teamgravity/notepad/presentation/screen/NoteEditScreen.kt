package xyz.teamgravity.notepad.presentation.screen

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import xyz.teamgravity.notepad.data.model.NoteModel
import xyz.teamgravity.notepad.presentation.navigation.MainNavGraph

@MainNavGraph
@Destination(navArgsDelegate = NoteEditScreenNavArgs::class)
@Composable
fun NoteEditScreen() {

}

data class NoteEditScreenNavArgs(
    val note: NoteModel
)