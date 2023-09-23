package com.moritoui.vegegrowthapp.ui

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moritoui.vegegrowthapp.model.FileManager
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegetableRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalFoundationApi::class)
data class ManageScreenUiState(
    val pagerCount: Int = 0,
    val vegeRepositoryList: List<VegetableRepository> = listOf(),
    val pagerState: PagerState = PagerState()
)

class ManageScreenViewModel constructor(
    index: Int,
    applicationContext: Context
    ) : ViewModel() {
    private val fileManager: FileManager
    private var vegeItem: VegeItem
    private var vegeRepositoryList: MutableList<VegetableRepository>

    private val _uiState = MutableStateFlow(ManageScreenUiState())
    val uiState: StateFlow<ManageScreenUiState> = _uiState.asStateFlow()

    init {
        this.fileManager = FileManager(
            index = index,
            applicationContext = applicationContext
        )
        this.vegeItem = fileManager.getVegeItem()
        this.vegeRepositoryList = fileManager.parseFromJson(fileManager.readJsonData())
        updateState(
            pagerCount = vegeRepositoryList.size - 1,
            vegeRepositoryList = vegeRepositoryList
            )
    }

    @OptIn(ExperimentalFoundationApi::class)
    private fun updateState(
        pagerCount: Int = _uiState.value.pagerCount,
        vegeRepositoryList: List<VegetableRepository> = _uiState.value.vegeRepositoryList
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                pagerCount = pagerCount,
                vegeRepositoryList = vegeRepositoryList
            )
        }
    }

    class ManageScreenFactory(
        private val index: Int,
        private val applicationContext: Context
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            ManageScreenViewModel(
                index,
                applicationContext
            ) as T
    }

    @OptIn(ExperimentalFoundationApi::class)
    suspend fun moveImage(index: Int) {
        _uiState.value.pagerState.animateScrollToPage(index)
    }
}
