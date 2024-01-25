package ls.android.chatapp.domain.model

data class Message @JvmOverloads constructor(
    val id: String = "",
    val connectionId: String = "",
    val text: String = "",
    val createdAt: String = "",
    val sender: String = "",
    var liked: Boolean = false
)