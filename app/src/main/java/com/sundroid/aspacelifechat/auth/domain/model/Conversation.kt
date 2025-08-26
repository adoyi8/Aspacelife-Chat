package com.sundroid.aspacelifechat.auth.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class Conversation(
    @PrimaryKey val id: String = "",
    val members: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastMessageAt: Long = 0,
    val unreadCount: Int = 0,

    val otherUserDisplayName: String = "",
    val otherUserPhotoUrl: String = ""
)