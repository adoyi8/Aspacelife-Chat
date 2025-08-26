package com.sundroid.aspacelifechat.auth.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sundroid.aspacelifechat.auth.domain.model.Conversation
import com.sundroid.aspacelifechat.auth.domain.model.Message
import com.sundroid.aspacelifechat.auth.util.Converters

@Database(entities = [Conversation::class, Message::class], version = 3)
@TypeConverters(Converters::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
}