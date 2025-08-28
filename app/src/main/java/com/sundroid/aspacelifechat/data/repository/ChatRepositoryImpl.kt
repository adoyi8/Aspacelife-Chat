package com.sundroid.aspacelifechat.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sundroid.aspacelifechat.data.local.ConversationDao
import com.sundroid.aspacelifechat.data.local.MessageDao
import com.sundroid.aspacelifechat.domain.model.Conversation
import com.sundroid.aspacelifechat.domain.model.Message
import com.sundroid.aspacelifechat.domain.model.Response
import com.sundroid.aspacelifechat.domain.repository.ChatRepository
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import com.sundroid.aspacelifechat.domain.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class ChatRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao,
    private val userRepository: UserRepository
) : ChatRepository {
    override fun getConversations(userId: String): Flow<Response<List<Conversation>>> {

        return conversationDao.getConversations().map { cachedConversations ->
            Response.Success(cachedConversations)
        }
    }
    override suspend fun syncConversations(userId: String) {
        val conversationsQuery = firestore.collection("conversations")
            .whereArrayContains("members", userId)

        conversationsQuery.snapshots().collect { querySnapshot ->

            val firestoreConversations = querySnapshot.toObjects<Conversation>()


            val enrichedConversations = coroutineScope {
                firestoreConversations.map { conversation ->
                    async {
                        val otherUserId = conversation.members.find { it != userId }
                        if (otherUserId != null) {
                            when (val userResponse = userRepository.getUser(otherUserId)) {
                                is Response.Success -> {

                                    conversation.copy(
                                        otherUserDisplayName = userResponse.data.displayName,
                                        otherUserPhotoUrl = userResponse.data.photoUrl
                                    )
                                }
                                else -> conversation

                            }
                        } else {
                            conversation
                        }
                    }
                }.map { it.await() }
            }
            conversationDao.insertAll(enrichedConversations)

        }
    }

    override suspend fun deleteAllMessages() {
        messageDao.deleteAll()
    }

    override suspend fun deleteAllConversations() {
        conversationDao.deleteAll()
    }

    override fun getMessages(conversationId: String, userId: String): Flow<Response<List<Message>>> = flow {
        try {
            emit(Response.Loading)
            val messagesQuery = firestore.collection("conversations")
                .document(conversationId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)


            messagesQuery.snapshots().collect { querySnapshot ->
                val messages = querySnapshot.toObjects<Message>().map {
                    it.copy(isSentByCurrentUser = it.senderId == userId)
                }
                messageDao.insertAll(messages)
                emit(Response.Success(messages))
            }

            messageDao.getMessages(conversationId).collect { cachedMessages ->
                emit(Response.Success(cachedMessages))
            }

        } catch (e: Exception) {

            emit(Response.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    override suspend fun sendMessage(conversationId: String, message: Message): Response<Boolean> {
        return try {
            firestore.runTransaction { transaction ->
                val conversationRef = firestore.collection("conversations").document(conversationId)
                val messageRef = conversationRef.collection("messages").document(message.id)


                transaction.set(messageRef, message)

                transaction.update(
                    conversationRef,
                    "lastMessage", message.text,
                    "lastMessageAt", message.timestamp
                )
                null
            }.await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Error(e.message ?: "Failed to send message")
        }
    }
}