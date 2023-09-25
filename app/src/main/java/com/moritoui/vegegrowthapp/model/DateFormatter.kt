package com.moritoui.vegegrowthapp.model

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateFormatter {
    private val customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    private fun stringToDate(stringDateTime: String): LocalDateTime {
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

    fun diffEpochTime(targetEpochTime: Long, baseEpochTime: Long): String {
        val diffEpochTime = targetEpochTime - baseEpochTime
        // 差分の秒数 * 1000 で出るので、1000で割ってから日数で割る
        return (diffEpochTime / 1000 / 24 / 60 / 60).toString()
    }
}
