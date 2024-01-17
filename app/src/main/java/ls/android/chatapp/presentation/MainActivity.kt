package ls.android.chatapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ls.android.chatapp.data.repository.mock.ConnectionsRepositoryMock
import ls.android.chatapp.presentation.connections.ConnectionRoute
import ls.android.chatapp.presentation.connections.ConnectionsViewModel
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
                    val viewModel = ConnectionsViewModel(ConnectionsRepositoryMock())
                    ConnectionRoute(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxSize(),
                        viewModel = viewModel,
                        {},
                        { viewModel.addItem() })
                }
            }
        }
    }
}