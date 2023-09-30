package com.moritoui.vegegrowthapp.model

import com.moritoui.vegegrowthapp.R

enum class SortStatus {
    All,
    End,
    Favorite,
    Default,
    Leaf,
    Flower,
    Other
}

val sortStatusMap = mapOf(
    SortStatus.End to VegeStatus.End,
    SortStatus.Favorite to VegeStatus.Favorite,
    SortStatus.Default to VegeStatus.Default,
    SortStatus.Leaf to VegeCategory.Leaf,
    SortStatus.Flower to VegeCategory.Flower,
    SortStatus.Other to VegeCategory.Other
)

fun SortStatus.getText(): Int {
    return when (this) {
        SortStatus.All -> R.string.all_text
        SortStatus.End -> R.string.growth_end_text
        SortStatus.Favorite -> R.string.favorite_text
        SortStatus.Default -> R.string.select_none
        SortStatus.Leaf -> R.string.leaf_text
        SortStatus.Flower -> R.string.flower_text
        SortStatus.Other -> R.string.other_category_text
    }
}
