package com.sundroid.aspacelifechat.domain.use_case

import com.sundroid.aspacelifechat.domain.repository.UserRepository

class GetUser(private val repository: UserRepository) {
    suspend operator fun invoke(userId: String) = repository.getUser(userId)
}