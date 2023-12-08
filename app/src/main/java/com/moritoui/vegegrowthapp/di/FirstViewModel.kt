package com.moritoui.vegegrowthapp.di

import com.moritoui.vegegrowthapp.model.SortStatus
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.sortStatusMap
import com.moritoui.vegegrowthapp.ui.FirstScreenUiState
import kotlinx.coroutines.flow.StateFlow


interface FirstViewModel {
    val uiState: StateFlow<FirstScreenUiState>
    val vegeItemList: MutableList<VegeItem>

    // 完全なprotectedではないが、これでprotectedの実装ができる
    fun checkInputText(inputText: String): Boolean {
        return when (inputText) {
            "" -> false
            else -> true
        }
    }

    fun sortList(sortStatus: SortStatus, itemList: List<VegeItem>): MutableList<VegeItem> {
        return when (sortStatus) {
            SortStatus.All -> itemList.toMutableList()
            else -> {
                itemList.filter { item ->
                    item.status == sortStatusMap[sortStatus] || item.category == sortStatusMap[sortStatus]
                }.toMutableList()
            }
        }
    }

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
