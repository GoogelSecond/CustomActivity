package com.example.customactivity

import java.util.*

object TimeHelper {
    private const val TIME_ZONE = "GMT+5"

    fun getCurrentTime(): TimeModel {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE))
        val hour = calendar.get(Calendar.HOUR)

        val currentHour = if (hour > 12) hour - 12 else hour
        val currentMinute = calendar.get(Calendar.MINUTE)
        val currentSecond = calendar.get(Calendar.SECOND)
        val currentMillSecond = calendar.get(Calendar.MILLISECOND)

        return TimeModel(
            hours = currentHour.toFloat(),
            minutes = currentMinute.toFloat(),
            seconds = currentSecond.toFloat(),
            millSeconds = currentMillSecond.toFloat()
        )
    }
}