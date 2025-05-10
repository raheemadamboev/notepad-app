package xyz.teamgravity.notepad.core.extension

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Returns formatted string representation of the date.
 */
fun LocalDateTime.format(): String {
    return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT).format(this)
}