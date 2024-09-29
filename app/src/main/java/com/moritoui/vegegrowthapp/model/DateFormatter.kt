package com.moritoui.vegegrowthapp.model

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DateFormatter {
    private val customFormatter = DateTimeFormatter.ofPattern(CUSTOM_PATTERN)

    private fun stringToDate(stringDateTime: String): LocalDateTime = LocalDateTime.parse(stringDateTime, customFormatter)

    fun dateToString(dateTime: LocalDateTime): String = dateTime.format(customFormatter)

    fun dateToString(dateTime: ZonedDateTime): String = dateTime.format(customFormatter)

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

    companion object {
        const val CUSTOM_PATTERN = "yyyy-MM-dd HH:mm:ss"
    }
}
