package ls.android.chatapp.presentation.connections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ls.android.chatapp.domain.model.Connection
import ls.android.chatapp.domain.repository.ConnectionRepository
import ls.android.chatapp.domain.repository.MessagesRepository
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ConnectionsViewModel @Inject constructor(
    private val repository: ConnectionRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.updateConnectionMessageToken(Firebase.messaging.token.await())
        }
    }

    private val _currentConnections = MutableStateFlow<List<Connection>>(emptyList())
    val connections: Flow<ConnectionScreenState> = repository.getConnections()
        .mapLatest {
            _currentConnections.value = it
            ConnectionScreenState(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ConnectionScreenState()
        )

    fun addItem(scannedData: String) {
        viewModelScope.launch {
            repository.createConnections(scannedData, _currentConnections.value)
        }
    }
    fun deleteConnection(id: String) {
        viewModelScope.launch {
            launch { repository.updateConnection(id, increment = false, shouldDelete = true) }
            launch { repository.removeConnection(id)
            }
        }
    }
}
