package xyz.teamgravity.notepad.deprecated.viewmodel

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import xyz.teamgravity.notepad.deprecated.model.NoteModel

@Database(version = 1, entities = [NoteModel::class], exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private var INSTANCE: MyDatabase? = null

        fun getMyDatabase(context: Context): MyDatabase? {
            if (INSTANCE == null) {
                synchronized(MyDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "notepad_database"
                    ).addMigrations().build()
                }
            }

            return INSTANCE
        }
    }
}