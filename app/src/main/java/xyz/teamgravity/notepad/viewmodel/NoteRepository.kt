package xyz.teamgravity.notepad.viewmodel

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import xyz.teamgravity.notepad.model.NoteModel

class NoteRepository(application: Application) {

    private val noteDao = MyDatabase.getMyDatabase(application)!!.noteDao()

    fun insert(note: NoteModel) {
        InsertTask(noteDao).execute(note)
    }

    fun update(note: NoteModel) {
        UpdateTask(noteDao).execute(note)
    }

    fun delete(note: NoteModel) {
        DeleteTask(noteDao).execute(note)
    }

    fun deleteSelected(notes: List<NoteModel>) {
        DeleteSelectedTask(noteDao, notes).execute()
    }

    fun deleteAll() {
        DeleteAllTask(noteDao).execute()
    }

    fun getAllNotes(): LiveData<List<NoteModel>> {
        return noteDao.getAllNotes()
    }

    class InsertTask(private val noteDao: NoteDao) : AsyncTask<NoteModel, Void, Void?>() {
        override fun doInBackground(vararg params: NoteModel?): Void? {
            noteDao.insert(params[0]!!)
            return null
        }
    }

    class UpdateTask(private val noteDao: NoteDao) : AsyncTask<NoteModel, Void, Void?>() {
        override fun doInBackground(vararg params: NoteModel?): Void? {
            noteDao.update(params[0]!!)
            return null
        }
    }

    class DeleteSelectedTask(private val noteDao: NoteDao, private val notes: List<NoteModel>) :
        AsyncTask<Void, Void, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            noteDao.deleteSelected(notes)
            return null
        }
    }

    class DeleteTask(private val noteDao: NoteDao) : AsyncTask<NoteModel, Void, Void?>() {
        override fun doInBackground(vararg params: NoteModel?): Void? {
            noteDao.delete(params[0]!!)
            return null
        }
    }

    class DeleteAllTask(private val noteDao: NoteDao) : AsyncTask<Void, Void, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            noteDao.deleteAll()
            return null
        }
    }
}