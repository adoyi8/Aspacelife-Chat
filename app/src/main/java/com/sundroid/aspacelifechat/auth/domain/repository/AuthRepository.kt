package com.sundroid.aspacelifechat.auth.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.sundroid.aspacelifechat.auth.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: FirebaseUser?
    fun signIn(email: String, password: String): Flow<Response<FirebaseUser>>
    fun signUp(email: String, password: String, displayName: String): Flow<Response<FirebaseUser>>
    suspend fun signOut(): Response<Boolean>
}