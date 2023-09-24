package com.moritoui.vegegrowthapp.model

import kotlinx.serialization.Serializable

@Serializable
data class VegeItem(
    val name: String,
    val category: VegeCategory,
    val uuid: String
)

enum class VegeCategory {
    Leaf,
    Flower,
    None
}
