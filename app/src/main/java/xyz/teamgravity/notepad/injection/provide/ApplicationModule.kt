package xyz.teamgravity.notepad.injection.provide

import android.app.Application
import androidx.core.content.getSystemService
import androidx.hilt.work.HiltWorkerFactory
import androidx.paging.PagingConfig
import androidx.room.Room
import androidx.work.Configuration
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import xyz.teamgravity.coresdkandroid.crypto.CryptoManager
import xyz.teamgravity.coresdkandroid.notification.NotificationManager
import xyz.teamgravity.coresdkandroid.preferences.Preferences
import xyz.teamgravity.coresdkandroid.review.ReviewManager
import xyz.teamgravity.coresdkandroid.update.UpdateManager
import xyz.teamgravity.notepad.core.constant.PagingConst
import xyz.teamgravity.notepad.core.notification.TrashNotification
import xyz.teamgravity.notepad.core.util.manager.TrashManager
import xyz.teamgravity.notepad.core.worker.TrashWorker
import xyz.teamgravity.notepad.data.local.note.constant.NoteDatabaseConst
import xyz.teamgravity.notepad.data.local.note.dao.NoteDao
import xyz.teamgravity.notepad.data.local.note.database.NoteDatabase
import xyz.teamgravity.notepad.data.local.note.migration.NoteMigration
import xyz.teamgravity.notepad.data.local.preferences.AppPreferences
import xyz.teamgravity.notepad.data.repository.NoteRepository
import xyz.teamgravity.notepad.injection.name.TrashWork
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private typealias AndroidNotificationManager = android.app.NotificationManager

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(application: Application): NoteDatabase = Room.databaseBuilder(
        context = application,
        klass = NoteDatabase::class.java,
        name = NoteDatabaseConst.NAME
    )
        .addMigrations(NoteMigration.MIGRATION_1_2)
        .build()

    @Provides
    @Singleton
    fun provideNoteDao(noteDatabase: NoteDatabase): NoteDao = noteDatabase.noteDao()

    @Provides
    @Singleton
    fun providePagingConfig(): PagingConfig = PagingConfig(
        pageSize = PagingConst.PAGE_SIZE,
        prefetchDistance = PagingConst.PREFETCH_DISTANCE,
        maxSize = PagingConst.MAX_SIZE,
        enablePlaceholders = PagingConst.ENABLE_PLACEHOLDERS
    )

    @Provides
    @Singleton
    fun provideNoteRepository(
        noteDao: NoteDao,
        pagingConfig: PagingConfig
    ): NoteRepository = NoteRepository(
        dao = noteDao,
        config = pagingConfig
    )

    @Provides
    @Singleton
    fun provideTimberDebugTree(): Timber.DebugTree = Timber.DebugTree()

    @Provides
    @Singleton
    fun provideCryptoManager(): CryptoManager = CryptoManager()

    @Provides
    @Singleton
    fun providePreferences(
        cryptoManager: CryptoManager,
        application: Application
    ): Preferences = Preferences(
        crypto = cryptoManager,
        context = application
    )

    @Provides
    @Singleton
    fun provideAppPreferences(preferences: Preferences): AppPreferences = AppPreferences(preferences)

    @Provides
    @Singleton
    fun provideUpdateManager(application: Application): UpdateManager = UpdateManager(application)

    @Provides
    @Singleton
    fun provideReviewManager(
        preferences: Preferences,
        application: Application
    ): ReviewManager = ReviewManager(
        preferences = preferences,
        context = application
    )

    @Provides
    @Singleton
    fun provideConfiguration(hiltWorkerFactory: HiltWorkerFactory): Configuration = Configuration.Builder()
        .setWorkerFactory(hiltWorkerFactory)
        .build()

    @Provides
    @Singleton
    fun provideWorkManager(application: Application): WorkManager = WorkManager.getInstance(application)

    @Provides
    @Singleton
    fun provideAndroidNotificationManager(application: Application): AndroidNotificationManager = application.getSystemService()!!

    @Provides
    @Singleton
    fun provideNotificationManager(
        application: Application,
        androidNotificationManager: AndroidNotificationManager
    ): NotificationManager = NotificationManager(
        application = application,
        manager = androidNotificationManager
    )

    @Provides
    @Singleton
    @TrashWork
    fun provideTrashWork(): PeriodicWorkRequest =
        PeriodicWorkRequestBuilder<TrashWorker>(TrashWorker.REPEAT_INTERVAL, TimeUnit.DAYS).build()

    @Provides
    @Singleton
    fun provideTrashManager(noteRepository: NoteRepository): TrashManager = TrashManager(noteRepository)

    @Provides
    @Singleton
    fun provideTrashNotification(
        application: Application,
        notificationManager: NotificationManager
    ): TrashNotification = TrashNotification(
        application = application,
        manager = notificationManager
    )
}