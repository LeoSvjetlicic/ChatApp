package ls.android.chatapp.presentation.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ls.android.chatapp.domain.repository.MessagesRepository

class ChatViewModel(
    private val repository: MessagesRepository,
    private val receiverId: String
) : ViewModel() {
    var messageText: String by mutableStateOf("")
    var isSent: Boolean by mutableStateOf(false)
    val messages: Flow<ChatViewState> = repository.getMessages(receiverId).mapLatest {
        ChatViewState(repository.getReceiver(receiverId), it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ChatViewState()
    )

    fun onUserInputChanged(value: String) {
        messageText = value
    }

    fun setIsSent(value: Boolean) {
        isSent = value
    }

    fun onDoubleClick(id: String) {
        viewModelScope.launch {
            repository.doubleClickMessage(id)
        }
    }

    fun onSendClick(text: String) {
        viewModelScope.launch {
            repository.sendMessage(text)
            isSent = true
            messageText = ""
        }
    }

    fun onClipClick() {
//        TODO
    }
}