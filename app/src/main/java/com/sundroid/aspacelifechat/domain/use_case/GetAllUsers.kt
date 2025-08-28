package com.sundroid.aspacelifechat.domain.use_case

import com.sundroid.aspacelifechat.domain.repository.UserRepository

class GetAllUsers(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.getAllUsers()
}