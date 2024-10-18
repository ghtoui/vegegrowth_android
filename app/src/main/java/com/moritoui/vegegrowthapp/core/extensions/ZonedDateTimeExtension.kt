package com.moritoui.vegegrowthapp.core.extensions

import com.moritoui.vegegrowthapp.model.DateFormatter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * 表示用の年月日を取得する
 */
fun ZonedDateTime.toDateFormat(): String = format(
    DateTimeFormatter.ofPattern(DateFormatter.CUSTOM_DATE_PATTERN)
)
