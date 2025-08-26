package com.sundroid.aspacelifechat.auth.domain.repository

import com.sundroid.aspacelifechat.auth.domain.model.Response
import com.sundroid.aspacelifechat.auth.domain.model.User

interface UserRepository {
    suspend fun getUser(userId: String): Response<User>
    suspend fun getAllUsers():Response<List<User>>
}