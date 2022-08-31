package xyz.teamgravity.notepad.presentation.screen.about

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import xyz.teamgravity.notepad.presentation.component.misc.WindowInfo
import xyz.teamgravity.notepad.presentation.component.misc.rememberWindowInfo
import xyz.teamgravity.notepad.presentation.navigation.MainNavGraph

@MainNavGraph
@Destination
@Composable
fun AboutScreen(
    navigator: DestinationsNavigator,
) {
    when (rememberWindowInfo().screenWidthInfo) {
        WindowInfo.WindowType.Compact -> AboutPortraitScreen(onBackButtonClick = navigator::popBackStack)
        else -> AboutLandscapeScreen(onBackButtonClick = navigator::popBackStack)
    }
}