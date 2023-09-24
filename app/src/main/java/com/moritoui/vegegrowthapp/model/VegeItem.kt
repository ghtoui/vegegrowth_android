package com.moritoui.vegegrowthapp.model

data class VegeItem(
    val name: String,
    val category: VegeCategory,
    val uuid: String
)

enum class VegeCategory {
    Leaf,
    Flower
}
