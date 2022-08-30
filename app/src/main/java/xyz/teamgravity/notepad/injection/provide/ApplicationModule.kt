package xyz.teamgravity.notepad.injection.provide

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import xyz.teamgravity.notepad.data.local.constant.NoteDatabaseConst
import xyz.teamgravity.notepad.data.local.dao.NoteDao
import xyz.teamgravity.notepad.data.local.database.NoteDatabase
import xyz.teamgravity.notepad.data.repository.NoteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(application: Application): NoteDatabase =
        Room.databaseBuilder(application, NoteDatabase::class.java, NoteDatabaseConst.NAME)
            .addMigrations()
            .build()

    @Provides
    @Singleton
    fun provideNoteDao(noteDatabase: NoteDatabase): NoteDao = noteDatabase.noteDao()

    @Provides
    @Singleton
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository = NoteRepository(noteDao)

    @Provides
    @Singleton
    fun provideTimberDebugTree(): Timber.DebugTree = Timber.DebugTree()
}