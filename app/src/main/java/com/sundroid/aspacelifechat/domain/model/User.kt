package com.sundroid.aspacelifechat.domain.model

data class User(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = ""
)