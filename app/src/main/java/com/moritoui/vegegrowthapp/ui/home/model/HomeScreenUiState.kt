package com.moritoui.vegegrowthapp.ui.home.model

import com.moritoui.vegegrowthapp.data.room.model.VegetableFolderEntity
import com.moritoui.vegegrowthapp.model.FilterStatus
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus

data class HomeScreenUiState(
    val openAddDialogType: AddDialogType,
    val isOpenAddFolderDialog: Boolean,
    val isOpenDeleteDialog: Boolean,
    val inputText: String,
    val selectCategory: VegeCategory,
    val isAddAble: Boolean,
    val selectMenu: SelectMenu,
    val selectStatus: VegeStatus,
    val filterStatus: FilterStatus,
    val targetDeleteItem: VegeItem?,
    val targetDeleteFolder: VegetableFolderEntity?,
    val isLoading: Boolean,
) {
    companion object {
        fun initialState() = HomeScreenUiState(
            openAddDialogType = AddDialogType.NotOpenDialog,
            isOpenAddFolderDialog = false,
            isOpenDeleteDialog = false,
            inputText = "",
            selectCategory = VegeCategory.None,
            isAddAble = false,
            selectMenu = SelectMenu.None,
            selectStatus = VegeStatus.Default,
            filterStatus = FilterStatus.All,
            targetDeleteItem = null,
            isLoading = false,
            targetDeleteFolder = null,
        )
    }
}
