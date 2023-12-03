package ls.android.chatapp.domain.repository

interface ConnectionRepository {
    // todo return values and parms
    suspend fun getConnections()
    suspend fun createConnections()
}