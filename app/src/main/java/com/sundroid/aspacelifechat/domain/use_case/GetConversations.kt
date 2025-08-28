package com.sundroid.aspacelifechat.domain.use_case

import com.sundroid.aspacelifechat.domain.repository.ChatRepository

class GetConversations(private val repository: ChatRepository) {
    operator fun invoke(userId: String) = repository.getConversations(userId)
}

class SynConversations(private val repository: ChatRepository) {
    suspend operator fun invoke(userId: String) = repository.syncConversations(userId)
}