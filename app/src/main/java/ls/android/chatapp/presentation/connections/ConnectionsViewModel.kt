package ls.android.chatapp.presentation.connections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ls.android.chatapp.domain.model.Connection
import ls.android.chatapp.domain.repository.ConnectionRepository

@OptIn(ExperimentalCoroutinesApi::class)
class ConnectionsViewModel(
    private val repository: ConnectionRepository
) : ViewModel() {
    val connections: Flow<ConnectionScreenState> = repository.getConnections().mapLatest {
        ConnectionScreenState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ConnectionScreenState()
    )

    fun addItem() {
        viewModelScope.launch {
            repository.createConnections(Connection("10", "Lukala", null, 0))
        }
    }
}