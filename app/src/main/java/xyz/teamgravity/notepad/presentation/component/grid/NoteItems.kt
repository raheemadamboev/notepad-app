package xyz.teamgravity.notepad.presentation.component.grid

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import xyz.teamgravity.notepad.data.model.NoteModel
import xyz.teamgravity.notepad.presentation.component.card.CardNote

fun LazyStaggeredGridScope.noteItems(
    notes: LazyPagingItems<NoteModel>,
    onClick: (note: NoteModel) -> Unit
) {
    items(
        count = notes.itemCount,
        key = notes.itemKey(
            key = { note ->
                note.id!!
            }
        ),
        contentType = notes.itemContentType()
    ) { index ->
        val note = notes[index]
        if (note != null) {
            CardNote(
                note = note,
                onClick = onClick
            )
        }
    }
}