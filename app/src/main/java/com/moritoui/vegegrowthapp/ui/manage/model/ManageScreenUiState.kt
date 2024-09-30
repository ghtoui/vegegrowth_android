package com.moritoui.vegegrowthapp.ui.manage.model

import com.moritoui.vegegrowthapp.model.VegeItemDetail

data class ManageScreenUiState(
    val pagerCount: Int,
    val vegeRepositoryList: List<VegeItemDetail>,
    val isOpenImageBottomSheet: Boolean,
    val inputMemoText: String,
    val isOpenMemoEditorBottomSheet: Boolean,
    val isOpenDeleteDialog: Boolean,
    val selectedDeleteVegeDetail: VegeItemDetail?,
) {
    companion object {
        fun initialState(): ManageScreenUiState = ManageScreenUiState(
            pagerCount = 0,
            vegeRepositoryList = listOf(),
            isOpenImageBottomSheet = false,
            inputMemoText = "",
            isOpenMemoEditorBottomSheet = false,
            isOpenDeleteDialog = false,
            selectedDeleteVegeDetail = null,
        )
    }
}
