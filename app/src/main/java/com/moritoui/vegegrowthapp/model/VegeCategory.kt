package com.moritoui.vegegrowthapp.model

import androidx.compose.ui.graphics.Color
import com.moritoui.vegegrowthapp.R

enum class VegeCategory {
    None,
    Leaf,
    Flower,
    Other,
}

fun VegeCategory.getIcon(): Int? = when (this) {
    VegeCategory.None -> null
    VegeCategory.Flower -> R.drawable.flower
    VegeCategory.Leaf -> R.drawable.potted_plant
    VegeCategory.Other -> R.drawable.pending
}

fun VegeCategory.getText(): Int = when (this) {
    VegeCategory.None -> R.string.vege_category_none_text
    VegeCategory.Flower -> R.string.flower_text
    VegeCategory.Leaf -> R.string.leaf_text
    VegeCategory.Other -> R.string.other_category_text
}

fun VegeCategory.getTint(otherColor: Color): Color = when (this) {
    VegeCategory.None -> Color.White
    VegeCategory.Flower -> Color.Magenta
    VegeCategory.Leaf -> Color.Green
    VegeCategory.Other -> otherColor
}
