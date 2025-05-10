package xyz.teamgravity.notepad.data.mapper

import xyz.teamgravity.coresdkandroid.time.TimeUtil
import xyz.teamgravity.notepad.data.local.note.entity.NoteEntity
import xyz.teamgravity.notepad.data.model.NoteModel

///////////////////////////////////////////////////////////////////////////
// Model
///////////////////////////////////////////////////////////////////////////

fun NoteEntity.toModel(): NoteModel {
    return NoteModel(
        id = id,
        title = title,
        body = body,
        created = TimeUtil.fromLongToLocalDateTime(createdTime),
        edited = TimeUtil.fromLongToLocalDateTime(editedTime)
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
        createdTime = TimeUtil.fromLocalDateTimeToLong(created),
        editedTime = TimeUtil.fromLocalDateTimeToLong(edited)
    )
}