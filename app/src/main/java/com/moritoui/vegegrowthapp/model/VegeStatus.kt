package com.moritoui.vegegrowthapp.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.moritoui.vegegrowthapp.R

enum class VegeStatus {
    Default,
    Favorite,
    End,
}

object VegeStatusMethod {
    fun getIcon(vegeStatus: VegeStatus): ImageVector = when (vegeStatus) {
        VegeStatus.Default -> Icons.Filled.MoreVert
        VegeStatus.Favorite -> Icons.Filled.Favorite
        VegeStatus.End -> Icons.Filled.Check
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
