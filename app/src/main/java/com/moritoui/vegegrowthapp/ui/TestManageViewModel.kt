package com.moritoui.vegegrowthapp.ui

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemList
import com.moritoui.vegegrowthapp.model.VegetableRepository
import com.moritoui.vegegrowthapp.model.VegetableRepositoryList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TestManageViewModel(
    index: Int,
) : ManageViewModel {

    private var vegeItem: VegeItem
    private var vegeRepositoryList: MutableList<VegetableRepository>

    private val _uiState = MutableStateFlow(ManageUiState())
    override val uiState: StateFlow<ManageUiState> = _uiState.asStateFlow()

    init {
        this.vegeItem = VegeItemList.getVegeList()[index]
        this.vegeRepositoryList = VegetableRepositoryList.getVegeRepositoryList().toMutableList()
        updateState(
            pagerCount = vegeRepositoryList.size - 1,
            vegeRepositoryList = vegeRepositoryList,
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    private fun updateState(
        pagerCount: Int = _uiState.value.pagerCount,
        vegeRepositoryList: List<VegetableRepository> = _uiState.value.vegeRepositoryList,
        takePicList: List<Bitmap?> = _uiState.value.takePicList
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                pagerCount = pagerCount,
                vegeRepositoryList = vegeRepositoryList,
                takePicList = takePicList,
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    override suspend fun moveImage(index: Int) {
        _uiState.value.pagerState.animateScrollToPage(index)
    }
}
