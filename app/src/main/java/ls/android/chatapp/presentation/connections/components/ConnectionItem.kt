package ls.android.chatapp.presentation.connections.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import ls.android.chatapp.domain.model.Connection
import ls.android.chatapp.presentation.ui.DarkBlue
import ls.android.chatapp.presentation.ui.DarkGray
import ls.android.chatapp.presentation.ui.IceBlue
import ls.android.chatapp.presentation.ui.LightRed
import ls.android.chatapp.presentation.ui.Red
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun ConnectionItem(
    modifier: Modifier = Modifier,
    connection: Connection,
    onDeleteClick: (String) -> Unit,
    onItemClick: (String) -> Unit
) {
    var isDark by remember {
        mutableStateOf(false)
    }
    val animatedColor = animateColorAsState(
        targetValue = if (isDark) Red else LightRed,
        animationSpec = tween(durationMillis = 1000), label = "" // Adjust duration as needed
    )
    var shouldChange by remember {
        mutableStateOf(false)
    }
    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { -40.dp.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states

    LaunchedEffect(shouldChange) {
        isDark = !isDark
        delay(1000)
        shouldChange = !shouldChange
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 6.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable { onDeleteClick(connection.id) },
            imageVector = Icons.Filled.Delete,
            contentDescription = "",
            tint = Red
        )
        Card(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
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
                        .weight(1f)
                ) {
                    if (connection.status == 1) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 8.dp, end = 8.dp),
                            text = "In Chat",
                            fontSize = 12.sp,
                            color = animatedColor.value
                        )
                    }
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
}
fun getReceiver(receiverId: String): String {
    val parts = receiverId.replace(Firebase.auth.currentUser?.email ?: "", "").split(".", "@")
        .map { string ->
            string.replaceFirstChar {
                if (it.isLowerCase())
                    it.titlecase(Locale.ROOT) else it.toString()
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
