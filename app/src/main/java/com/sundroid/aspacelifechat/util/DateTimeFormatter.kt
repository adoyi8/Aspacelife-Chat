package com.sundroid.aspacelifechat.util

import android.os.Build
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


fun formatMessageDate(timestamp: Long): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val zoneId = ZoneId.systemDefault()
        val messageDate = Instant.ofEpochMilli(timestamp).atZone(zoneId).toLocalDate()
        val today = LocalDate.now(zoneId)

        if (messageDate.isEqual(today)) {
            val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
            Instant.ofEpochMilli(timestamp).atZone(zoneId).format(timeFormatter)
        } else {
            val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm a", Locale.getDefault())
            Instant.ofEpochMilli(timestamp).atZone(zoneId).format(dateTimeFormatter)
        }
    } else {

        val messageDate = Date(timestamp)
        val today = Date(System.currentTimeMillis())

        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        if (SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(messageDate) == SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(today)) {
            timeFormat.format(messageDate)
        } else {
            val formattedDate = dateFormat.format(messageDate)
            val formattedTime = timeFormat.format(messageDate)
            "$formattedDate $formattedTime"
        }
    }
}
