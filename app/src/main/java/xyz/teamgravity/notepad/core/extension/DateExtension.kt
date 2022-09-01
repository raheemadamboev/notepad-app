package xyz.teamgravity.notepad.core.extension

import java.text.DateFormat
import java.util.*

/**
 * Returns formatted string representation of date
 */
fun Date.format(): String {
    return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(this)
}