package ls.android.chatapp.domain.model

data class Message(
    val id: String,
    val text: String,
    val timeStamp: String,
    val senderId: String,
    var isLiked: Boolean = false
)