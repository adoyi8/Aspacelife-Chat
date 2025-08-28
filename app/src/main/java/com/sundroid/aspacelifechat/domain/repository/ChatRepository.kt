package com.sundroid.aspacelifechat.domain.repository

import com.sundroid.aspacelifechat.domain.model.Conversation
import com.sundroid.aspacelifechat.domain.model.Message
import com.sundroid.aspacelifechat.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun getConversations(userId: String): Flow<Response<List<Conversation>>>
    fun getMessages(conversationId: String, userId: String): Flow<Response<List<Message>>>
    suspend fun sendMessage(conversationId: String, message: Message): Response<Boolean>
    suspend fun syncConversations(userId: String)
    suspend fun deleteAllMessages()
    suspend fun deleteAllConversations()
}