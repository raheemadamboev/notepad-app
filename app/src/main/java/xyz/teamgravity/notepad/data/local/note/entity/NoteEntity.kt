package xyz.teamgravity.notepad.data.local.note.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import xyz.teamgravity.notepad.data.local.note.constant.NoteDatabaseConst

@Entity(NoteDatabaseConst.TABLE_NOTE)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("_id") val id: Long?,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("body") val body: String,
    @ColumnInfo("createdTime") val createdTime: Long,
    @ColumnInfo("editedTime") val editedTime: Long
)