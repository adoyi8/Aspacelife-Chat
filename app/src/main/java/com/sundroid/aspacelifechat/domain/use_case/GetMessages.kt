package com.sundroid.aspacelifechat.domain.use_case

import com.sundroid.aspacelifechat.domain.repository.ChatRepository

class GetMessages(private val repository: ChatRepository) {
    operator fun invoke(conversationId: String, userId: String) = repository.getMessages(conversationId, userId)
}