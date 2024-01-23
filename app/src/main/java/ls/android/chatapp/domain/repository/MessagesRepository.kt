package ls.android.chatapp.domain.repository

import kotlinx.coroutines.flow.Flow
import ls.android.chatapp.domain.model.Message

interface MessagesRepository {
    suspend fun sendMessage(message: String)
    fun getMessages(receiverId: String): Flow<List<Message>>
    suspend fun getReceiver(receiverId: String):String
    suspend fun doubleClickMessage(messageId: String)
}