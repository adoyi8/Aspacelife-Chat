package com.sundroid.aspacelifechat.domain.use_case

import com.sundroid.aspacelifechat.domain.repository.ChatRepository

class DeleteAllMessages(private val repository: ChatRepository) {
    suspend operator fun invoke() = repository.deleteAllMessages()
}