package ls.android.chatapp.data.repository.real

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import ls.android.chatapp.common.ToastHelper
import ls.android.chatapp.domain.repository.AuthenticationRepository
import kotlin.coroutines.resume

class AuthenticationRepositoryImpl(
    private val db: FirebaseAuth,
    private val toastHelper: ToastHelper
) :
    AuthenticationRepository {
    override suspend fun registerUser(userName: String, password: String, navigate: () -> Unit) {
        try {
            val registrationResult = suspendCancellableCoroutine { continuation ->
                db.createUserWithEmailAndPassword(userName.trim(), password.trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(true)
                        } else {
                            toastHelper.createToast("Registration failed", Toast.LENGTH_SHORT)
                            continuation.resume(false)
                        }
                    }
            }

            if (registrationResult) {
                navigate()
            }
        } catch (e: Throwable) {
            Log.d("error", e.message.toString())
        }
    }

    override suspend fun loginUser(userName: String, password: String, navigate: () -> Unit) {
        if (!(userName.isEmpty() || password.isEmpty())) {
            try {
                val authResult = suspendCancellableCoroutine { continuation ->
                    db.signInWithEmailAndPassword(userName.trim(), password.trim())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                continuation.resume(true)
                            } else {
                                toastHelper.createToast("Login failed", Toast.LENGTH_SHORT)
                                continuation.resume(false)
                            }
                        }
                }
                if (authResult) {
                    navigate()
                }
            } catch (e: Throwable) {
                Log.d("error", e.message.toString())
            }
        }
    }
}
