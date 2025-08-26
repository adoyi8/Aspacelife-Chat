package com.sundroid.aspacelifechat.auth.domain.use_case

import com.sundroid.aspacelifechat.auth.domain.repository.ChatRepository

class GetMessages(private val repository: ChatRepository) {
    operator fun invoke(conversationId: String, userId: String) = repository.getMessages(conversationId, userId)
}