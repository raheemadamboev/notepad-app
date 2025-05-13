package xyz.teamgravity.notepad.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import xyz.teamgravity.notepad.presentation.navigation.Navigation
import xyz.teamgravity.notepad.presentation.theme.NotepadTheme

@AndroidEntryPoint
class Main : ComponentActivity() {

    companion object {
        const val EXTRA_SHORTCUT_ID = "Main_extraShortcutId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val intent = intent
        setContent {
            NotepadTheme {
                Navigation(intent)
            }
        }
    }
}