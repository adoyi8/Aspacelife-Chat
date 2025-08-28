package com.sundroid.aspacelifechat.domain.repository

import com.sundroid.aspacelifechat.domain.model.Response
import com.sundroid.aspacelifechat.domain.model.User

interface UserRepository {
    suspend fun getUser(userId: String): Response<User>
    suspend fun getAllUsers(): Response<List<User>>
}