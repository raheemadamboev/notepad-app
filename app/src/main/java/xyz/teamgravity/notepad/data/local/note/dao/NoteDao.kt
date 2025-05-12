package xyz.teamgravity.notepad.data.local.note.dao

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
    // INSERT
    ///////////////////////////////////////////////////////////////////////////

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    ///////////////////////////////////////////////////////////////////////////
    // UPDATE
    ///////////////////////////////////////////////////////////////////////////

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: NoteEntity)

    ///////////////////////////////////////////////////////////////////////////
    // DELETE
    ///////////////////////////////////////////////////////////////////////////

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("DELETE FROM $TABLE_NOTE")
    suspend fun deleteAllNotes()

    ///////////////////////////////////////////////////////////////////////////
    // GET
    ///////////////////////////////////////////////////////////////////////////

    @Query("SELECT * FROM $TABLE_NOTE ORDER BY editedTime DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM $TABLE_NOTE WHERE :id = _id LIMIT 1")
    fun getNote(id: Long): Flow<NoteEntity?>
}