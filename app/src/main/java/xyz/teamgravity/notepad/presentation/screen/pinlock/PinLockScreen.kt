package xyz.teamgravity.notepad.presentation.screen.pinlock

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import xyz.teamgravity.notepad.presentation.navigation.MainNavGraph

@MainNavGraph
@Destination
@Composable
fun PinLockScreen(
    navigator: DestinationsNavigator,
) {
    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> PinLockPortraitScreen(onBackButtonClick = navigator::popBackStack)
        else -> PinLockLandscapeScreen(onBackButtonClick = navigator::popBackStack)
    }
}