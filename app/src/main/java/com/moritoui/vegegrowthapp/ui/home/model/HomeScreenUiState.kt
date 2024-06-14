package com.moritoui.vegegrowthapp.ui.home.model

import com.moritoui.vegegrowthapp.model.FilterStatus
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus

data class HomeScreenUiState(
    val isOpenAddDialog: Boolean,
    val inputText: String,
    val selectCategory: VegeCategory,
    val isAddAble: Boolean,
    val selectMenu: SelectMenu,
    val selectStatus: VegeStatus,
    val filterStatus: FilterStatus,
    val vegetables: List<VegeItem>,
) {
    companion object {
        fun initialState() =
            HomeScreenUiState(
                isOpenAddDialog = false,
                inputText = "",
                selectCategory = VegeCategory.None,
                isAddAble = false,
                selectMenu = SelectMenu.None,
                selectStatus = VegeStatus.Default,
                filterStatus = FilterStatus.All,
                vegetables = emptyList(),
            )
    }
}