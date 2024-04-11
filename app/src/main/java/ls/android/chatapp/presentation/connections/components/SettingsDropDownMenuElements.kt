package ls.android.chatapp.presentation.connections.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SettingsDropDownMenuElements(
    elements: List<String>,
    modifier: Modifier = Modifier,
    onElementClick: (String) -> Unit
) {
    Box(
        modifier = modifier.then(
            Modifier
                .clip(
                    RoundedCornerShape(8.dp)
                )
                .background(MaterialTheme.colorScheme.background)
                .border(
                    1.dp,
                    Color.Gray,
                    RoundedCornerShape(8.dp)
                )
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            elements.forEach { element ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onElementClick(element) },
                    text = element
                )
            }
        }
    }
}
