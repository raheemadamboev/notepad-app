package xyz.teamgravity.notepad.deprecated.helper

class Constants {

    companion object {
        /**
         * Intent extra -> Parcelable extra = NoteModel
         */
        const val PARCELABLE_EXTRA = "parcelableExtra"

        /**
         * Intent extra -> Edit note or add note = Boolean
         */
        const val EDIT_NOTE_EXTRA = "modeExtra"

        /**
         * SharedPreferences
         */
        const val PREFS = "notepadSharedPreferences"

        /**
         * SharedPreferences -> Language code = String
         */
        const val LANGUAGE = "language"

        /**
         * SharedPreferences -> Is pin lock turned on = Boolean
         */
        const val PIN_LOCK = "pinLock"

        /**
         * SharedPreferences -> Pin lock hash = String
         */
        const val PIN_HASH = "pinHash"

        /**
         * Company email
         */
        const val SUPPORT_MAIL = "ideatestuz@gmail.com"
    }
}