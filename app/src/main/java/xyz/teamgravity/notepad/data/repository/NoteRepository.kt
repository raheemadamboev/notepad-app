package xyz.teamgravity.notepad.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import xyz.teamgravity.notepad.data.local.note.dao.NoteDao
import xyz.teamgravity.notepad.data.mapper.toEntity
import xyz.teamgravity.notepad.data.mapper.toModel
import xyz.teamgravity.notepad.data.model.NoteModel

class NoteRepository(
    private val dao: NoteDao
) {

    ///////////////////////////////////////////////////////////////////////////
    // Insert
    ///////////////////////////////////////////////////////////////////////////

    suspend fun insertNote(note: NoteModel): Long {
        return withContext(Dispatchers.IO) {
            return@withContext dao.insertNote(note.toEntity())
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Update
    ///////////////////////////////////////////////////////////////////////////

    suspend fun updateNote(note: NoteModel) {
        withContext(Dispatchers.IO) {
            dao.updateNote(note.toEntity())
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Delete
    ///////////////////////////////////////////////////////////////////////////

    suspend fun deleteNote(note: NoteModel) {
        withContext(Dispatchers.IO) {
            dao.deleteNote(note.toEntity())
        }
    }

    suspend fun deleteAllNotes() {
        withContext(Dispatchers.IO) {
            dao.deleteAllNotes()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Get
    ///////////////////////////////////////////////////////////////////////////

    fun getAllNotes(): Flow<List<NoteModel>> {
        return dao.getAllNotes().map { entities -> entities.map { entity -> entity.toModel() } }
    }

    fun getNote(id: Long): Flow<NoteModel?> {
        return dao.getNote(id).map { entity -> entity?.toModel() }
    }
}