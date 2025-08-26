package com.sundroid.aspacelifechat.auth.data.repository


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.sundroid.aspacelifechat.auth.domain.model.Response
import com.sundroid.aspacelifechat.auth.domain.model.User
import com.sundroid.aspacelifechat.auth.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override fun signIn(email: String, password: String): Flow<Response<FirebaseUser>> = callbackFlow {
        trySend(Response.Loading)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(Response.Success(auth.currentUser!!))
                } else {
                    trySend(Response.Error(task.exception?.message ?: "Unknown Error"))
                }
            }
        awaitClose { /* No-op */ }
    }

    override fun signUp(email: String, password: String, displayName: String): Flow<Response<FirebaseUser>> = callbackFlow {
        trySend(Response.Loading)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                // Profile update successful, now save to Firestore
                                val newUser = User(
                                    userId = user.uid,
                                    displayName = user.displayName ?: "",
                                    email = user.email ?: "",
                                    photoUrl = user.photoUrl.toString()
                                )

                                firestore.collection("users")
                                    .document(user.uid)
                                    .set(newUser)
                                    .addOnCompleteListener { firestoreTask ->
                                        if (firestoreTask.isSuccessful) {
                                            // Firestore document created successfully
                                            trySend(Response.Success(user))
                                        } else {
                                            // Firestore save failed
                                            trySend(
                                                Response.Error(
                                                    firestoreTask.exception?.message
                                                        ?: "Failed to save user to Firestore."
                                                )
                                            )
                                        }
                                    }
                            }
                        }
                } else {
                    trySend(Response.Error(task.exception?.message ?: "Unknown Error"))
                }
            }
        awaitClose { /* No-op */ }
    }

    override suspend fun signOut(): Response<Boolean> {
        return try {
            auth.signOut()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Error(e.message ?: "Unknown error")
        }
    }
}