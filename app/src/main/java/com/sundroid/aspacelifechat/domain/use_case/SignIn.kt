package com.sundroid.aspacelifechat.domain.use_case


import com.sundroid.aspacelifechat.domain.repository.AuthRepository
import javax.inject.Inject

class SignIn @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(email: String, pass: String) = repository.signIn(email, pass)
}