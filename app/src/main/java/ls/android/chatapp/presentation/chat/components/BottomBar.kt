package ls.android.chatapp.presentation.chat.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ls.android.chatapp.R
import ls.android.chatapp.presentation.ui.DarkGray

@Composable
fun BottomBar(
    modifier: Modifier,
    onClipClick: () -> Unit,
    onSendClick: (String) -> Unit
) {
    var userInput by remember {
        mutableStateOf("")
    }

    ConstraintLayout(modifier = modifier) {
        val (clipButton, inputText, sendButton) = createRefs()
        ActionButton(
            modifier = Modifier
                .size(28.dp)
                .constrainAs(clipButton) {
                    top.linkTo(parent.top, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                },
            imageId = R.drawable.ic_clip
        ) {
            // todo maybe a camera instead
            onClipClick()
        }

        BasicTextField(
            modifier = Modifier
                .constrainAs(inputText) {
                    start.linkTo(clipButton.end, margin = 8.dp)
                    end.linkTo(sendButton.start, margin = 8.dp)
                    top.linkTo(parent.top, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    width = Dimension.fillToConstraints
                },
            singleLine = false,
            textStyle = TextStyle(
                color = DarkGray,
                fontSize = 14.sp,
            ),
            value = userInput,
            onValueChange = { userInput = it }
        )

        ActionButton(
            modifier = Modifier
                .size(28.dp)
                .constrainAs(sendButton) {
                    top.linkTo(parent.top, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                },
            imageId = R.drawable.ic_send
        ) {
            onSendClick(userInput)
        }
    }
}
