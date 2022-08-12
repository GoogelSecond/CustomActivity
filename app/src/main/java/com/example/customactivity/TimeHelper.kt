package com.example.customactivity

object TimeHelper {
    private const val TIME_ZONE = 5

    fun getCurrentTime(): TimeModel {
        val currentTimeMills = System.currentTimeMillis()

        val totalSeconds = currentTimeMills / 1000
        val currentSecond = (totalSeconds % 60).toInt()

        val totalMinutes = totalSeconds / 60
        val currentMinute = (totalMinutes % 60).toInt()

        val totalHours = totalMinutes / 60
        val currentHour = (totalHours % 12).toInt()

        return TimeModel(currentHour + TIME_ZONE, currentMinute, currentSecond)
    }
}