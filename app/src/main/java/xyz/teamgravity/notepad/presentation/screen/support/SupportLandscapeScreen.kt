package xyz.teamgravity.notepad.presentation.screen.support

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.teamgravity.coresdkcompose.button.IconButtonPlain
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.core.util.Helper
import xyz.teamgravity.notepad.presentation.component.card.CardConnection
import xyz.teamgravity.notepad.presentation.theme.White

@Composable
fun SupportLandscapeScreen(
    onBackButtonClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(12.dp)
            ) {
                IconButtonPlain(
                    onClick = onBackButtonClick,
                    icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
                    contentDescription = R.string.cd_back_button,
                    tint = White
                )
                Text(
                    text = stringResource(R.string.need_help),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = White,
                    modifier = Modifier.weight(1F)
                )
                Spacer(
                    modifier = Modifier.width(38.dp)
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(
                    horizontal = 16.dp,
                    vertical = 20.dp
                )
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) {
                Text(
                    text = stringResource(R.string.connect_us),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.connect_us_body),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) {
                CardConnection(
                    onClick = {
                        Helper.connectViaTelegram(context)
                    },
                    icon = R.drawable.ic_telegram,
                    title = R.string.via_telegram,
                    contentDescription = R.string.cd_via_telegram,
                    fillMaxSize = false,
                    modifier = Modifier.weight(1F)
                )
                CardConnection(
                    onClick = {
                        Helper.connectViaEmail(context)
                    },
                    icon = R.drawable.ic_email,
                    title = R.string.via_email,
                    contentDescription = R.string.cd_via_email,
                    fillMaxSize = false,
                    modifier = Modifier.weight(1F)
                )
            }
        }
    }
}