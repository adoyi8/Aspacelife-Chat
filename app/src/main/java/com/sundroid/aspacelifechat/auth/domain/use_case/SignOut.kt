package com.sundroid.aspacelifechat.auth.domain.use_case


import com.sundroid.aspacelifechat.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignOut @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.signOut()
}