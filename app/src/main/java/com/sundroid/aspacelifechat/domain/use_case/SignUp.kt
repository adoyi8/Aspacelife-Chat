package com.sundroid.aspacelifechat.domain.use_case


import com.sundroid.aspacelifechat.domain.repository.AuthRepository
import javax.inject.Inject

class SignUp @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(email: String, pass: String, displayName: String) = repository.signUp(email, pass, displayName)
}