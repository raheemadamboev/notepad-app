package xyz.teamgravity.notepad.presentation.component.pinlock

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import xyz.teamgravity.coresdkcompose.text.TextPlain
import xyz.teamgravity.notepad.R

@Composable
fun ChangePinLockCard(
    onClick: () -> Unit,
    modifier: Modifier
) {
    ElevatedCard(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 22.dp
                )
        ) {
            TextPlain(
                id = R.string.change_pin_lock
            )
        }
    }
}