package xyz.teamgravity.notepad.presentation.screen.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.teamgravity.coresdkcompose.button.IconButtonPlain
import xyz.teamgravity.coresdkcompose.text.TextPlain
import xyz.teamgravity.notepad.BuildConfig
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.presentation.component.topbar.TopBar

@Composable
fun AboutPortraitScreen(
    onBackButtonClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = {
                    TextPlain(
                        id = R.string.app_name
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(
                modifier = Modifier.weight(1F)
            )
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = stringResource(id = R.string.cd_app_icon),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(180.dp)
                    .clip(RoundedCornerShape(35.dp))
            )
            Spacer(
                modifier = Modifier.height(20.dp)
            )
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            Text(
                text = BuildConfig.VERSION_NAME,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.weight(1F)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gravity),
                    contentDescription = stringResource(id = R.string.cd_company_logo),
                    modifier = Modifier
                        .width(100.dp)
                        .height(20.dp)
                )
                Text(
                    text = stringResource(id = R.string.raheem),
                    fontSize = 12.sp
                )
            }
            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }
}