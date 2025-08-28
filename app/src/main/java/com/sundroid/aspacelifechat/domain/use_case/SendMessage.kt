package com.sundroid.aspacelifechat.domain.use_case

import com.sundroid.aspacelifechat.domain.model.Message
import com.sundroid.aspacelifechat.domain.repository.ChatRepository

class SendMessage(private val repository: ChatRepository) {
    suspend operator fun invoke(conversationId: String, message: Message) =
        repository.sendMessage(conversationId, message)
}