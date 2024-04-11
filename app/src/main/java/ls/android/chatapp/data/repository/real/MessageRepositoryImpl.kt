package ls.android.chatapp.data.repository.real

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ls.android.chatapp.common.Constants
import ls.android.chatapp.common.ToastHelper
import ls.android.chatapp.domain.model.Message
import ls.android.chatapp.domain.repository.MessagesRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val toastHelper: ToastHelper
) : MessagesRepository {
    override suspend fun sendMessage(message: String, connectionId: String) {
        if (message.isNotBlank()) {
            val currentDateTime = LocalDateTime.now()
            val formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDateTime = currentDateTime.format(formatter)

            val connectionData = mapOf(
                "text" to message,
                "connectionId" to connectionId,
                "liked" to false,
                "createdAt" to formattedDateTime,
                "sender" to auth.currentUser!!.email
            )

            db.collection(Constants.FIREBASE_MESSAGES)
                .add(connectionData)
                .addOnFailureListener { e ->
                    toastHelper.createToast("An error occurred. Try again.", Toast.LENGTH_SHORT)
                    e.printStackTrace()
                }
        } else {
            toastHelper.createToast("You can't send an empty message.", Toast.LENGTH_SHORT)
        }
    }

    override fun getMessages(connectionId: String): Flow<List<Message>> {
        return callbackFlow {
            val listenerRegistration = db.collection(Constants.FIREBASE_MESSAGES)
                .whereEqualTo("connectionId", connectionId)
                .orderBy("createdAt")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val messages = snapshot?.documents?.mapNotNull {
                        val message = it.toObject(Message::class.java)
                        val messageWithId = message?.copy(id = it.id)
                        messageWithId
                    } ?: emptyList()
                    trySend(messages)
                }
            awaitClose { listenerRegistration.remove() }
        }
    }

    override fun getReceiver(receiverId: String): String {
        val parts = receiverId.split(".", "@").map { string ->
            string.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }
        }

        return parts[0]
    }

    override suspend fun doubleClickMessage(messageId: String, isLiked: Boolean) {
        val messageRef = db.collection("messages").document(messageId)
        val updateData = mapOf("liked" to !isLiked)

        messageRef.update(updateData).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("Error updating message: ${task.exception?.message}")
            }
        }
    }

}
