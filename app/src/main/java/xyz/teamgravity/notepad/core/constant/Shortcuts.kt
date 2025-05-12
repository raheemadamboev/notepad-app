package xyz.teamgravity.notepad.core.constant

enum class Shortcuts(
    val id: String
) {
    AddNote("add_note");

    companion object {
        fun fromId(id: String?): Shortcuts? {
            if (id == null) return null
            return entries.firstOrNull { it.id == id }
        }
    }
}