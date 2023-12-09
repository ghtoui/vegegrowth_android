package com.moritoui.vegegrowthapp.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.moritoui.vegegrowthapp.di.ManageUiState
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.usecases.GetTakePictureListUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemDetailListUseCase
import com.moritoui.vegegrowthapp.usecases.SaveVegeDetailMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ManageScreenViewModel @Inject constructor(
    getVegeItemDetailListUseCase: GetVegeItemDetailListUseCase,
    private val saveVegeDetailMemoUseCase: SaveVegeDetailMemoUseCase,
    private val getTakePictureListUseCase: GetTakePictureListUseCase
) : ViewModel() {
    private var vegeItemDetailList: MutableList<VegeItemDetail> = getVegeItemDetailListUseCase()

    var takePicList: List<Bitmap?> = getTakePictureListUseCase()

    private val _uiState = MutableStateFlow(ManageUiState())
    val uiState: StateFlow<ManageUiState> = _uiState.asStateFlow()

    init {
        updateState(
            pagerCount = vegeItemDetailList.size,
            vegeRepositoryList = vegeItemDetailList
        )
    }

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
        saveVegeDetailMemoUseCase(index = index, memo = _uiState.value.inputMemoText)
        updateState(
            inputMemoText = "",
            isOpenMemoEditorBottomSheet = false
        )
    }

    fun changeOpenMemoEditorBottomSheet(index: Int) {
        if (!_uiState.value.isOpenMemoEditorBottomSheet) {
            updateState(inputMemoText = vegeItemDetailList[index].memo)
        }
        updateState(
            isOpenMemoEditorBottomSheet = !_uiState.value.isOpenMemoEditorBottomSheet
        )
    }
}
