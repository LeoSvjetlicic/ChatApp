package ls.android.chatapp.domain.model

data class Connection @JvmOverloads constructor(
    val id: String = "",
    val name: String = "",
    val userOne: String? = null,
    val userTwo: String? = null,
    val created: String = ""
)
