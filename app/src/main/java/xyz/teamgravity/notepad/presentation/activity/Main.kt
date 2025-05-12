package xyz.teamgravity.notepad.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(intent)
                }
            }
        }
    }
}