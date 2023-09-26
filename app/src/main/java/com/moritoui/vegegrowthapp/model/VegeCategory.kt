package com.moritoui.vegegrowthapp.model

import androidx.compose.ui.graphics.Color
import com.moritoui.vegegrowthapp.R

enum class VegeCategory {
    None,
    Leaf,
    Flower,
    Other
}

object VegeCategoryMethod {
    fun getIcon(selectCategory: VegeCategory): Int? {
        return when (selectCategory) {
            VegeCategory.None -> null
            VegeCategory.Flower -> R.drawable.flower
            VegeCategory.Leaf -> R.drawable.potted_plant
            VegeCategory.Other -> R.drawable.pending
        }
    }

    fun getText(selectCategory: VegeCategory): String {
        return when (selectCategory) {
            VegeCategory.None -> "カテゴリーを選択してください"
            VegeCategory.Flower -> "花"
            VegeCategory.Leaf -> "葉"
            VegeCategory.Other -> "分類無し"
        }
    }

    fun getTint(selectCategory: VegeCategory): Color {
        return when (selectCategory) {
            VegeCategory.None -> Color.White
            VegeCategory.Flower -> Color.Magenta
            VegeCategory.Leaf -> Color.Green
            VegeCategory.Other -> Color.DarkGray
        }
    }
}
