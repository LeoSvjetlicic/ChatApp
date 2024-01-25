package ls.android.chatapp.data.repository.real

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import ls.android.chatapp.common.User
import ls.android.chatapp.domain.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(private val db: FirebaseAuth, private val context: Context) :
    AuthenticationRepository {
    override suspend fun registerUser(userName: String, password: String) {
        db.createUserWithEmailAndPassword(userName.trim(), password.trim()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = db.currentUser
                User.name = user!!.email.toString()
            } else {
                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override suspend fun loginUser(userName: String, password: String) {
        if (!(userName.isEmpty() || password.isEmpty())) {
            db.signInWithEmailAndPassword(userName.trim(), password.trim()).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = db.currentUser
                    User.name = user!!.email.toString()
                } else {
                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}