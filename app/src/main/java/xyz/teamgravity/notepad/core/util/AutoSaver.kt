package xyz.teamgravity.notepad.core.util

import kotlinx.coroutines.*
import xyz.teamgravity.notepad.data.model.NoteModel
import xyz.teamgravity.notepad.data.repository.NoteRepository
import java.io.Closeable
import java.util.*

class AutoSaver(
    private val repository: NoteRepository,
) : Closeable {

    companion object {
        private const val DELAY = 15_000L
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var job: Job? = null

    private var closed = false
    private var note: NoteModel? = null

    fun start(note: NoteModel?, title: () -> String, body: () -> String) {
        if (closed || job != null) return
        job = scope.launch {
            this@AutoSaver.note = note
            while (isActive) {
                delay(DELAY)
                saveNote(
                    title = title(),
                    body = body()
                )
            }
        }
    }

    fun saveAndClose(title: String, body: String) {
        scope.launch {
            saveNote(title = title, body = body)
            close()
        }
    }

    private suspend fun saveNote(title: String, body: String) {
        if (!closed && (note?.body != body || note?.title != title)) {
            if (note == null) {
                note = NoteModel(
                    title = title,
                    body = body
                )
                val id = repository.insertNote(note!!)
                note = note!!.copy(id = id)
            } else {
                note = note!!.copy(
                    title = title,
                    body = body,
                    edited = Date()
                )
                repository.updateNote(note!!)
            }
        }
    }

    override fun close() {
        job?.cancel()
        job = null
        scope.cancel()
        closed = true
    }
}