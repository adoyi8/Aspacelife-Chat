package com.sundroid.aspacelifechat.auth.presentation.chat_list


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable

import org.koin.compose.viewmodel.koinViewModel



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sundroid.aspacelifechat.auth.presentation.chat_list.components.ConversationItem
import com.sundroid.aspacelifechat.auth.presentation.navigation.Screen




import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.sundroid.aspacelifechat.auth.domain.model.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavController, viewModel: ChatListViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Chats")
                        }
                    }
                        },
                actions = {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable(
                                onClick = {
                                    viewModel.showConfirmDialog()
                                }
                            )
                        ,
                        contentAlignment = Alignment.Center
                    )
                    {
                        Text(
                            text = viewModel.currentUserName().take(2).uppercase(),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onFabClicked() }) {
                Icon(Icons.Default.Add, contentDescription = "Start new chat")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        )
        {

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.error != null) {
                Text(text = "Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            } else if (uiState.conversations.isEmpty()) {
                Text(
                    text = "No Conversations Yet\nTap the + button to start a new chat.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.conversations, key = { it.id }) { conversation ->
                        ConversationItem(conversation = conversation) {
                            navController.navigate(Screen.Chat.createRoute(conversation.id, conversation.otherUserDisplayName))
                        }
                    }
                }
            }
            AnimatedVisibility(visible = uiState.isConfirmLogoutDialogVisible) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.hideConfirmDialog()
                    },
                    confirmButton = {

                        TextButton(onClick = {
                            viewModel.onSignOut {
                                navController.navigate(Screen.Auth.route) {
                                    popUpTo(Screen.ChatList.route) { inclusive = true }
                                }
                                viewModel.hideConfirmDialog()
                            }
                        }) {
                            Text("Confirm")
                        }


                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.hideConfirmDialog() }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Confirm Logout") },
                    text = { Text("Are you sure you want to logout?") }
                )
            }
        }
    }

    if (uiState.isUserSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onDismissUserSheet() },
            sheetState = sheetState
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 800.dp),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoadingUsers) {
                    CircularProgressIndicator()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        item {
                            Text(
                                "Start a new chat with:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        items(uiState.allUsers, key = { it.userId }) { user ->
                            UserItem(user = user, onClick = {
                                viewModel.onUserSelected(user) { conversationId, otherUserDisplayName->
                                    navController.navigate(Screen.Chat.createRoute(conversationId, otherUserDisplayName))
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun UserItem(user: User, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = user.displayName.take(1).uppercase(),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = user.displayName, style = MaterialTheme.typography.bodyLarge)
    }
}