package com.moritoui.vegegrowthapp.ui.folder.model

import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import com.moritoui.vegegrowthapp.model.FilterStatus
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.ui.home.model.AddDialogType

data class FolderScreenUiState(
    val selectMenu: SelectMenu,
    val isLoading: Boolean,
    val selectedFolder: VegetableFolderEntity?,
    val filterStatus: FilterStatus,
    val isOpenDeleteDialog: Boolean,
    val selectedItem: VegeItem?,
    val openAddDialogType: AddDialogType,
    val selectCategory: VegeCategory,
    val inputText: String,
    val isAddAble: Boolean,
    val isOpenFolderMoveBottomSheet: Boolean,
) {
    companion object {
        fun initial(): FolderScreenUiState = FolderScreenUiState(
            selectMenu = SelectMenu.None,
            isLoading = false,
            selectedFolder = null,
            filterStatus = FilterStatus.All,
            isOpenDeleteDialog = false,
            selectedItem = null,
            openAddDialogType = AddDialogType.NotOpenDialog,
            selectCategory = VegeCategory.None,
            inputText = "",
            isAddAble = false,
            isOpenFolderMoveBottomSheet = false
        )
    }
}
