package ls.android.chatapp.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ls.android.chatapp.presentation.ui.IceBlue

@Composable
fun TopBar(
    modifier: Modifier,
    onBackClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(IceBlue)
    ) {
        BackButton(modifier = Modifier
            .fillMaxHeight()
            .align(Alignment.CenterStart)) {
            onBackClick()
        }
    }
}
