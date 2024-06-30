package com.moritoui.vegegrowthapp.model

import androidx.compose.ui.graphics.Color
import com.moritoui.vegegrowthapp.R

enum class SelectMenu {
    MoveFolder,
    None,
    Delete,
    Edit,
}

object SelectMenuMethod {
    fun getIcon(selectMenu: SelectMenu): Int = when (selectMenu) {
        SelectMenu.MoveFolder -> R.drawable.ic_folder_move
        SelectMenu.None -> R.drawable.ic_more_vert
        SelectMenu.Delete -> R.drawable.ic_delete
        SelectMenu.Edit -> R.drawable.ic_edit
    }

    fun getIconTint(selectMenu: SelectMenu): Color? = when (selectMenu) {
        SelectMenu.Delete -> Color.Red
        else -> null
    }
}
