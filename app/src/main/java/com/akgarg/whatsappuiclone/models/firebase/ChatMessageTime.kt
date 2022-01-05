package com.akgarg.whatsappuiclone.models.firebase

import com.akgarg.whatsappuiclone.utils.TimeUtils

@Suppress("unused")
class ChatMessageTime {

    private var hour: Int = 0
    private var minutes: Int = 0
    private var day: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    constructor()

    constructor(
        hour: Int,
        minutes: Int,
        day: Int,
        month: Int,
        year: Int
    ) {
        this.hour = hour
        this.minutes = minutes
        this.day = day
        this.month = month
        this.year = year
    }

    fun getHour() = this.hour

    fun getMinutes() = this.minutes

    fun getDay() = this.day

    fun getMonth() = this.month

    fun getYear() = this.year

    override fun toString(): String {
        return TimeUtils.beautifyChatMessageTime(hour, minutes, day, month, year)
    }

}