package xyz.teamgravity.notepad.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import xyz.teamgravity.notepad.data.model.NoteModel
import xyz.teamgravity.notepad.data.repository.NoteRepository
import java.io.Closeable
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.seconds

class AutoSaver(
    private val repository: NoteRepository,
) : Closeable {

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var job: Job? = null
    private var closed = false

    private var _note: NoteModel? = null
    private val note get() = _note!!

    private suspend fun saveNote(
        title: String,
        body: String
    ) {
        if (closed) return
        if (_note?.body == body && _note?.title == title) return

        if (_note == null) {
            _note = NoteModel(
                title = title,
                body = body
            )
            val id = repository.insertNote(note)
            _note = note.copy(id = id)
        } else {
            _note = note.copy(
                title = title,
                body = body,
                edited = LocalDateTime.now()
            )
            repository.updateNote(note)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun start(
        note: NoteModel?,
        title: () -> String,
        body: () -> String
    ) {
        if (closed || job != null) return
        job = scope.launch {
            this@AutoSaver._note = note
            while (isActive) {
                delay(15.seconds)
                saveNote(
                    title = title(),
                    body = body()
                )
            }
        }
    }

    fun saveAndClose(
        title: String,
        body: String
    ) {
        scope.launch {
            saveNote(
                title = title,
                body = body
            )
            close()
        }
    }

    override fun close() {
        job?.cancel()
        job = null
        scope.cancel()
        closed = true
    }
}