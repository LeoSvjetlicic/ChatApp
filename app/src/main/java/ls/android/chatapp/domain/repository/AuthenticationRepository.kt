package ls.android.chatapp.domain.repository

interface AuthenticationRepository {
    // todo return values
    suspend fun registerUser(userName: String, password: String)
    suspend fun loginUser(userName: String, password: String)
}