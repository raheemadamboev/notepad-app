package xyz.teamgravity.notepad.data.local.note.database

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.teamgravity.notepad.data.local.note.constant.NoteDatabaseConst
import xyz.teamgravity.notepad.data.local.note.dao.NoteDao
import xyz.teamgravity.notepad.data.local.note.entity.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = NoteDatabaseConst.VERSION
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}