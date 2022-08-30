package xyz.teamgravity.notepad.data.mapper

import xyz.teamgravity.notepad.data.local.entity.NoteEntity
import xyz.teamgravity.notepad.data.model.NoteModel
import java.util.*

fun NoteEntity.toModel(): NoteModel {
    return NoteModel(
        id = _id,
        title = title,
        body = body,
        created = Date(createdTime),
        edited = Date(editedTime)
    )
}

fun NoteModel.toEntity(): NoteEntity {
    return NoteEntity(
        _id = id,
        title = title,
        body = body,
        createdTime = created.time,
        editedTime = edited.time
    )
}