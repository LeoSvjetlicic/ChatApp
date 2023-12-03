package ls.android.chatapp.presentation.chat.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NameTag(
    modifier: Modifier,
    name: String
) {
    Card(
        modifier = modifier
            .wrapContentHeight()
            .padding(vertical = 16.dp, horizontal = 40.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 4.dp),
            text = name,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            fontSize = 12.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}
