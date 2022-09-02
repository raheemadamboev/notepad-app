package xyz.teamgravity.notepad.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class NoteModel(
    val id: Long? = null,
    val title: String,
    val body: String,
    val created: Date = Date(),
    val edited: Date = Date(),
) : Parcelable
