package com.sundroid.aspacelifechat.auth.domain.use_case

import com.sundroid.aspacelifechat.auth.domain.model.Conversation
import com.sundroid.aspacelifechat.auth.domain.model.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class CreateOrGetConversation(
    private val firestore: FirebaseFirestore
) {
    suspend operator fun invoke(currentUserId: String, otherUserId: String): Response<String> {
        return try {
            val conversationsRef = firestore.collection("conversations")
            val query = conversationsRef.whereArrayContains("members", currentUserId)
            val querySnapshot = query.get().await()

            val existingConversation = querySnapshot.documents.find {
                val members = it["members"] as? List<*>
                members?.contains(otherUserId) == true
            }

            if (existingConversation != null) {
                Response.Success(existingConversation.id)
            } else {
                val newConversationId = UUID.randomUUID().toString()
                val newConversation = Conversation(
                    id = newConversationId,
                    members = listOf(currentUserId, otherUserId).sorted(),
                    lastMessage = "Conversation started.",
                    lastMessageAt = System.currentTimeMillis()
                )
                firestore.collection("conversations").document(newConversationId).set(newConversation).await()
                Response.Success(newConversationId)
            }
        } catch (e: Exception) {
            Response.Error(e.message ?: "Failed to create or get conversation")
        }
    }
}