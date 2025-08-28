package com.sundroid.aspacelifechat.domain.use_case


import com.sundroid.aspacelifechat.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUser @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.currentUser
}