package com.moritoui.vegegrowthapp.di

import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.SortStatus
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus
import kotlinx.coroutines.flow.StateFlow

data class FirstScreenUiState(
    val isOpenAddDialog: Boolean = false,
    val inputText: String = "",
    val selectCategory: VegeCategory = VegeCategory.None,
    val isAddAble: Boolean = false,
    val selectMenu: SelectMenu = SelectMenu.None,
    val selectStatus: VegeStatus = VegeStatus.Default,
    val sortStatus: SortStatus = SortStatus.All
)

interface FirstViewModel {
    val uiState: StateFlow<FirstScreenUiState>
    val vegeItemList: MutableList<VegeItem>

    fun closeDialog()

    fun openAddDialog()

    fun selectStatus()

    fun changeInputText(inputText: String)

    fun saveVegeItemListData()
    fun changeDeleteMode()

    fun changeEditMode()

    fun deleteItem(item: VegeItem, isDelete: Boolean)

    fun cancelMenu()

    fun selectCategory(selectCategory: VegeCategory)

    fun setSortItemList(sortStatus: SortStatus)
}
