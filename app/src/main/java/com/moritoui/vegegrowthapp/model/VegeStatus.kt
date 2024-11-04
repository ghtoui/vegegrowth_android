package com.moritoui.vegegrowthapp.model

import androidx.compose.ui.graphics.Color
import com.moritoui.vegegrowthapp.R

enum class VegeStatus(val value: Int) {
    Default(value = 0),
    Favorite(value = 1),
    End(value = 2),
}

object VegeStatusMethod {
    fun getIconId(vegeStatus: VegeStatus): Int = when (vegeStatus) {
        VegeStatus.Default -> R.drawable.ic_more_vert
        VegeStatus.Favorite -> R.drawable.ic_favorite
        VegeStatus.End -> R.drawable.ic_check
    }

    fun getIconTint(vegeStatus: VegeStatus): Color? = when (vegeStatus) {
        VegeStatus.Default -> Color.Transparent
        VegeStatus.Favorite -> Color.Red
        else -> null
    }

    fun getText(vegeStatus: VegeStatus): Int = when (vegeStatus) {
        VegeStatus.Default -> R.string.select_none
        VegeStatus.Favorite -> R.string.favorite_text
        VegeStatus.End -> R.string.growth_end_text
    }
}
