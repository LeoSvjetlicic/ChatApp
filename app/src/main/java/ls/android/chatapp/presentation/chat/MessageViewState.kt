package ls.android.chatapp.presentation.chat

import ls.android.chatapp.domain.model.Message

data class ChatViewState(
    val connectionId: String = "",
    val receiver: String = "",
    val messages: List<Message> = listOf()
)