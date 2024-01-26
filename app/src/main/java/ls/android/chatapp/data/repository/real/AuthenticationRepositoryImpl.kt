package ls.android.chatapp.data.repository.real

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import ls.android.chatapp.common.User
import ls.android.chatapp.domain.repository.AuthenticationRepository
import kotlin.coroutines.resume

class AuthenticationRepositoryImpl(private val db: FirebaseAuth, private val context: Context) :
    AuthenticationRepository {
    override suspend fun registerUser(userName: String, password: String, navigate: () -> Unit) {
        try {
            val registrationResult = suspendCancellableCoroutine { continuation ->
                db.createUserWithEmailAndPassword(userName.trim(), password.trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = db.currentUser
                            User.name = user?.email.toString()
                            continuation.resume(true)
                        } else {
                            Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
                            continuation.resume(false)
                        }
                    }
            }

            if (registrationResult) {
                navigate()
            }
        } catch (e: Throwable) {
            Log.d("error",e.message.toString())
        }
    }
    override suspend fun loginUser(userName: String, password: String, navigate: () -> Unit) {
        if (!(userName.isEmpty() || password.isEmpty())) {
            try {
                val authResult = suspendCancellableCoroutine { continuation ->
                    db.signInWithEmailAndPassword(userName.trim(), password.trim())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = db.currentUser
                                User.name = user?.email.toString()
                                continuation.resume(true)
                            } else {
                                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                                continuation.resume(false)
                            }
                        }
                }
                if (authResult) {
                    navigate()
                }
            } catch (e: Throwable) {
                Log.d("error",e.message.toString())
            }
        }
    }
}