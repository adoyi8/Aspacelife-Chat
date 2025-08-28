package com.sundroid.aspacelifechat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sundroid.aspacelifechat.domain.model.Conversation
import com.sundroid.aspacelifechat.domain.model.Message
import com.sundroid.aspacelifechat.util.Converters

@Database(entities = [Conversation::class, Message::class], version = 3)
@TypeConverters(Converters::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
}