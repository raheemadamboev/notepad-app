package xyz.teamgravity.notepad.data.local.note.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import xyz.teamgravity.notepad.data.local.note.constant.NoteDatabaseConst.TABLE_NOTE

object NoteMigration {

    /**
     * Adds `deleteTimed` INTEGER column to the [TABLE_NOTE].
     */
    val MIGRATION_1_2: Migration = object : Migration(
        startVersion = 1,
        endVersion = 2
    ) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE $TABLE_NOTE ADD COLUMN `deletedTime` INTEGER")
        }
    }
}