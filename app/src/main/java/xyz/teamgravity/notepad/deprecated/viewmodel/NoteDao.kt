package xyz.teamgravity.notepad.deprecated.viewmodel

import androidx.lifecycle.LiveData
import androidx.room.*
import xyz.teamgravity.notepad.deprecated.model.NoteModel

@Dao
interface NoteDao {

    @Insert
    fun insert(note: NoteModel)

    @Update
    fun update(note: NoteModel)

    @Delete
    fun delete(note: NoteModel)

    @Delete
    fun deleteSelected(notes: List<NoteModel>)

    @Query("DELETE FROM note_table")
    fun deleteAll()

    @Query("SELECT * FROM note_table ORDER BY editedTime DESC")
    fun getAllNotes(): LiveData<List<NoteModel>>
}