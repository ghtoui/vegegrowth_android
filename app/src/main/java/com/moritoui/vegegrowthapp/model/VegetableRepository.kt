package com.moritoui.vegegrowthapp.model

import kotlinx.serialization.Serializable

// UUIDは文字列にしないとJsonに変換できない
@Serializable
data class VegetableRepository(
    val itemUuid: String,
    val uuid: String,
    val name: String,
    val size: Double,
    var memo: String,
)