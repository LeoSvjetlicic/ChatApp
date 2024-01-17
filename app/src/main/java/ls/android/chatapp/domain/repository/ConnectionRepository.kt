package ls.android.chatapp.domain.repository

import kotlinx.coroutines.flow.StateFlow
import ls.android.chatapp.domain.model.Connection

interface ConnectionRepository {
    // todo return values and parms
    fun getConnections(): StateFlow<List<Connection>>
    suspend fun createConnections(connection: Connection)
    suspend fun removeConnections(connectionId: String)
}