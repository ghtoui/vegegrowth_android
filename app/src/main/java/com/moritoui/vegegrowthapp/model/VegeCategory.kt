package com.moritoui.vegegrowthapp.model

import com.moritoui.vegegrowthapp.R

enum class VegeCategory {
    None,
    Leaf,
    Flower,
    Question
}

object VegeCategoryMethod {
    fun getIcon(selectCategory: VegeCategory): Int? {
        return when (selectCategory) {
            VegeCategory.None -> null
            VegeCategory.Flower -> R.drawable.flower
            VegeCategory.Leaf -> R.drawable.potted_plant
            VegeCategory.Question -> R.drawable.question
        }
    }

    fun getText(selectCategory: VegeCategory): String {
        return when (selectCategory) {
            VegeCategory.None -> "カテゴリーを選択してください"
            VegeCategory.Flower -> "花"
            VegeCategory.Leaf -> "葉"
            VegeCategory.Question -> "分類無し"
        }
    }
}
