package com.sundroid.aspacelifechat.auth.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey val id: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val deliveryStatus: String = "sent",
    val isSentByCurrentUser: Boolean = false
)