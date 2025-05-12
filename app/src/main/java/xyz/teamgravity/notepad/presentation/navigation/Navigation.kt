package xyz.teamgravity.notepad.presentation.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.NoteAddScreenDestination
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.spec.NavHostEngine
import xyz.teamgravity.notepad.core.constant.Shortcuts
import xyz.teamgravity.notepad.presentation.activity.Main

@Composable
fun Navigation(
    intent: Intent?,
    engine: NavHostEngine = rememberNavHostEngine(),
    controller: NavHostController = engine.rememberNavController()
) {
    LaunchedEffect(
        key1 = intent,
        block = {
            Shortcuts.fromId(intent?.getStringExtra(Main.EXTRA_SHORTCUT_ID))?.let { shortcut ->
                when (shortcut) {
                    Shortcuts.AddNote -> {
                        controller.navigate(NoteAddScreenDestination.route)
                    }
                }
            }
        }
    )

    DestinationsNavHost(
        navGraph = NavGraphs.main,
        engine = engine,
        navController = controller
    )
}