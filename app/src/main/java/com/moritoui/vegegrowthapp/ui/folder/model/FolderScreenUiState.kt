package com.moritoui.vegegrowthapp.ui.folder.model

import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import com.moritoui.vegegrowthapp.model.FilterStatus
import com.moritoui.vegegrowthapp.model.SelectMenu

data class FolderScreenUiState(
    val selectMenu: SelectMenu,
    val isLoading: Boolean,
    val selectedFolder: VegetableFolderEntity?,
    val filterStatus: FilterStatus
) {
    companion object {
        fun initial(): FolderScreenUiState = FolderScreenUiState(
            selectMenu = SelectMenu.None,
            isLoading = false,
            selectedFolder = null,
            filterStatus = FilterStatus.All
        )
    }
}
