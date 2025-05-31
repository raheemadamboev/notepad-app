package xyz.teamgravity.notepad.presentation.component.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import xyz.teamgravity.notepad.core.extension.format
import xyz.teamgravity.notepad.data.model.NoteModel
import xyz.teamgravity.notepad.presentation.theme.Grey40

@Composable
fun CardNote(
    note: NoteModel,
    onClick: (note: NoteModel) -> Unit
) {
    OutlinedCard(
        onClick = {
            onClick(note)
        },
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(
            width = if (isSystemInDarkTheme()) 0.5.dp else 1.dp,
            color = Grey40
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = 20.dp,
                    horizontal = 10.dp
                )
        ) {
            Text(
                text = note.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(
                modifier = Modifier.height(5.dp)
            )
            Text(
                text = note.body,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            Text(
                text = if (note.deleted == null) note.edited.format() else note.deleted.format(),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}