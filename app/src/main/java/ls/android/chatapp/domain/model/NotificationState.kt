package ls.android.chatapp.domain.model

data class NotificationState(
    val remoteToken: String = "",
    val messageText: String = ""
)

data class SendMessageDto(
    val to: String,
    val notification: NotificationBody
)

data class NotificationBody(
    val title: String,
    val body: String
)
