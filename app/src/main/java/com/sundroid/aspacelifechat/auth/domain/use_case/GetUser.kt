package com.sundroid.aspacelifechat.auth.domain.use_case

import com.sundroid.aspacelifechat.auth.domain.repository.UserRepository

class GetUser(private val repository: UserRepository) {
    suspend operator fun invoke(userId: String) = repository.getUser(userId)
}