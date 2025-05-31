package xyz.teamgravity.notepad.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import xyz.teamgravity.notepad.core.notification.TrashNotification
import xyz.teamgravity.notepad.core.util.manager.TrashManager

@HiltWorker
class TrashWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val trash: TrashManager,
    private val notification: TrashNotification
) : CoroutineWorker(context, workerParameters) {

    companion object {
        const val ID = "xyz.teamgravity.notepad.TrashWorker"
        const val REPEAT_INTERVAL = 1L
    }

    override suspend fun doWork(): Result {
        trash.deleteExpiredNotes()
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(notification.id(), notification.notification())
    }
}