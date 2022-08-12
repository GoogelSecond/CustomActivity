package com.example.customactivity

import java.util.*

object TimeHelper {
    private const val TIME_ZONE = 5

    fun getCurrentTime(): TimeModel {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5"))
        val hour = calendar.get(Calendar.HOUR)

        val currentHour = if (hour > 12) hour - 12 else hour
        val currentMinute = calendar.get(Calendar.MINUTE)
        val currentSecond = calendar.get(Calendar.SECOND)

        return TimeModel(currentHour.toFloat() , currentMinute.toFloat(), currentSecond.toFloat())
    }
}