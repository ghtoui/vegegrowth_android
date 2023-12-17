package com.moritoui.vegegrowthapp.model

import kotlinx.serialization.Serializable

// UUIDは文字列にしないとJsonに変換できない
@Serializable
data class VegeItemDetail(
    val itemUuid: String,
    val uuid: String,
    val name: String,
    val size: Double,
    var memo: String,
    val date: String
) {
    fun getDiffDatetime(baseDatetime: String): String {
        val dateFormatter = DateFormatter()
        val targetEpochTime = dateFormatter.stringToEpochTime(this.date)
        val baseEpochTime = dateFormatter.stringToEpochTime(baseDatetime)
        return dateFormatter.diffEpochTime(baseEpochTime = baseEpochTime, targetEpochTime = targetEpochTime)
    }
}
