package ls.android.chatapp.presentation.chat

import ls.android.chatapp.domain.model.Message

data class ChatViewState(
    val receiver: String = "",
    val messages: List<Message> = listOf()
)