package xyz.teamgravity.notepad.deprecated.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import xyz.teamgravity.notepad.deprecated.model.NoteModel

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NoteRepository(application)

    fun insert(note: NoteModel) = repository.insert(note)

    fun update(note: NoteModel) = repository.update(note)

    fun delete(note: NoteModel) = repository.delete(note)

    fun deleteSelected(notes: List<NoteModel>) = repository.deleteSelected(notes)

    fun deleteAll() = repository.deleteAll()

    fun getAllNotes() = repository.getAllNotes()
}