package ls.android.chatapp.data.repository.real

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
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

    override suspend fun updateConnections(connectionId: String, increment: Boolean) {
        val connectionRef = db.collection(Constants.FIREBASE_CONNECTION).document(connectionId)

        try {
            val documentSnapshot = connectionRef.get().await()

            if (documentSnapshot.exists()) {
                val currentStatus = (documentSnapshot.getLong("status") ?: 0).toInt()
                deleteMessages(currentStatus, increment, documentSnapshot, connectionRef)
            } else {
                Log.d("Error", "Document not found!")
            }
        } catch (e: Exception) {
            Log.e("Error", "An error occurred: $e")
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
                Toast.makeText(context, "An error occurred. Try again.", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
    }

    private fun connectionExists(
        scannedEmail: String,
        usedConnections: List<Connection>
    ) = usedConnections.any { it.userOne == scannedEmail || it.userTwo == scannedEmail }

    private suspend fun deleteMessages(
        currentStatus: Int,
        increment: Boolean,
        documentSnapshot: DocumentSnapshot,
        connectionRef: DocumentReference
    ) {
        if (!increment) {
            if (currentStatus == 1) {
                try {
                    val query = db.collection(Constants.FIREBASE_MESSAGES)
                        .whereEqualTo("connectionId", documentSnapshot.getString("name"))

                    val querySnapshot = query.get().await()

                    for (document in querySnapshot.documents) {
                        db.collection(Constants.FIREBASE_MESSAGES)
                            .document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                println("Document successfully deleted: ${document.id}")
                            }
                            .addOnFailureListener { e ->
                                println("Error deleting document ${document.id}: $e")
                            }
                    }
                } catch (e: Exception) {
                    Log.e("Error", "An error occurred while deleting messages: $e")
                }
            }

            val newStatus = 0
            connectionRef.update("status", newStatus).await()
        } else {
            val newStatus = currentStatus + 1
            connectionRef.update("status", newStatus).await()
        }
    }
}