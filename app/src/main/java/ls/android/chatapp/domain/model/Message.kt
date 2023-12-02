package ls.android.chatapp.domain.model

data class Message (
    val id:String,
    val text:String,
    val isLiked:Boolean,
    val senderId:String
)