package xyz.teamgravity.notepad.data.local.note.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import xyz.teamgravity.notepad.data.local.note.constant.NoteDatabaseConst.TABLE_NOTE
import xyz.teamgravity.notepad.data.local.note.entity.NoteEntity

@Dao
interface NoteDao {

    ///////////////////////////////////////////////////////////////////////////
    // Insert
    ///////////////////////////////////////////////////////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    ///////////////////////////////////////////////////////////////////////////
    // Update
    ///////////////////////////////////////////////////////////////////////////

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: NoteEntity)

    ///////////////////////////////////////////////////////////////////////////
    // Delete
    ///////////////////////////////////////////////////////////////////////////

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("DELETE FROM $TABLE_NOTE")
    suspend fun deleteAllNotes()

    @Query("DELETE FROM $TABLE_NOTE WHERE :expiredTime >= deletedTime")
    suspend fun deleteExpiredNotes(expiredTime: Long)

    ///////////////////////////////////////////////////////////////////////////
    // Get
    ///////////////////////////////////////////////////////////////////////////

    @Query("SELECT * FROM $TABLE_NOTE WHERE `deletedTime` IS NULL ORDER BY `editedTime` DESC")
    fun getValidNotes(): PagingSource<Int, NoteEntity>

    @Query("SELECT * FROM $TABLE_NOTE WHERE `deletedTime` IS NOT NULL ORDER BY `deletedTime` DESC")
    fun getDeletedNotes(): PagingSource<Int, NoteEntity>

    @Query("SELECT * FROM $TABLE_NOTE WHERE :id = `_id` LIMIT 1")
    fun getNote(id: Long): Flow<NoteEntity?>

    ///////////////////////////////////////////////////////////////////////////
    // Misc
    ///////////////////////////////////////////////////////////////////////////

    @Query("UPDATE $TABLE_NOTE SET `deletedTime` = :deletedTime WHERE deletedTime IS NULL")
    suspend fun moveValidNotesToTrash(deletedTime: Long)

    @Query("UPDATE $TABLE_NOTE SET `deletedTime` = NULL WHERE deletedTime IS NOT NULL")
    suspend fun restoreDeletedNotes()
}