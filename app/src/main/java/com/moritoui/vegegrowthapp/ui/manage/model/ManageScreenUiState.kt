package com.moritoui.vegegrowthapp.ui.manage.model

import com.moritoui.vegegrowthapp.model.VegeItemDetail

data class ManageScreenUiState(
    val pagerCount: Int = 0,
    val vegeRepositoryList: List<VegeItemDetail> = listOf(),
    val isOpenImageBottomSheet: Boolean = false,
    val inputMemoText: String = "",
    val isOpenMemoEditorBottomSheet: Boolean = false,
) {
    companion object {
        fun initialState(): ManageScreenUiState =
            ManageScreenUiState(
                pagerCount = 0,
                vegeRepositoryList = listOf(),
                isOpenImageBottomSheet = false,
                inputMemoText = "",
                isOpenMemoEditorBottomSheet = false,
            )
    }
}
