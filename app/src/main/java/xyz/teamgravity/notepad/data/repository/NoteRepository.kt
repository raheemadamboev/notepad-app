package xyz.teamgravity.notepad.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import xyz.teamgravity.notepad.data.local.note.dao.NoteDao
import xyz.teamgravity.notepad.data.mapper.toEntity
import xyz.teamgravity.notepad.data.mapper.toModel
import xyz.teamgravity.notepad.data.model.NoteModel

class NoteRepository(
    private val dao: NoteDao,
    private val config: PagingConfig
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

    suspend fun deleteExpiredNotes(expiredTime: Long) {
        withContext(Dispatchers.IO) {
            dao.deleteExpiredNotes(expiredTime)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Get
    ///////////////////////////////////////////////////////////////////////////

    fun getValidNotes(): Flow<PagingData<NoteModel>> {
        return Pager(
            config = config,
            pagingSourceFactory = {
                dao.getValidNotes()
            }
        ).flow.map { entities ->
            entities.map { entity ->
                entity.toModel()
            }
        }
    }

    fun getDeletedNotes(): Flow<PagingData<NoteModel>> {
        return Pager(
            config = config,
            pagingSourceFactory = {
                dao.getDeletedNotes()
            }
        ).flow.map { entities ->
            entities.map { entity ->
                entity.toModel()
            }
        }
    }

    fun getNote(id: Long): Flow<NoteModel?> {
        return dao.getNote(id).map { entity -> entity?.toModel() }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Misc
    ///////////////////////////////////////////////////////////////////////////

    suspend fun moveValidNotesToTrash(deletedTime: Long) {
        withContext(Dispatchers.IO) {
            dao.moveValidNotesToTrash(deletedTime)
        }
    }

    suspend fun restoreDeletedNotes() {
        withContext(Dispatchers.IO) {
            dao.restoreDeletedNotes()
        }
    }
}