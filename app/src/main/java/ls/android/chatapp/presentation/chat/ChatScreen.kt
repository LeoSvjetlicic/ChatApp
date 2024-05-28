package ls.android.chatapp.presentation.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ls.android.chatapp.common.GyroscopeHelper
import ls.android.chatapp.presentation.chat.components.BottomBar
import ls.android.chatapp.presentation.chat.components.MessageItem
import ls.android.chatapp.presentation.chat.components.NameTag
import ls.android.chatapp.presentation.ui.LocalCustomColorsPalette

const val BOTTOM_BAR_HEIGHT = 55

@Composable
fun ChatRoute(
    chatViewModel: ChatViewModel,
    maxXInitial: Float,
    maxYInitial: Float,
    onVisibilityChanged: (String, Boolean) -> Unit,
) {
    val circleOffset by chatViewModel.gyroscope.getGyroscopeData()
        .collectAsState(FloatArray(9) { 0f })
    val messages: ChatViewState by chatViewModel.messages.collectAsState(ChatViewState())

    ChatScreen(
        modifier = Modifier,
        gyroscopeData = circleOffset,
        viewState = messages,
        isSent = chatViewModel.isSent,
        setIsSent = chatViewModel::setIsSent,
        messageInput = chatViewModel.messageText,
        maxXInitial = maxXInitial,
        maxYInitial = maxYInitial,
        onUserInputChanged = chatViewModel::onUserInputChanged,
        onDoubleClick = chatViewModel::onDoubleClick,
        onSendClick = chatViewModel::onSendClick,
        onUpdateConnectionStatus = onVisibilityChanged
    )
}

@Composable
fun ChatScreen(
    modifier: Modifier,
    gyroscopeData: FloatArray,
    viewState: ChatViewState,
    isSent: Boolean,
    messageInput: String,
    maxXInitial: Float,
    maxYInitial: Float,
    onUserInputChanged: (String) -> Unit,
    setIsSent: (Boolean) -> Unit,
    onDoubleClick: (String, Boolean) -> Unit,
    onSendClick: (String) -> Unit,
    onUpdateConnectionStatus: (String, Boolean) -> Unit
) {
    var isPrivateMode by remember {
        mutableStateOf(false)
    }
    var clockTrigger by remember {
        mutableStateOf(false)
    }
    val density = LocalDensity.current.density
    var maxX by remember {
        mutableFloatStateOf(maxXInitial)
    }
    var maxY by remember {
        mutableFloatStateOf(maxYInitial)
    }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    var offset by remember {
        mutableStateOf(Offset(maxX / 2, maxY / 2))
    }
    val outlineColor = LocalCustomColorsPalette.current.privateModeOutline
    val backgroundColor = MaterialTheme.colorScheme.background
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
        val privateScreen = createRef()
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
                }
                .onGloballyPositioned {
                    maxX = it.size.width / density
                    maxY = it.size.height / density
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
        if (isPrivateMode) {
            LaunchedEffect(clockTrigger) {
                clockTrigger = !clockTrigger
                offset =
                    GyroscopeHelper.calculateWeight(gyroscopeData, maxX, maxY, offset, density)
            }
            Box(modifier = Modifier
                .constrainAs(privateScreen) {
                    top.linkTo(nameTag.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(bottomBar.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        Brush.radialGradient(
                            listOf(
                                Color.Transparent,
                                Color.Transparent,
                                outlineColor,
                                backgroundColor,
                                backgroundColor
                            ),
                            center = Offset(
                                offset.x.dp.toPx(),
                                offset.y.dp.toPx()
                            ),
                            radius = 100.dp.toPx(),
                        )
                    )
                })
        }
        BottomBar(
            modifier = Modifier
                .height(BOTTOM_BAR_HEIGHT.dp)
                .constrainAs(bottomBar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                }
                .fillMaxWidth(),
            userInput = messageInput,
            onInputChanged = { onUserInputChanged(it) },
            onPrivateClick = {
                isPrivateMode = !isPrivateMode
                offset = Offset(maxX / 2, maxY / 2)
            },
            onSendClick = { newMessageText ->
                onSendClick(newMessageText)
            })
    }
}
