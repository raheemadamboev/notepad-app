package xyz.teamgravity.notepad.deprecated.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "note_table")
data class NoteModel(

    var title: String = "",
    var body: String = "",

    val createdTime: Long,
    var editedTime: Long,

    @PrimaryKey(autoGenerate = true)
    val _id: Long? = null
) : Parcelable