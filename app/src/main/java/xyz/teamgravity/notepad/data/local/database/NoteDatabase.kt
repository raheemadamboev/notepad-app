package xyz.teamgravity.notepad.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.teamgravity.notepad.data.local.constant.NoteDatabaseConst
import xyz.teamgravity.notepad.data.local.dao.NoteDao
import xyz.teamgravity.notepad.data.local.entity.NoteEntity

@Database(
    version = NoteDatabaseConst.VERSION,
    entities = [NoteEntity::class]
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}