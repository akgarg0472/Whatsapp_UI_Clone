package com.akgarg.whatsappuiclone.models.firebase

@Suppress("unused")
class ChatMessageTime(
    private val hour: Int,
    private val minutes: Int,
    private val day: Int,
    private val month: Int,
    private val year: Int
) {

    fun getHour() = this.hour

    fun getMinutes() = this.minutes

    fun getDay() = this.day

    fun getMonth() = this.month

    fun getYear() = this.year

    override fun toString(): String {
        return "{ $hour:$minutes, $day-$month-$year }"
    }

}