package xyz.teamgravity.notepad.presentation.screen.support

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import xyz.teamgravity.notepad.presentation.navigation.MainNavGraph

@Destination<MainNavGraph>
@Composable
fun SupportScreen(
    navigator: DestinationsNavigator
) {
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> SupportLandscapeScreen(onBackButtonClick = navigator::navigateUp)
        else -> SupportPortraitScreen(onBackButtonClick = navigator::navigateUp)
    }
}