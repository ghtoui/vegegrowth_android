package com.moritoui.vegegrowthapp.model

import com.moritoui.vegegrowthapp.data.room.model.VegetableEntity
import kotlinx.serialization.Serializable

@Serializable
data class VegeItem(
    val id: Int = 0,
    val name: String,
    val category: VegeCategory,
    val uuid: String = "",
    var status: VegeStatus,
)

fun VegeItem.toVegeTableEntity(): VegetableEntity =
    VegetableEntity(
        id = id,
        vegetableState = status,
        vegetableName = name,
        vegetableCategory = category,
    )
