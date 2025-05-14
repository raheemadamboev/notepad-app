package xyz.teamgravity.notepad.injection.provide

import android.app.Application
import androidx.paging.PagingConfig
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import xyz.teamgravity.coresdkandroid.crypto.CryptoManager
import xyz.teamgravity.coresdkandroid.preferences.Preferences
import xyz.teamgravity.coresdkandroid.update.UpdateManager
import xyz.teamgravity.notepad.core.constant.PagingConst
import xyz.teamgravity.notepad.data.local.note.constant.NoteDatabaseConst
import xyz.teamgravity.notepad.data.local.note.dao.NoteDao
import xyz.teamgravity.notepad.data.local.note.database.NoteDatabase
import xyz.teamgravity.notepad.data.local.preferences.AppPreferences
import xyz.teamgravity.notepad.data.repository.NoteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(application: Application): NoteDatabase = Room.databaseBuilder(
        context = application,
        klass = NoteDatabase::class.java,
        name = NoteDatabaseConst.NAME
    ).addMigrations().build()

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
}