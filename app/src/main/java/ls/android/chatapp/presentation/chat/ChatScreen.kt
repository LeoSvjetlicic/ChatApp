package ls.android.chatapp.presentation.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ls.android.chatapp.presentation.chat.components.BottomBar
import ls.android.chatapp.presentation.chat.components.MessageItem
import ls.android.chatapp.presentation.chat.components.NameTag

@Composable
fun ChatRoute(
    chatViewModel: ChatViewModel,
    onVisibilityChanged: (String, Boolean) -> Unit
) {
    val messages: ChatViewState by chatViewModel.messages.collectAsState(ChatViewState())

    ChatScreen(
        modifier = Modifier,
        viewState = messages,
        isSent = chatViewModel.isSent,
        setIsSent = chatViewModel::setIsSent,
        messageInput = chatViewModel.messageText,
        onUserInputChanged = chatViewModel::onUserInputChanged,
        onDoubleClick = chatViewModel::onDoubleClick,
        onClipClick = chatViewModel::onClipClick,
        onSendClick = chatViewModel::onSendClick,
        onUpdateConnectionStatus = onVisibilityChanged
    )
}

@Composable
fun ChatScreen(
    modifier: Modifier,
    viewState: ChatViewState,
    isSent: Boolean,
    messageInput: String,
    onUserInputChanged: (String) -> Unit,
    setIsSent: (Boolean) -> Unit,
    onDoubleClick: (String, Boolean) -> Unit,
    onClipClick: () -> Unit,
    onSendClick: (String) -> Unit,
    onUpdateConnectionStatus: (String, Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    LaunchedEffect(viewState.connectionId) {
        if (viewState.connectionId.isNotBlank()) {
            onUpdateConnectionStatus(viewState.connectionId, true)
        }
    }

    DisposableEffect(viewState.connectionId) {
        onDispose {
            if (viewState.connectionId.isNotBlank()) {
                onUpdateConnectionStatus(viewState.connectionId, false)
            }
        }
    }
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .clickable(interactionSource = interactionSource, indication = null) {
                focusManager.clearFocus()
            }

    ) {
        val (nameTag, chat, bottomBar) = createRefs()
        val lazyListState = rememberLazyListState()
        LaunchedEffect(isSent) {
            if (isSent) {
                lazyListState.scrollToItem(0)
                setIsSent(false)
            }
        }
        LazyColumn(
            modifier = Modifier
                .constrainAs(chat) {
                    top.linkTo(nameTag.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(bottomBar.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            state = lazyListState,
            reverseLayout = true
        ) {
            items(viewState.messages.reversed()) {
                MessageItem(
                    modifier = Modifier,
                    message = it,
                    onDoubleClick = { id ->
                        onDoubleClick(id, it.liked)
                    })
            }
        }

        NameTag(modifier = Modifier
            .constrainAs(nameTag) {
                top.linkTo(parent.top, margin = 50.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .padding(vertical = 4.dp)
            .width(200.dp),
            name = viewState.receiver
        )

        BottomBar(
            modifier = Modifier
                .height(55.dp)
                .constrainAs(bottomBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                }
                .fillMaxWidth(),
            userInput = messageInput,
            onInputChanged = { onUserInputChanged(it) },
            onClipClick = { onClipClick() },
            onSendClick = { newMessageText ->
                onSendClick(newMessageText)
            })
    }
}
