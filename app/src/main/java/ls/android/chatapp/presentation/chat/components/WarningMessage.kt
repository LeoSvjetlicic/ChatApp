package ls.android.chatapp.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ls.android.chatapp.presentation.ui.DarkBlue
import ls.android.chatapp.presentation.ui.DarkGray
import ls.android.chatapp.presentation.ui.DarkGray_p70
import ls.android.chatapp.presentation.ui.IceBlue
import ls.android.chatapp.presentation.ui.IceBlue_p70

@Composable
fun WarningMessage(
    modifier: Modifier,
    text: String,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize().background(DarkGray_p70)) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .padding(end = 10.dp, bottom = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                IceBlue,
                contentColor = DarkGray,
                disabledContainerColor = IceBlue,
                disabledContentColor = DarkGray,
            )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = text,
                color = DarkGray,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1F)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkBlue)
                        .padding(vertical = 8.dp)
                        .clickable { onYesClick() },
                    text = "No",
                    color = IceBlue_p70,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1F)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkBlue)
                        .padding(vertical = 8.dp)
                        .clickable { onNoClick() },
                    text = "Yes",
                    color = IceBlue_p70,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
