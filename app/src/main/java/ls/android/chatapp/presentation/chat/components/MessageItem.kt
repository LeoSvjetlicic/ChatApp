package ls.android.chatapp.presentation.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ls.android.chatapp.R
import ls.android.chatapp.common.User
import ls.android.chatapp.domain.model.Message
import ls.android.chatapp.presentation.ui.DarkBlue_p70
import ls.android.chatapp.presentation.ui.DarkGray
import ls.android.chatapp.presentation.ui.IceBlue_p70

@Composable
fun MessageItem(
    modifier: Modifier,
    message: Message,
    onDoubleClick: (String) -> Unit
) {
    val interactionSource = MutableInteractionSource()
    val bgColor =
        if (message.senderId == User.id) {
            IceBlue_p70
        } else {
            DarkBlue_p70
        }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = if (message.senderId == User.id) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {
        Box(
            modifier = modifier
                .wrapContentSize()
        ) {
            Card(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(end = 10.dp, bottom = 4.dp)
                    .pointerInput(interactionSource) {
                        detectTapGestures(
                            onDoubleTap = {
                                onDoubleClick(message.id)
                            })
                    },
                colors = CardDefaults.cardColors(
                    containerColor =
                    bgColor,
                    contentColor = DarkGray,
                    disabledContainerColor = bgColor,
                    disabledContentColor = DarkGray,
                )
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(8.dp),
                    text = message.text,
                    color = DarkGray,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
            }
            if (message.isLiked) {
                Image(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    painter = painterResource(id = R.drawable.ic_heart),
                    contentDescription = "Heart"
                )
            }
        }
    }
}