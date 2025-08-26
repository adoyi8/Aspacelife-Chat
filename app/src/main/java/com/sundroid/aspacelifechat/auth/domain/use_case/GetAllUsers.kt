package com.sundroid.aspacelifechat.auth.domain.use_case

import com.sundroid.aspacelifechat.auth.domain.repository.UserRepository

class GetAllUsers(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.getAllUsers()
}