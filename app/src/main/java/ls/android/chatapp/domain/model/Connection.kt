package ls.android.chatapp.domain.model

data class Connection(
    val id:String,
    val name:String,
    val nickName:String? = null,
    val daysConnected:Int = 1
)
