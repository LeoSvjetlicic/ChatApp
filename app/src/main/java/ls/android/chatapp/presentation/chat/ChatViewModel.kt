package ls.android.chatapp.presentation.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ls.android.chatapp.common.GyroscopeHelper
import ls.android.chatapp.data.service.FcmApi
import ls.android.chatapp.domain.model.Connection
import ls.android.chatapp.domain.model.Notification
import ls.android.chatapp.domain.model.NotificationBody
import ls.android.chatapp.domain.model.NotificationState
import ls.android.chatapp.domain.model.SendMessageDto
import ls.android.chatapp.domain.repository.ConnectionRepository
import ls.android.chatapp.domain.repository.MessagesRepository

@HiltViewModel(assistedFactory = ChatViewModel.ChatViewModelFactory::class)
class ChatViewModel @AssistedInject constructor(
    @Assisted val connectionId: String,
    private val repository: MessagesRepository,
    private val connectionRepository: ConnectionRepository,
    val gyroscope: GyroscopeHelper,
    private val auth: FirebaseAuth,
    private val api: FcmApi
) : ViewModel() {

    @AssistedFactory
    fun interface ChatViewModelFactory {
        fun create(connectionId: String?): ChatViewModel
    }

    private var notificationState by mutableStateOf(NotificationState())

    var messageText: String by mutableStateOf("")
    var isSent: Boolean by mutableStateOf(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val messages: Flow<ChatViewState> = repository.getMessages(connectionId).mapLatest {
        val connection = connectionRepository.getConnection(connectionId)
        ChatViewState(connectionId, repository.getReceiver(getReceiverId(connection)), it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ChatViewState()
    )

    private fun sendMessage() {
        viewModelScope.launch {
            val connection = connectionRepository.getConnection(connectionId)
            val receiverEmail = getReceiverId(connection)
            val receiverToken = connectionRepository.getConnectionMessageToken(receiverEmail)
            if (receiverToken != null) {
                val messageDto = SendMessageDto(
                    to = receiverToken,
                    notification = Notification(
                        NotificationBody(
                            "New Message!",
                            repository.getReceiver(receiverEmail) + " sent you a message"
                        )
                    )
                )
                try {
                    api.sendMessage(messageDto)
                    notificationState = notificationState.copy(messageText = "")
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }

    fun onUserInputChanged(value: String) {
        messageText = value
    }

    fun setIsSent(value: Boolean) {
        isSent = value
    }

    fun onDoubleClick(id: String, isLiked: Boolean) {
        viewModelScope.launch {
            repository.doubleClickMessage(id, isLiked)
        }
    }

    fun onSendClick(text: String) {
        viewModelScope.launch {
            repository.sendMessage(text.trim(), connectionId)
            isSent = true
            messageText = ""
            sendMessage()
        }
    }
    fun updateConnectionStatus(status: Boolean) {
        viewModelScope.launch {
            connectionRepository.updateConnection(connectionId, status)
        }
    }
    private fun getReceiverId(connection: Connection): String =
        connection.name.replace(auth.currentUser!!.email!!, "")
}
