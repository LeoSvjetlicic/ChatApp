package ls.android.chatapp.domain.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ls.android.chatapp.R
import ls.android.chatapp.presentation.ui.DarkBlue

@Composable
fun BackButton(
    modifier: Modifier,
    onBackClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxHeight()
            .wrapContentWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .clickable { onBackClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 8.dp),
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = null,
        )
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = "Return",
            fontSize = 10.sp,
            fontWeight = FontWeight(600),
            color = DarkBlue
        )
    }
}
