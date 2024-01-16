package ls.android.chatapp.presentation.connections.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ls.android.chatapp.presentation.ui.DarkBlue
import ls.android.chatapp.presentation.ui.IceBlue

@Composable
fun AddButton(modifier: Modifier, onClick: () -> Unit) {
    FloatingActionButton(
        modifier = modifier,
        onClick = { onClick() },
        shape = CircleShape,
        containerColor = DarkBlue,
        contentColor = IceBlue
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Add button")
    }
}

