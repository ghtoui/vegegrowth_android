package com.moritoui.vegegrowthapp.ui

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import com.moritoui.vegegrowthapp.model.VegetableRepository
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalFoundationApi::class)
data class ManageUiState(
    val pagerCount: Int = 0,
    val vegeRepositoryList: List<VegetableRepository> = listOf(),
    val pagerState: PagerState = PagerState(),
    val isOpenImageBottomSheet: Boolean = false,
    val inputMemoText: String = "",
    val isOpenMemoEditorBottomSheet: Boolean = false
)
interface ManageViewModel {
    val uiState: StateFlow<ManageUiState>

    var takePicList: List<Bitmap?>

    suspend fun moveImage(index: Int) { }

    fun changeOpenImageBottomSheet() { }
    fun changeOpenMemoEditorBottomSheet() { }

    fun changeMemoText(inputText: String) { }

    fun cancelEditMemo() { }

    fun saveEditMemo() { }
}
