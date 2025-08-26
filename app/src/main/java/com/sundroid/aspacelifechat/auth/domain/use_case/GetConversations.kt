package com.sundroid.aspacelifechat.auth.domain.use_case

import com.sundroid.aspacelifechat.auth.domain.repository.ChatRepository

class GetConversations(private val repository: ChatRepository) {
    operator fun invoke(userId: String) = repository.getConversations(userId)
}

class SynConversations(private val repository: ChatRepository) {
    suspend operator fun invoke(userId: String) = repository.syncConversations(userId)
}