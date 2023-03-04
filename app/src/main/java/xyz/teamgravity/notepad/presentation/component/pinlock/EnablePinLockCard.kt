package xyz.teamgravity.notepad.presentation.component.pinlock

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import xyz.teamgravity.notepad.R

@Composable
fun EnablePinLockCard(
    pinLockEnabled: Boolean,
    onPinLockEnabledChange: () -> Unit,
    modifier: Modifier,
) {
    ElevatedCard(
        onClick = onPinLockEnabledChange,
        shape = MaterialTheme.shapes.large,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.protect_with_pin_lock),
                modifier = Modifier.weight(1F)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Switch(
                checked = pinLockEnabled,
                onCheckedChange = {
                    onPinLockEnabledChange()
                },
            )
        }
    }
}