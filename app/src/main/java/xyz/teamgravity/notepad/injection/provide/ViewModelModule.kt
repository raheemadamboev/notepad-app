package xyz.teamgravity.notepad.injection.provide

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import xyz.teamgravity.notepad.core.util.AutoSaver
import xyz.teamgravity.notepad.data.repository.NoteRepository

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideAutoSaver(noteRepository: NoteRepository): AutoSaver = AutoSaver(noteRepository)
}