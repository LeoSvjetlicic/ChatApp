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
import ls.android.chatapp.domain.model.Connection
import ls.android.chatapp.domain.repository.ConnectionRepository
import ls.android.chatapp.domain.repository.MessagesRepository

@HiltViewModel(assistedFactory = ChatViewModel.ChatViewModelFactory::class)
class ChatViewModel @AssistedInject constructor(
    @Assisted val connectionId: String,
    private val repository: MessagesRepository,
    private val connectionRepository: ConnectionRepository,
    val gyroscope:GyroscopeHelper,
    private val auth: FirebaseAuth
) : ViewModel() {

    @AssistedFactory
    fun interface ChatViewModelFactory {
        fun create(connectionId: String?): ChatViewModel
    }

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
        }
    }

    private fun getReceiverId(connection: Connection): String =
        connection.name.replace(auth.currentUser!!.email!!, "")
}
