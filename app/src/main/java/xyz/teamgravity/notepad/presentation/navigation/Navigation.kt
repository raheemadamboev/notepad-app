package xyz.teamgravity.notepad.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.Navigation
import com.ramcosta.composedestinations.DestinationsNavHost
import xyz.teamgravity.notepad.presentation.screen.NavGraphs

@Composable
fun Navigation() {
    DestinationsNavHost(navGraph = NavGraphs.main)
}