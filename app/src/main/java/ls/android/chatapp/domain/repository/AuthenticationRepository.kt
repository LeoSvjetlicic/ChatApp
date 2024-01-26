package ls.android.chatapp.domain.repository

interface AuthenticationRepository {
    suspend fun registerUser(userName: String, password: String, navigate: () -> Unit)
    suspend fun loginUser(userName: String, password: String, navigate: () -> Unit)
}