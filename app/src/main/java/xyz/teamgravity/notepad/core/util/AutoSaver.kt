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
    private var resolution: EmptyResolution = EmptyResolution.Delete

    private var _note: NoteModel? = null
    private val note get() = _note!!

    private suspend fun saveNote(
        title: String,
        body: String
    ) {
        if (closed) return
        if (_note?.body == body && _note?.title == title && _note?.deleted == null) return

        if (_note == null) {
            if (title.isBlank() && body.isBlank()) return

            _note = NoteModel(
                title = title,
                body = body
            )
            val id = repository.insertNote(note)
            _note = note.copy(id = id)
        } else {
            if (title.isBlank() && body.isBlank()) {
                when (resolution) {
                    EmptyResolution.Delete -> {
                        repository.deleteNote(note)
                        _note = null
                    }

                    EmptyResolution.MoveToTrash -> {
                        _note = note.copy(
                            deleted = LocalDateTime.now()
                        )
                        repository.updateNote(note)
                    }
                }
                return
            }

            _note = note.copy(
                title = title,
                body = body,
                edited = LocalDateTime.now(),
                deleted = null
            )
            repository.updateNote(note)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun start(
        resolution: EmptyResolution,
        note: NoteModel?,
        title: () -> String,
        body: () -> String
    ) {
        if (closed || job != null) return
        job = scope.launch {
            this@AutoSaver.resolution = resolution
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

    fun save(
        title: String,
        body: String
    ) {
        scope.launch {
            saveNote(
                title = title,
                body = body
            )
        }
    }

    fun getCurrentNote(): NoteModel? {
        return _note
    }

    override fun close() {
        job?.cancel()
        job = null
        scope.cancel()
        closed = true
    }

    ///////////////////////////////////////////////////////////////////////////
    // Misc
    ///////////////////////////////////////////////////////////////////////////

    enum class EmptyResolution {
        /**
         * This resolution only should be used when adding a note. This deletes the note from database and original note's created date is lost.
         */
        Delete,

        /**
         * This resolution moves the note to trash when title and body is blank. Whenever title or body is not blank, the original note is moved to valid note.
         */
        MoveToTrash;
    }
}