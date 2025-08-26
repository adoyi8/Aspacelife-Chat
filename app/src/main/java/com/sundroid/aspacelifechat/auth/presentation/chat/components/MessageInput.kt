package com.sundroid.aspacelifechat.auth.presentation.chat.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Surface(
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { /* TODO: Handle attachments */ }) {
                Icon(Icons.Default.Add, contentDescription = "Attach File")
            }

            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type your message...") },
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                trailingIcon = {

                }
            )

            Spacer(modifier = Modifier.width(8.dp))


            val isSendEnabled = value.isNotBlank()
            val buttonColor = if (isSendEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(buttonColor)
            ) {
                IconButton(
                    onClick = { if (isSendEnabled) onSendClick() else { /* TODO: Handle mic */ } },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = if (isSendEnabled) Icons.Default.Send else Icons.Default.Add,
                        contentDescription = if (isSendEnabled) "Send Message" else "Record Voice Message",
                        tint = Color.White
                    )
                }
            }
        }
    }
}