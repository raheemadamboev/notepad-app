package xyz.teamgravity.notepad.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import xyz.teamgravity.notepad.data.local.note.dao.NoteDao
import xyz.teamgravity.notepad.data.local.note.entity.NoteEntity
import xyz.teamgravity.notepad.data.mapper.toEntity
import xyz.teamgravity.notepad.data.mapper.toModel
import xyz.teamgravity.notepad.data.model.NoteModel

class NoteRepository(
    private val dao: NoteDao
) {

    ///////////////////////////////////////////////////////////////////////////
    // INSERT
    ///////////////////////////////////////////////////////////////////////////

    suspend fun insertNote(note: NoteModel): Long {
        return withContext(Dispatchers.IO) {
            dao.insertNote(note.toEntity())
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // UPDATE
    ///////////////////////////////////////////////////////////////////////////

    suspend fun updateNote(note: NoteModel) {
        withContext(Dispatchers.IO) {
            dao.updateNote(note.toEntity())
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DELETE
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
    // GET
    ///////////////////////////////////////////////////////////////////////////

    fun getAllNotes(): Flow<List<NoteModel>> {
        return dao.getAllNotes().map { entities -> entities.map { it.toModel() } }
    }
}