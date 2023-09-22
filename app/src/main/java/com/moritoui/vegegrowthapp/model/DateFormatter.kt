package com.moritoui.vegegrowthapp.model

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateFormatter {
    private val customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun stringToDate(stringDateTime: String): LocalDateTime {
        return LocalDateTime.parse(stringDateTime, customFormatter)
    }

    fun dateToString(dateTime: LocalDateTime): String {
        return dateTime.format(customFormatter)
    }

    fun stringToEpochTime(stringDateTime: String): Long {
        val datetime = stringToDate(stringDateTime)
        val instant = datetime.atZone(ZoneId.systemDefault()).toInstant()
        return instant.toEpochMilli()
    }
}
