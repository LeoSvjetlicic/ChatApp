package ls.android.chatapp.presentation.chat.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ls.android.chatapp.presentation.ui.LocalCustomColorsPalette
import ls.android.chatapp.presentation.ui.MidGray

@Composable
fun NameTag(
    modifier: Modifier,
    name: String
) {
    Card(
        modifier = modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MidGray,
        )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            text = name,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = LocalCustomColorsPalette.current.textColor,
            textAlign = TextAlign.Center
        )
    }
}
