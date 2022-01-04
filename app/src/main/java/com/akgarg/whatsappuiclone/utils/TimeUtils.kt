package com.akgarg.whatsappuiclone.utils

import com.akgarg.whatsappuiclone.models.firebase.ChatMessageTime
import java.time.LocalDateTime

class TimeUtils {

    companion object {

        fun getMessageDateTime(): ChatMessageTime {
            val localDateTime = LocalDateTime.now()

            val hour = localDateTime.hour
            val minutes = localDateTime.minute
            val day = localDateTime.dayOfMonth
            val month = localDateTime.monthValue
            val year = localDateTime.year

            return ChatMessageTime(
                hour = hour,
                minutes = minutes,
                day = day,
                month = month,
                year = year
            )
        }


        fun getCurrentDateAndTime(): String {
            val localDateTime = LocalDateTime.now()

            val hour = localDateTime.hour
            val minutes = localDateTime.minute
            val seconds = localDateTime.second
            val day = localDateTime.dayOfMonth
            val month = localDateTime.monthValue
            val year = localDateTime.year

            return "$day-$month-$year, $hour:$minutes:$seconds"
        }

    }

}