package ls.android.chatapp.presentation.connections.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ls.android.chatapp.domain.model.Connection
import ls.android.chatapp.presentation.ui.DarkBlue
import ls.android.chatapp.presentation.ui.DarkGray
import ls.android.chatapp.presentation.ui.IceBlue

@Composable
fun ConnectionItem(
    modifier: Modifier = Modifier,
    connection: Connection,
    onItemClick: (String) -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onItemClick(connection.id) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = IceBlue,
            contentColor = DarkBlue,
            disabledContainerColor = IceBlue,
            disabledContentColor = DarkBlue,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2F)
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 14.dp)
                        .align(Alignment.CenterStart),
                    text = connection.name,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp,
                    maxLines = 1,
                    color = DarkGray,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1F)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp, end = 8.dp),
                    text = "Connected for: ${connection.daysConnected} days",
                    fontSize = 10.sp,
                    color = DarkGray
                )
            }
        }
    }
}
