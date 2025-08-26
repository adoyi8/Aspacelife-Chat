package com.sundroid.aspacelifechat.auth.domain.model

data class User(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = ""
)