package xyz.teamgravity.notepad.data.model

import java.util.*

data class NoteModel(
    val id: Long = 0,
    val title: String,
    val body: String = "",
    val created: Date = Date(),
    val edited: Date = Date(),
)
