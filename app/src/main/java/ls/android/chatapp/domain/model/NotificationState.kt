package ls.android.chatapp.domain.model

data class NotificationState(
    val remoteToken: String = "",
    val messageText: String = ""
)

data class SendMessageDto(
    val to: String,
    val notification: Notification
)

data class Notification(
    val notification:NotificationBody
)
data class NotificationBody(
    val title: String,
    val data: String
)
