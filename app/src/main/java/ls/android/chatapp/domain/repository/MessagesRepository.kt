package ls.android.chatapp.domain.repository

interface MessagesRepository {
    // todo return values and params
    suspend fun sendMessage()
    suspend fun getMessage()
}