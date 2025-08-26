package com.sundroid.aspacelifechat.auth.domain.use_case

import com.sundroid.aspacelifechat.auth.domain.model.Message
import com.sundroid.aspacelifechat.auth.domain.repository.ChatRepository

class SendMessage(private val repository: ChatRepository) {
    suspend operator fun invoke(conversationId: String, message: Message) =
        repository.sendMessage(conversationId, message)
}