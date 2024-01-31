package ls.android.chatapp.domain.repository

import kotlinx.coroutines.flow.Flow
import ls.android.chatapp.domain.model.Connection

interface ConnectionRepository {
    fun getConnections(): Flow<List<Connection>>
    suspend fun updateConnections(connectionId: String, increment: Boolean)
    suspend fun createConnections(scannedEmail: String, usedConnections: List<Connection>)
    suspend fun removeConnections(connectionId: String)
}