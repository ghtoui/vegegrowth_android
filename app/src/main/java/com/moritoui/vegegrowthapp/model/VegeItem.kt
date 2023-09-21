package com.moritoui.vegegrowthapp.model

import java.util.UUID

data class VegeItem(
    val name: String,
    val category: VegeCategory,
    val uuid: UUID
)

enum class VegeCategory {
    Leaf,
    Flower
}
