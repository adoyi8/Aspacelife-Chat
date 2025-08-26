package com.sundroid.aspacelifechat.auth.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        // Convert a List<String> to a JSON string
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        // Convert a JSON string back to a List<String>
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(data, listType)
    }
}