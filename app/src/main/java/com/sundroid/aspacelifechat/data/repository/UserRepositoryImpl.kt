package com.sundroid.aspacelifechat.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sundroid.aspacelifechat.domain.model.Response
import com.sundroid.aspacelifechat.domain.model.User
import com.sundroid.aspacelifechat.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(private val firestore: FirebaseFirestore) : UserRepository {
    override suspend fun getUser(userId: String): Response<User> {
        return try {
            val document = firestore.collection("users").document(userId).get().await()
            val user = document.toObject(User::class.java)
            Response.Success(user!!)
        } catch (e: Exception) {
            Response.Error(e.message ?: "Failed to fetch user")
        }
    }

    override suspend fun getAllUsers(): Response<List<User>> {
        return try {
            val querySnapshot = firestore.collection("users").get().await()


            val users = querySnapshot.toObjects(User::class.java)


            Response.Success(users)
        } catch (e: Exception) {

            Response.Error(e.message ?: "Failed to fetch all users")
        }
    }
}