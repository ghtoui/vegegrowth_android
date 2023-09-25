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
    val takePicList: List<Bitmap?> = mutableListOf(),
)
interface ManageViewModel {
    val uiState: StateFlow<ManageUiState>

    suspend fun moveImage(index: Int) {

    }
}
