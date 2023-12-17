package com.moritoui.vegegrowthapp.testviewmodel

import com.moritoui.vegegrowthapp.di.FirstViewModel
import com.moritoui.vegegrowthapp.model.SortStatus
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemList
import com.moritoui.vegegrowthapp.ui.FirstScreenUiState
import kotlinx.coroutines.flow.StateFlow

class TestFirstViewModel : FirstViewModel {
    override val uiState: StateFlow<FirstScreenUiState>
        get() = uiState
    override val vegeItemList: MutableList<VegeItem> = VegeItemList.getVegeList().toMutableList()

    override fun closeDialog() { }

    override fun openAddDialog() { }

    override fun selectStatus() { }

    override fun changeInputText(inputText: String) { }

    override fun saveVegeItemListData() { }

    override fun changeDeleteMode() { }

    override fun changeEditMode() { }

    override fun deleteItem(item: VegeItem, isDelete: Boolean) { }

    override fun cancelMenu() { }

    override fun selectCategory(selectCategory: VegeCategory) { }

    override fun setSortItemList(sortStatus: SortStatus) { }
}
