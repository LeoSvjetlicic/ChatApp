package ls.android.chatapp.presentation.connections.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            elements.forEach { element ->
                val interactionSource by remember {
                    mutableStateOf(MutableInteractionSource())
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(interactionSource,null) { onElementClick(element) },
                    text = element,
                    fontSize = 20.sp
                )
            }
        }
    }
}
