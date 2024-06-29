package com.moritoui.vegegrowthapp.ui.folder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moritoui.vegegrowthapp.model.FilterStatus
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.filterStatusMap
import com.moritoui.vegegrowthapp.ui.folder.model.FolderScreenUiState
import com.moritoui.vegegrowthapp.ui.folder.model.VegetablesState
import com.moritoui.vegegrowthapp.usecases.GetVegeItemDetailLastUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemFromFolderIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getVegetableFromFolderIdUseCase: GetVegeItemFromFolderIdUseCase,
    private val getVegeItemDetailLastUseCase: GetVegeItemDetailLastUseCase,
) : ViewModel() {
    private val args = checkNotNull(savedStateHandle.get<Int>("vegetableId"))

    private val _uiState = MutableStateFlow(FolderScreenUiState.initial())
    val uiState: StateFlow<FolderScreenUiState> = _uiState.asStateFlow()

    private val _vegetables = MutableStateFlow<List<VegeItem>>(emptyList())
    private val _vegetableDetails = MutableStateFlow<List<VegeItemDetail?>>(emptyList())
    val vegetablesState: StateFlow<VegetablesState> = combine(
        _vegetables,
        _vegetableDetails
    ) { vegetables, vegetableDetails ->
        VegetablesState(
            vegetables = vegetables,
            vegetableDetails = vegetableDetails
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        VegetablesState.initial()
    )

    /**
     * 登録されている野菜のリストを更新する
     */
    private fun reloadVegetables() {
        viewModelScope.launch {
            val filterStatus = _uiState.value.filterStatus
            val filteredVegetables = getVegetableFromFolderIdUseCase(args).filter { item ->
                if (filterStatus == FilterStatus.All) {
                    true
                } else {
                    item.status == filterStatusMap[filterStatus] ||
                            item.category == filterStatusMap[filterStatus]
                }
            }

            _vegetables.update {
                filteredVegetables
            }

            _vegetableDetails.update {
                filteredVegetables.map { reloadVegetableDetailLast(it) }
            }
        }
    }
    /**
     * 指定された野菜の最新登録情報を取得する
     */
    private suspend fun reloadVegetableDetailLast(vegeItem: VegeItem): VegeItemDetail? = getVegeItemDetailLastUseCase(vegeItem.id)

    /**
     * uiStateの変更を監視する
     */
    private fun monitorUiState() {
        _uiState.onEach {
            _uiState.update {
                it.copy(isLoading = true)
            }
            reloadVegetables()
            _uiState.update {
                it.copy(isLoading = false)
            }
        }.launchIn(viewModelScope)
    }
}
