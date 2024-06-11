package com.moritoui.vegegrowthapp.di

import android.graphics.Bitmap
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import kotlinx.coroutines.flow.StateFlow

data class ManageUiState(
    val pagerCount: Int = 0,
    val vegeRepositoryList: List<VegeItemDetail> = listOf(),
    val isOpenImageBottomSheet: Boolean = false,
    val inputMemoText: String = "",
    val isOpenMemoEditorBottomSheet: Boolean = false
)
interface ManageViewModel {
    val uiState: StateFlow<ManageUiState>

    var takePicList: List<Bitmap?>

    fun changeOpenImageBottomSheet() { }

    fun changeMemoText(inputText: String) { }

    fun cancelEditMemo() { }

    fun changeOpenMemoEditorBottomSheet(index: Int)
    fun saveEditMemo(index: Int)
}
