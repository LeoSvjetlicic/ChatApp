package ls.android.chatapp.data.repository.real

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import ls.android.chatapp.common.Constants
import ls.android.chatapp.common.ToastHelper
import ls.android.chatapp.domain.model.Connection
import ls.android.chatapp.domain.repository.ConnectionRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ConnectionRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val toastHelper: ToastHelper
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
                            it.toObject(Connection::class.java)?.copy(id = it.id)
                        } else {
                            null
                        }
                    } ?: emptyList()
                    trySend(connections)
                }
            awaitClose { listenerRegistration.remove() }
        }
    }

    override suspend fun getConnection(connectionId: String): Connection? {
        val documentSnapshot = db.collection(Constants.FIREBASE_CONNECTION)
            .document(connectionId)
            .get()
            .await()

        return documentSnapshot.toObject(Connection::class.java)
    }

    override suspend fun createConnections(
        scannedEmail: String,
        usedConnections: List<Connection>
    ) {
        if (usedConnections.any { !connectionExists(scannedEmail, usedConnections) }) {
            val currentUserEmail = auth.currentUser?.email!!
            val currentDateTime = LocalDateTime.now()
            val formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDateTime = currentDateTime.format(formatter)

            val connectionData = mapOf(
                "name" to currentUserEmail + scannedEmail,
                "userOne" to currentUserEmail,
                "userTwo" to scannedEmail,
                "created" to formattedDateTime,
                "status" to 0
            )

            db.collection(Constants.FIREBASE_CONNECTION)
                .add(connectionData)
                .addOnSuccessListener {
                    toastHelper.createToast("Successfully connected.", Toast.LENGTH_SHORT)
                }
                .addOnFailureListener { e ->
                    toastHelper.createToast("An error occurred. Try again.", Toast.LENGTH_SHORT)
                    e.printStackTrace()
                }
        } else {
            toastHelper.createToast("Connection already exists.", Toast.LENGTH_SHORT)
        }
    }

    override suspend fun updateConnection(
        connectionId: String,
        increment: Boolean,
        shouldDelete: Boolean
    ) {
        if (shouldDelete) {
            if (connectionId.isNotBlank()) {
                val connectionRef: DocumentReference =
                    db.collection(Constants.FIREBASE_CONNECTION).document(connectionId)
                try {
                    val currentStatus = connectionRef.get().await().get("status").toString().toInt()
                    update(
                        connectionId,
                        currentStatus,
                        increment,
                        connectionRef,
                        true
                    )
                } catch (e: Exception) {
                    Log.d("mojError", "An error occurred: $e")
                }
            } else {
                Log.d("mojError", "Id is empty")
            }
        } else {
            if (connectionId.isNotBlank()) {
                val connectionRef: DocumentReference =
                    db.collection(Constants.FIREBASE_CONNECTION).document(connectionId)
                try {
                    val currentStatus = connectionRef.get().await().get("status").toString().toInt()
                    update(
                        connectionId,
                        currentStatus,
                        increment,
                        connectionRef,
                        false
                    )
                } catch (e: Exception) {
                    Log.d("mojError", "An error occurred: $e")
                }
            } else {
                Log.d("mojError", "Id is empty")
            }
        }
    }
    override suspend fun updateConnectionMessageToken(token: String) {
        val email = auth.currentUser?.email!!
        if (token.isNotBlank()) {
            try {
                val query = db.collection(Constants.FIREBASE_TOKEN)
                    .whereEqualTo("email", email)
                val querySnapshot = query.get().await()

                if (!querySnapshot.isEmpty) { // If document exists
                    val documentSnapshot = querySnapshot.documents[0]
                    documentSnapshot.reference.update("token", token).await()
                } else {
                    db.collection(Constants.FIREBASE_TOKEN)
                        .document()
                        .set(mapOf("email" to email, "token" to token))
                        .await()
                }
            } catch (e: Exception) {
                Log.d("mojError", "An error occurred: $e")
            }
        } else {
            Log.d("mojError", "Email or token is empty")
        }
    }

    override suspend fun getConnectionMessageToken(email: String): String? {
        if (email.isNotBlank()) {
            try {
                val query = db.collection(Constants.FIREBASE_TOKEN)
                    .whereEqualTo("email", email)
                val querySnapshot = query.get().await()

                if (!querySnapshot.isEmpty) {
                    val documentSnapshot = querySnapshot.documents[0]
                    return documentSnapshot.getString("token")
                } else {
                    Log.d("mojError", "No document found for email: $email")
                    return null // or handle as appropriate for your use case
                }
            } catch (e: Exception) {
                Log.d("mojError", "An error occurred: $e")
                return null // or handle as appropriate for your use case
            }
        } else {
            Log.d("mojError", "Email is empty")
            return null // or handle as appropriate for your use case
        }
    }


    override suspend fun removeConnection(connectionId: String) {
        val documentReference =
            db.collection(Constants.FIREBASE_CONNECTION).document(connectionId)

        documentReference
            .delete()
            .addOnSuccessListener {
                toastHelper.createToast("Successfully deleted.", Toast.LENGTH_SHORT)
            }
            .addOnFailureListener { e ->
                toastHelper.createToast("An error occurred. Try again.", Toast.LENGTH_SHORT)
                e.printStackTrace()
            }
    }

    private fun connectionExists(
        scannedEmail: String,
        usedConnections: List<Connection>
    ) = usedConnections.any { it.userOne == scannedEmail || it.userTwo == scannedEmail }

    private suspend fun update(
        connectionId: String,
        currentStatus: Int,
        increment: Boolean,
        connectionRef: DocumentReference,
        shouldDelete: Boolean
    ) {
        try {
            if (shouldDelete) {
                val query = db.collection(Constants.FIREBASE_MESSAGES)
                    .whereEqualTo("connectionId", connectionId)
                val iLoveATLA = query.get().await()
                for (document in iLoveATLA) {
                    db.collection(Constants.FIREBASE_MESSAGES)
                        .document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            println("Document successfully deleted: $document")
                        }
                        .addOnFailureListener { e ->
                            println("Error deleting document $document: $e")
                        }
                }
            } else {
                if (!increment) {
                    val newStatus = currentStatus - 1
                    val fieldUpdates = mapOf(
                        "status" to newStatus
                    )
                    connectionRef.update(fieldUpdates)
                    if (currentStatus == 1) {
                        val query = db.collection(Constants.FIREBASE_MESSAGES)
                            .whereEqualTo("connectionId", connectionId)
                        val iLoveATLA = query.get().await()
                        for (document in iLoveATLA) {
                            db.collection(Constants.FIREBASE_MESSAGES)
                                .document(document.id)
                                .delete()
                                .addOnSuccessListener {
                                    println("Document successfully deleted: $document")
                                }
                                .addOnFailureListener { e ->
                                    println("Error deleting document $document: $e")
                                }
                        }
                    }
                } else {
                    val newStatus = currentStatus + 1
                    val fieldUpdates = mapOf(
                        "status" to newStatus
                    )
                    connectionRef.update(fieldUpdates)
                }
            }
        } catch (e: Exception) {
            println("Something went wrong.")
        }
    }
}
