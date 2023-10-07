package com.moritoui.vegegrowthapp.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.toMutableStateList
import com.moritoui.vegegrowthapp.di.ManageUiState
import com.moritoui.vegegrowthapp.di.ManageViewModel
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegetableRepository
import com.moritoui.vegegrowthapp.model.VegetableRepositoryFileManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalFoundationApi::class)
class ManageScreenViewModel(
    index: Int,
    sortText: String,
    applicationContext: Context
) : ManageViewModel {
    private val fileManager: VegetableRepositoryFileManager
    private var vegeItem: VegeItem
    private var vegeRepositoryList: MutableList<VegetableRepository>

    override var takePicList: List<Bitmap?> = emptyList()

    private val _uiState = MutableStateFlow(ManageUiState())
    override val uiState: StateFlow<ManageUiState> = _uiState.asStateFlow()

    init {
        this.fileManager = VegetableRepositoryFileManager(
            index = index,
            sortText = sortText,
            applicationContext = applicationContext
        )
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
        vegeRepositoryList: List<VegetableRepository> = _uiState.value.vegeRepositoryList,
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

    override fun changeOpenImageBottomSheet() {
        updateState(
            isOpenImageBottomSheet = !_uiState.value.isOpenImageBottomSheet
        )
        this.takePicList = fileManager.getImageList().toMutableStateList()
    }

    override fun cancelEditMemo() {
        updateState(
            inputMemoText = "",
            isOpenMemoEditorBottomSheet = false
        )
    }

    override fun changeMemoText(inputText: String) {
        updateState(inputMemoText = inputText)
    }

    override fun saveEditMemo(index: Int) {
        vegeRepositoryList[index].memo = _uiState.value.inputMemoText
        fileManager.saveVegeRepository(vegeRepositoryList = vegeRepositoryList)
        updateState(
            inputMemoText = "",
            isOpenMemoEditorBottomSheet = false
        )
    }

    override fun changeOpenMemoEditorBottomSheet(index: Int) {
        if (!_uiState.value.isOpenMemoEditorBottomSheet) {
            updateState(inputMemoText = vegeRepositoryList[index].memo)
        }
        updateState(
            isOpenMemoEditorBottomSheet = !_uiState.value.isOpenMemoEditorBottomSheet
        )
    }
}
