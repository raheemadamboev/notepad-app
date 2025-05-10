package xyz.teamgravity.notepad.data.mapper

import xyz.teamgravity.notepad.data.local.note.entity.NoteEntity
import xyz.teamgravity.notepad.data.model.NoteModel
import java.util.Date

///////////////////////////////////////////////////////////////////////////
// Model
///////////////////////////////////////////////////////////////////////////

fun NoteEntity.toModel(): NoteModel {
    return NoteModel(
        id = id,
        title = title,
        body = body,
        created = Date(createdTime),
        edited = Date(editedTime)
    )
}

///////////////////////////////////////////////////////////////////////////
// Entity
///////////////////////////////////////////////////////////////////////////

fun NoteModel.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        body = body,
        createdTime = created.time,
        editedTime = edited.time
    )
}