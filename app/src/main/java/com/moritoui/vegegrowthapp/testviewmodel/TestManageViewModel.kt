package com.moritoui.vegegrowthapp.testviewmodel

import android.graphics.Bitmap
import com.moritoui.vegegrowthapp.di.ManageUiState
import com.moritoui.vegegrowthapp.di.ManageViewModel
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.VegeItemList
import com.moritoui.vegegrowthapp.model.VegetableRepositoryList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TestManageViewModel(
    index: Int,
) : ManageViewModel {

    private var vegeItem: VegeItem
    private var vegeRepositoryList: MutableList<VegeItemDetail>

    private val _uiState = MutableStateFlow(ManageUiState())
    override val uiState: StateFlow<ManageUiState> = _uiState.asStateFlow()

    override var takePicList: List<Bitmap?>
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun changeOpenMemoEditorBottomSheet(index: Int) { }

    override fun saveEditMemo(index: Int) { }

    init {
        this.vegeItem = VegeItemList.getVegeList()[index]
        this.vegeRepositoryList = VegetableRepositoryList.getVegeRepositoryList().toMutableList()
        updateState(
            pagerCount = vegeRepositoryList.size - 1,
            vegeRepositoryList = vegeRepositoryList,
        )
    }

    private fun updateState(
        pagerCount: Int = _uiState.value.pagerCount,
        vegeRepositoryList: List<VegeItemDetail> = _uiState.value.vegeRepositoryList,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                pagerCount = pagerCount,
                vegeRepositoryList = vegeRepositoryList,
            )
        }
    }
}
