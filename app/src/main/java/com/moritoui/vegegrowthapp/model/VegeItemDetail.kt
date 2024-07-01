package com.moritoui.vegegrowthapp.model

import com.moritoui.vegegrowthapp.data.room.model.VegetableDetailEntity
import kotlinx.serialization.Serializable

// UUIDは文字列にしないとJsonに変換できない
@Serializable
data class VegeItemDetail(
    val id: Int = 0,
    val vegeItemId: Int = 0,
    val itemUuid: String = "",
    val uuid: String,
    val name: String,
    val size: Double,
    var memo: String,
    val date: String,
    val imagePath: String = "",
) {
    fun getDiffDatetime(baseDatetime: String): String {
        val dateFormatter = DateFormatter()
        val targetEpochTime = dateFormatter.stringToEpochTime(this.date)
        val baseEpochTime = dateFormatter.stringToEpochTime(baseDatetime)
        return dateFormatter.diffEpochTime(
            baseEpochTime = baseEpochTime,
            targetEpochTime = targetEpochTime
        )
    }
}

fun VegeItemDetail.toVegetableEntity(): VegetableDetailEntity = VegetableDetailEntity(
    id = id,
    note = memo,
    vegetableId = vegeItemId,
    size = size,
    name = name,
    imagePath = imagePath,
    date = date,
    uuid = uuid
)
