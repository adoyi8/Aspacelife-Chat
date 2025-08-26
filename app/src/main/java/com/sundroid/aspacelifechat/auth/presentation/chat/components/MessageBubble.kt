package com.sundroid.aspacelifechat.auth.presentation.chat.components

import com.sundroid.aspacelifechat.auth.domain.model.Message



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MessageBubble(message: Message) {
    val bubbleColor = if (message.isSentByCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (message.isSentByCurrentUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    val alignment = if (message.isSentByCurrentUser) Alignment.CenterEnd else Alignment.CenterStart


    val bubbleShape = if (message.isSentByCurrentUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        contentAlignment = alignment
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(bubbleShape)
                .background(bubbleColor)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = if (message.isSentByCurrentUser) Alignment.End else Alignment.Start
        ) {
            Text(
                text = message.text,
                color = textColor,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = formatTimestamp(message.timestamp),
                    color = textColor.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                if (message.isSentByCurrentUser) {
                    Spacer(modifier = Modifier.width(4.dp))
                    DeliveryStatusIcon(status = message.deliveryStatus)
                }
            }
        }
    }
}

@Composable
private fun DeliveryStatusIcon(status: String) {
    val icon = when (status.lowercase()) {
        "read" -> Icons.Default.Done
        "delivered" -> Icons.Default.Done
        else -> Icons.Default.Done
    }
    val tint = when (status.lowercase()) {
        "read" -> Color(0xFF4FC3F7)
        else -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
    }
    Icon(
        imageVector = icon,
        contentDescription = "Delivery Status",
        tint = tint,
        modifier = Modifier.size(16.dp)
    )
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}