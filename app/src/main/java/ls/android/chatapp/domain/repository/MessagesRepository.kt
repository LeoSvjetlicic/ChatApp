package ls.android.chatapp.domain.repository

import kotlinx.coroutines.flow.Flow
import ls.android.chatapp.domain.model.Message

interface MessagesRepository {
    suspend fun sendMessage(message: String, connectionId: String)
    fun getMessages(connectionId: String): Flow<List<Message>>
    fun getReceiver(receiverId: String): String
    suspend fun doubleClickMessage(messageId: String, isLiked: Boolean)
}