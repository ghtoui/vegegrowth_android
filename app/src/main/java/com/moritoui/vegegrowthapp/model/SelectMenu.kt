package com.moritoui.vegegrowthapp.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class SelectMenu {
    None,
    Delete,
    Edit
}

object SelectMenuMethod {
    fun getIcon(selectMenu: SelectMenu): ImageVector {
        return when (selectMenu) {
            SelectMenu.None -> Icons.Filled.MoreVert
            SelectMenu.Delete -> Icons.Filled.Delete
            SelectMenu.Edit -> Icons.Filled.Edit
        }
    }

    fun getIconTint(selectMenu: SelectMenu): Color? {
        return when (selectMenu) {
            SelectMenu.Delete -> Color.Red
            else -> null
        }
    }
}
