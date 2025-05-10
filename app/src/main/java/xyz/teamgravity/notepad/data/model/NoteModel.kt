package xyz.teamgravity.notepad.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class NoteModel(
    val id: Long? = null,
    val title: String,
    val body: String,
    val created: LocalDateTime = LocalDateTime.now(),
    val edited: LocalDateTime = LocalDateTime.now(),
) : Parcelable
