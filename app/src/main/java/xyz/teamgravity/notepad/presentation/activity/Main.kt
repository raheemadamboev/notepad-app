package xyz.teamgravity.notepad.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import xyz.teamgravity.notepad.presentation.component.pinlock.NotePinLock
import xyz.teamgravity.notepad.presentation.navigation.Navigation
import xyz.teamgravity.notepad.presentation.theme.NotepadTheme

@AndroidEntryPoint
class Main : ComponentActivity() {

    companion object {
        const val EXTRA_SHORTCUT_ID = "Main_extraShortcutId"
    }

    private val viewmodel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val intent = if (savedInstanceState == null) intent else null
        setContent {
            NotepadTheme {
                when (viewmodel.navigation) {
                    MainViewModel.Navigation.None -> Unit

                    MainViewModel.Navigation.PinLock -> {
                        NotePinLock(
                            onPinCorrect = viewmodel::onPinCorrect
                        )
                    }

                    MainViewModel.Navigation.Content -> {
                        Navigation(intent)
                    }
                }
            }
        }
    }
}