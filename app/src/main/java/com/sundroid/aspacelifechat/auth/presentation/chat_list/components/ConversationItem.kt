package com.sundroid.aspacelifechat.auth.presentation.chat_list.components

import com.sundroid.aspacelifechat.auth.domain.model.Conversation



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sundroid.aspacelifechat.auth.domain.model.Response
import com.sundroid.aspacelifechat.auth.domain.model.User
import com.sundroid.aspacelifechat.auth.domain.use_case.GetUser
import com.sundroid.aspacelifechat.auth.util.formatMessageDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationItem(conversation: Conversation, onClick: () -> Unit) {



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            )
            {
                Text(
                    text = conversation.otherUserDisplayName.take(2).uppercase(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            )
            {
                Text(
                    text = conversation.otherUserDisplayName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = conversation.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            )
            {
                Text(
                    text = formatMessageDate(conversation.lastMessageAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                if (conversation.unreadCount > 0) {
                    Badge {
                        Text(text = conversation.unreadCount.toString())
                    }
                }
            }


    }
}