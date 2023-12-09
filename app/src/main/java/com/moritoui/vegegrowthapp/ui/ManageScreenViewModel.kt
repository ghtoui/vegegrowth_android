package com.moritoui.vegegrowthapp.ui

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.moritoui.vegegrowthapp.di.ManageUiState
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.VegetableRepositoryFileManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ManageScreenViewModel @Inject constructor(
    private val fileManager: VegetableRepositoryFileManager
) : ViewModel() {
    private var vegeItem: VegeItem
    private var vegeRepositoryList: MutableList<VegeItemDetail>

    var takePicList: List<Bitmap?> = emptyList()

    private val _uiState = MutableStateFlow(ManageUiState())
    val uiState: StateFlow<ManageUiState> = _uiState.asStateFlow()

    init {
        this.vegeItem = fileManager.getVegeItem()
        this.vegeRepositoryList = fileManager.readVegeRepositoryList(fileManager.readJsonData(vegeItem.uuid))
        updateState(
            pagerCount = vegeRepositoryList.size,
            vegeRepositoryList = vegeRepositoryList
        )
        this.takePicList = fileManager.getImageList().toMutableStateList()
    }

    @OptIn(ExperimentalFoundationApi::class)
    private fun updateState(
        pagerCount: Int = _uiState.value.pagerCount,
        vegeRepositoryList: List<VegeItemDetail> = _uiState.value.vegeRepositoryList,
        isOpenImageBottomSheet: Boolean = _uiState.value.isOpenImageBottomSheet,
        inputMemoText: String = _uiState.value.inputMemoText,
        isOpenMemoEditorBottomSheet: Boolean = _uiState.value.isOpenMemoEditorBottomSheet
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                pagerCount = pagerCount,
                vegeRepositoryList = vegeRepositoryList,
                isOpenImageBottomSheet = isOpenImageBottomSheet,
                inputMemoText = inputMemoText,
                isOpenMemoEditorBottomSheet = isOpenMemoEditorBottomSheet
            )
        }
    }

    fun changeOpenImageBottomSheet() {
        updateState(
            isOpenImageBottomSheet = !_uiState.value.isOpenImageBottomSheet
        )
        this.takePicList = fileManager.getImageList().toMutableStateList()
    }

    fun cancelEditMemo() {
        updateState(
            inputMemoText = "",
            isOpenMemoEditorBottomSheet = false
        )
    }

    fun changeMemoText(inputText: String) {
        updateState(inputMemoText = inputText)
    }

    fun saveEditMemo(index: Int) {
        vegeRepositoryList[index].memo = _uiState.value.inputMemoText
        fileManager.saveVegeRepository(vegeRepositoryList = vegeRepositoryList)
        updateState(
            inputMemoText = "",
            isOpenMemoEditorBottomSheet = false
        )
    }

    fun changeOpenMemoEditorBottomSheet(index: Int) {
        if (!_uiState.value.isOpenMemoEditorBottomSheet) {
            updateState(inputMemoText = vegeRepositoryList[index].memo)
        }
        updateState(
            isOpenMemoEditorBottomSheet = !_uiState.value.isOpenMemoEditorBottomSheet
        )
    }
}
