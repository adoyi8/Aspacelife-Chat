package com.sundroid.aspacelifechat.auth.presentation.chat

import androidx.lifecycle.ViewModel
import com.sundroid.aspacelifechat.auth.domain.model.Message
import com.sundroid.aspacelifechat.auth.domain.model.Response
import com.sundroid.aspacelifechat.auth.domain.model.User
import com.sundroid.aspacelifechat.auth.domain.use_case.GetCurrentUser
import com.sundroid.aspacelifechat.auth.domain.use_case.GetMessages
import com.sundroid.aspacelifechat.auth.domain.use_case.GetUser
import com.sundroid.aspacelifechat.auth.domain.use_case.SendMessage




import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val otherUser: User? = null,
    val currentMessage: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class ChatViewModel(
    private val getMessages: GetMessages,
    private val sendMessage: SendMessage,
    private val getUser: GetUser,
    private val getCurrentUser: GetCurrentUser
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            getCurrentUser()?.let {
                getMessages(conversationId, it.uid).collect { response ->
                    when (response) {
                        is Response.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
                        is Response.Success -> _uiState.value = _uiState.value.copy(isLoading = false, messages = response.data)
                        is Response.Error -> _uiState.value = _uiState.value.copy(isLoading = false, error = response.message)
                    }
                }
            }

        }
    }

    fun onMessageChange(message: String) {
        _uiState.value = _uiState.value.copy(currentMessage = message)
    }

    fun onSendMessage(conversationId: String) {
        val currentUserId = getCurrentUser()?.uid ?: return
        val messageText = _uiState.value.currentMessage.trim()
        if (messageText.isBlank()) return

        viewModelScope.launch {
            val message = Message(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                senderId = currentUserId,
                text = messageText,
                timestamp = System.currentTimeMillis()
            )
            sendMessage(conversationId, message)
            _uiState.value = _uiState.value.copy(currentMessage = "")
        }
    }
}