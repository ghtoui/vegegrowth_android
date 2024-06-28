package com.moritoui.vegegrowthapp.model

import com.moritoui.vegegrowthapp.R

enum class FilterStatus {
    All,
    End,
    Favorite,
    Default,
    Leaf,
    Flower,
    Other,
}

val filterStatusMap =
    mapOf(
        FilterStatus.End to VegeStatus.End,
        FilterStatus.Favorite to VegeStatus.Favorite,
        FilterStatus.Default to VegeStatus.Default,
        FilterStatus.Leaf to VegeCategory.Leaf,
        FilterStatus.Flower to VegeCategory.Flower,
        FilterStatus.Other to VegeCategory.Other
    )

fun FilterStatus.getText(): Int = when (this) {
    FilterStatus.All -> R.string.all_text
    FilterStatus.End -> R.string.growth_end_text
    FilterStatus.Favorite -> R.string.favorite_text
    FilterStatus.Default -> R.string.select_none
    FilterStatus.Leaf -> R.string.leaf_text
    FilterStatus.Flower -> R.string.flower_text
    FilterStatus.Other -> R.string.other_category_text
}
