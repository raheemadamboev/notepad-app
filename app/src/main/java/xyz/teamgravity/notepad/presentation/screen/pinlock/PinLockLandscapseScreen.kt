package xyz.teamgravity.notepad.presentation.screen.pinlock

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.presentation.component.button.IconButtonPlain
import xyz.teamgravity.notepad.presentation.component.pinlock.ChangePinLockCard
import xyz.teamgravity.notepad.presentation.component.pinlock.EnablePinLockCard
import xyz.teamgravity.notepad.presentation.component.pinlock.NoteChangePinLock
import xyz.teamgravity.notepad.presentation.component.pinlock.NotePinLock
import xyz.teamgravity.notepad.presentation.component.pinlock.PinLockDialog
import xyz.teamgravity.notepad.presentation.component.text.TextPlain
import xyz.teamgravity.notepad.presentation.component.topbar.TopBar

@Composable
fun PinLockLandscapeScreen(
    onBackButtonClick: () -> Unit,
    viewmodel: PinLockViewModel = hiltViewModel()
) {
    when (viewmodel.pinLockState) {
        PinLockViewModel.PinLockState.Content -> {
            Scaffold(
                topBar = {
                    TopBar(
                        title = {
                            TextPlain(
                                id = R.string.pin_lock
                            )
                        },
                        navigationIcon = {
                            IconButtonPlain(
                                onClick = onBackButtonClick,
                                icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
                                contentDescription = R.string.cd_back_button
                            )
                        }
                    )
                },
                contentWindowInsets = WindowInsets.safeDrawing
            ) { padding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxHeight()
                    ) {
                        EnablePinLockCard(
                            pinLockEnabled = viewmodel.pinLockEnabled,
                            onPinLockEnabledChange = viewmodel::onPinLockEnabledChange,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        if (viewmodel.pinLockEnabled) {
                            Spacer(
                                modifier = Modifier.height(20.dp)
                            )
                            ChangePinLockCard(
                                onClick = viewmodel::onPinLockChange,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxHeight()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sticker_pin_lock),
                            contentDescription = stringResource(id = R.string.pin_lock)
                        )
                    }
                }
                if (viewmodel.pinLockWarningShown) {
                    PinLockDialog(
                        onConfirm = viewmodel::onPinLockWarningConfirm,
                        onDismiss = viewmodel::onPinLockWarningDismiss
                    )
                }
            }
        }

        PinLockViewModel.PinLockState.Change -> {
            NoteChangePinLock(
                onPinChanged = viewmodel::onPinLockCorrect
            )
        }

        PinLockViewModel.PinLockState.Create,
        PinLockViewModel.PinLockState.Remove -> {
            NotePinLock(
                onPinCorrect = viewmodel::onPinLockCorrect
            )
        }
    }
}