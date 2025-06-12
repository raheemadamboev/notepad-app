package xyz.teamgravity.notepad.injection.app

import android.app.Application
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import xyz.teamgravity.coresdkandroid.preferences.Preferences
import xyz.teamgravity.notepad.BuildConfig
import xyz.teamgravity.notepad.core.worker.TrashWorker
import xyz.teamgravity.notepad.injection.name.TrashWork
import xyz.teamgravity.pin_lock_compose.PinManager
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var configuration: Configuration

    @Inject
    lateinit var tree: Timber.Tree

    @Inject
    lateinit var preferences: Preferences

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    @TrashWork
    lateinit var trashWork: PeriodicWorkRequest

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(tree)
        PinManager.initialize(
            context = this,
            preferences = preferences
        )
        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = TrashWorker.ID,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.UPDATE,
            request = trashWork
        )
    }

    override val workManagerConfiguration: Configuration
        get() = configuration
}