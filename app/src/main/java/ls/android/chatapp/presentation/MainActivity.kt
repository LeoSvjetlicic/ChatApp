package ls.android.chatapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ls.android.chatapp.data.repository.mock.MessagesRepositoryMock
import ls.android.chatapp.presentation.chat.ChatRoute
import ls.android.chatapp.presentation.chat.ChatViewModel
import ls.android.chatapp.presentation.ui.ChatAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatRoute(chatViewModel = ChatViewModel(MessagesRepositoryMock(),"User"))
                }
            }
        }
    }
}