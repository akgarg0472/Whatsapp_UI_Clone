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


        fun beautifyChatMessageTime(
            hour: Int,
            minutes: Int,
            day: Int,
            month: Int,
            year: Int
        ): String {
            val h = when {
                hour < 10 -> {
                    "0$hour"
                }
                hour <= 12 -> {
                    hour
                }
                else -> {
                    "${hour - 12}"
                }
            }

            val m = if (minutes < 10) {
                "0$minutes"
            } else {
                minutes
            }

            val amPm = if (hour < 12) {
                "AM"
            } else {
                "PM"
            }

            val d = if (day < 10) {
                "0$day"
            } else {
                day
            }

            val mo = if (month < 10) {
                "0$month"
            } else {
                month
            }

            return "$d/$mo/$year, $h:$m $amPm"
        }

    }

}