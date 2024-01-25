package ls.android.chatapp.data.repository.real

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ls.android.chatapp.common.Constants
import ls.android.chatapp.domain.model.Connection
import ls.android.chatapp.domain.repository.ConnectionRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ConnectionRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val context: Context
) : ConnectionRepository {
    override fun getConnections(): Flow<List<Connection>> {
        return callbackFlow {
            val listenerRegistration = db.collection(Constants.FIREBASE_CONNECTION)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val connections = snapshot?.documents?.mapNotNull {
                        val temp = it.toObject(Connection::class.java)
                        if (temp?.name?.contains(auth.currentUser?.email!!) == true) {
                            it.toObject(Connection::class.java)
                        } else {
                            null
                        }
                    } ?: emptyList()
                    trySend(connections)
                }
            awaitClose { listenerRegistration.remove() }
        }
    }

    override suspend fun createConnections(
        scannedEmail: String,
        usedConnections: List<Connection>
    ) {
        if (usedConnections.any { connectionExists(scannedEmail, usedConnections) }) {
            val currentUserEmail = auth.currentUser?.email!!
            val currentDateTime = LocalDateTime.now()
            val formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDateTime = currentDateTime.format(formatter)

            val connectionData = mapOf(
                "name" to currentUserEmail + scannedEmail,
                "UserOne" to currentUserEmail,
                "UserTwo" to scannedEmail,
                "created" to formattedDateTime
            )

            db.collection(Constants.FIREBASE_CONNECTION)
                .add(connectionData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully connected.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "An error occurred. Try again.", Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
        } else {
            Toast.makeText(context, "Connection already exists.", Toast.LENGTH_SHORT).show()
        }
    }

    override suspend fun removeConnections(connectionId: String) {
        val documentReference =
            db.collection(Constants.FIREBASE_CONNECTION).document(connectionId)

        documentReference
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Successfully deleted.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "An error occurred. Try again.", Toast.LENGTH_SHORT)
                    .show()
                e.printStackTrace()
            }
    }

    private fun connectionExists(
        scannedEmail: String,
        usedConnections: List<Connection>
    ) = usedConnections.any { it.userOne == scannedEmail || it.userTwo == scannedEmail }
}