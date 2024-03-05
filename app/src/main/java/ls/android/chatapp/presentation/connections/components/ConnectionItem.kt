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
import ls.android.chatapp.common.User
import ls.android.chatapp.domain.model.Connection
import ls.android.chatapp.presentation.ui.DarkBlue
import ls.android.chatapp.presentation.ui.DarkGray
import ls.android.chatapp.presentation.ui.IceBlue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

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
//                    I know it isn't a good practice but it is too late now
                    text = getReceiver(connection.name),
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
                    text = "Connected for: ${getFormattedDate(connection)} days",
                    fontSize = 10.sp,
                    color = DarkGray
                )
            }
        }
    }
}

fun getReceiver(receiverId: String): String {
    val parts = receiverId.replace(User.name, "").split(".", "@").map { string ->
        string.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }
    }

    return parts[0]
}

fun getFormattedDate(connection: Connection): Long {
    val creationDate = LocalDateTime.parse(
        connection.created,
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    )

    val currentDateTime = LocalDateTime.now()
    return ChronoUnit.DAYS.between(creationDate, currentDateTime)

}
