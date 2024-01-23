package ls.android.chatapp.data.repository.mock

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ls.android.chatapp.common.User
import ls.android.chatapp.domain.model.Message
import ls.android.chatapp.domain.repository.MessagesRepository
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class MessagesRepositoryMock @Inject constructor() : MessagesRepository {
    private val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    private val currentDate: String = sdf.format(Date())
    private val _messages = MutableStateFlow(
        listOf(
            Message(
                "1",
                "aidjfopsdjsod",
                timeStamp = currentDate,
                senderId = "leo",
            ),
            Message(
                "2",
                "aidjfopsafedfwersdfwersfdjsod",
                timeStamp = currentDate,
                senderId = "",
            ),
            Message(
                "3",
                "aidjfopsdjsod",
                timeStamp = currentDate,
                senderId = "leo",
            ),
            Message(
                "4",
                "aidjfopsdjsod",
                timeStamp = currentDate,
                senderId = "leo",
            ),
            Message(
                "5",
                "aidjfopsdjsod",
                timeStamp = currentDate,
                senderId = "",
                isLiked = true
            ),
            Message(
                "6",
                "aidjfopsdjsod",
                timeStamp = currentDate,
                senderId = "leo",
            )
        )
    )
    private val messages = _messages.asStateFlow()

    override suspend fun sendMessage(message: String) {
        _messages.compareAndSet(
            _messages.value,
            (_messages.value + Message(
                (_messages.value.size + 1).toString(),
                message,
                timeStamp = sdf.format(Date()),
                senderId = User.id
            ))
        )
    }

    override suspend fun getReceiver(receiverId: String): String = "Filip Baotic je konj"
    override fun getMessages(receiverId: String): StateFlow<List<Message>> = messages

    override suspend fun doubleClickMessage(messageId: String) {
        _messages.compareAndSet(
            _messages.value,
            (_messages.value.map {
                if (it.id == messageId) {
                    it.copy(isLiked = it.isLiked.not())
                } else {
                    it
                }
            })
        )
    }
}