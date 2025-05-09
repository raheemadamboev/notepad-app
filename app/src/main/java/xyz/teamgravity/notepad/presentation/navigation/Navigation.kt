package xyz.teamgravity.notepad.presentation.navigation

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.DestinationsNavHost

@Composable
fun Navigation() {
    DestinationsNavHost(navGraph = NavGraphs.main)
}