package com.sundroid.aspacelifechat.presentation.chat

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.koin.compose.viewmodel.koinViewModel



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sundroid.aspacelifechat.presentation.chat.components.ChatAppBar
import com.sundroid.aspacelifechat.presentation.chat.components.MessageBubble
import com.sundroid.aspacelifechat.presentation.chat.components.MessageInput
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    conversationId: String,
    otherUserDisplayName: String,
    navController: NavController,
    viewModel: ChatViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()


    LaunchedEffect(key1 = conversationId) {
        viewModel.loadMessages(conversationId)
    }

    LaunchedEffect(key1 = uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            ChatAppBar(

                userName = otherUserDisplayName,
                isOnline = Random.nextBoolean(),
            ) {
                navController.navigateUp()
            }
        },

        modifier = Modifier.safeDrawingPadding()

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.messages, key = { it.id }) { message ->
                            MessageBubble(message = message)
                        }
                    }
                }
            }

            MessageInput(
                value = uiState.currentMessage,
                onValueChange = { viewModel.onMessageChange(it) },
                onSendClick = { viewModel.onSendMessage(conversationId) }
            )
        }
    }
}

