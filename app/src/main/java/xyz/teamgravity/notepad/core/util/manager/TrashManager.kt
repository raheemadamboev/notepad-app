package xyz.teamgravity.notepad.core.util.manager

import xyz.teamgravity.coresdkandroid.time.TimeUtil
import xyz.teamgravity.notepad.data.repository.NoteRepository
import java.time.LocalDateTime

class TrashManager(
    private val repository: NoteRepository
) {

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    suspend fun deleteExpiredNotes() {
        val expiredDate = LocalDateTime.now().minusDays(30)
        repository.deleteExpiredNotes(TimeUtil.fromLocalDateTimeToLong(expiredDate))
    }
}