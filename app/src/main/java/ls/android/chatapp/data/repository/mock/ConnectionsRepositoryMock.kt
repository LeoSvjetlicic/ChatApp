package ls.android.chatapp.data.repository.mock

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ls.android.chatapp.domain.model.Connection
import ls.android.chatapp.domain.repository.ConnectionRepository
import javax.inject.Inject

class ConnectionsRepositoryMock @Inject constructor() : ConnectionRepository {
    private val _connections = MutableStateFlow(
        mutableListOf(
            Connection("1", "Leo", null, 30),
            Connection("2", "Mato", null, 69),
            Connection("3", "Filip", null, 9),
            Connection("3", "Matan", null, 3),
        )
    )
    private val connections = _connections.asStateFlow()
    override fun getConnections(): StateFlow<List<Connection>> = connections

    override suspend fun createConnections(connection: Connection) {
        _connections.compareAndSet(
            _connections.value,
            (_connections.value + Connection(
                (_connections.value.size + 1).toString(),
                connection.name,
                null,
                1
            )) as MutableList<Connection>
        )
    }

    override suspend fun removeConnections(connectionId: String) {
        val connectionToRemove = _connections.value.find { it.id == connectionId }
        val newConnections = _connections.value
        newConnections.remove(connectionToRemove)
        _connections.compareAndSet(_connections.value, newConnections)
    }
}