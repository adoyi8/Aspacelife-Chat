package com.sundroid.aspacelifechat.auth.domain.use_case

import com.sundroid.aspacelifechat.auth.domain.repository.ChatRepository

class DeleteAllConversations(private val repository: ChatRepository) {
    suspend operator fun invoke() = repository.deleteAllConversations()
}