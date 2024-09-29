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
    val selectedItem: VegeItem?,
    val selectedFolder: VegetableFolderEntity?,
    val isLoading: Boolean,
    val isOpenFolderMoveBottomSheet: Boolean,
    val isOpenDrawer: Boolean,
    val isRegisterSelectDate: Boolean
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
            selectedItem = null,
            isLoading = false,
            selectedFolder = null,
            isOpenFolderMoveBottomSheet = false,
            isOpenDrawer = false,
            isRegisterSelectDate = false,
        )
    }
}
