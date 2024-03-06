package ls.android.chatapp.presentation.chat.components

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ls.android.chatapp.R
import ls.android.chatapp.presentation.common.components.ActionButton

@Composable
fun BottomBar(
    modifier: Modifier,
    userInput: String,
    onInputChanged: (String) -> Unit,
    onPrivateClick: () -> Unit,
    onSendClick: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    ConstraintLayout(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .border(1.dp, Color.LightGray)
    ) {
        val (clipButton, inputText, sendButton) = createRefs()

        ActionButton(
            modifier = Modifier
                .size(40.dp)
                .constrainAs(clipButton) {
                    top.linkTo(parent.top, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                },
            imageId = R.drawable.ic_incognito,
        ) {
            onPrivateClick()
        }

        BasicTextField(
            modifier = Modifier
                .constrainAs(inputText) {
                    start.linkTo(clipButton.end, margin = 8.dp)
                    end.linkTo(sendButton.start, margin = 8.dp)
                    top.linkTo(parent.top, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    width = Dimension.fillToConstraints
                }
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                },
            singleLine = false,
            textStyle = TextStyle(
                color = Color.Gray,
                fontSize = 14.sp,
            ),
            value = userInput,
            onValueChange = { onInputChanged(it) }
        )

        ActionButton(
            modifier = Modifier
                .size(40.dp)
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
