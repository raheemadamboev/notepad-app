package xyz.teamgravity.notepad.data.model

import java.time.LocalDateTime

data class NoteModel(
    val id: Long? = null,
    val title: String,
    val body: String,
    val created: LocalDateTime = LocalDateTime.now(),
    val edited: LocalDateTime = LocalDateTime.now(),
    val deleted: LocalDateTime? = null
)