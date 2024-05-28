package ls.android.chatapp.domain.repository

import kotlinx.coroutines.flow.Flow
import ls.android.chatapp.domain.model.Connection

interface ConnectionRepository {
    fun getConnections(): Flow<List<Connection>>
    suspend fun getConnection(connectionId: String): Connection
    suspend fun updateConnection(connectionId: String, increment: Boolean)
    suspend fun updateConnectionMessageToken(token: String)
    suspend fun getConnectionMessageToken(email: String): String?
    suspend fun createConnections(scannedEmail: String, usedConnections: List<Connection>)
    suspend fun removeConnections(connectionId: String)
}
