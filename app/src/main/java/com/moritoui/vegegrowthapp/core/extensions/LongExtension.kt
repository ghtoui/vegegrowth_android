package com.moritoui.vegegrowthapp.core.extensions

import com.moritoui.vegegrowthapp.model.DateFormatter
import java.text.SimpleDateFormat
import java.util.Date

fun Long.toDateString(): String {
    val date = Date(this)
    val format = SimpleDateFormat(DateFormatter.CUSTOM_PATTERN)
    return format.format(date)
}
