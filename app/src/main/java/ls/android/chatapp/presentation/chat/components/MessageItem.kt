package ls.android.chatapp.presentation.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ls.android.chatapp.R
import ls.android.chatapp.domain.model.Message
import ls.android.chatapp.presentation.ui.DarkBlue_p90
import ls.android.chatapp.presentation.ui.DarkGray
import ls.android.chatapp.presentation.ui.IceBlue_p90

@Composable
fun MessageItem(
    modifier: Modifier,
    message: Message,
    onDoubleClick: (String) -> Unit
) {
    val bgColor by remember {
        mutableStateOf(
            if (message.senderId == "aldkcn") {
                IceBlue_p90
            } else {
                DarkBlue_p90
            }
        )
    }
    Box(
        modifier = modifier
            .wrapContentSize()
    )
    {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .padding(end = 10.dp, bottom = 10.dp)
                .clickable {
                    onDoubleClick(message.id)
                },
            colors = CardDefaults.cardColors(
                containerColor =
// todo the userId that is saved
                bgColor,
                contentColor = DarkGray,
                disabledContainerColor = bgColor,
                disabledContentColor = DarkGray,
            )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = message.text,
                color = DarkGray,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )
        }
        Image(
            modifier = Modifier.align(Alignment.BottomEnd),
            painter = painterResource(id = R.drawable.mingcute_heart_fill),
            contentDescription = "Heart"
        )
    }
}