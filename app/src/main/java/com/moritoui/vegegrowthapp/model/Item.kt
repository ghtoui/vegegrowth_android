package com.moritoui.vegegrowthapp.model

data class VegeItem(
    val name: String,
    val category: VegeCategory
)

enum class VegeCategory {
    leaf,
    flower
}
