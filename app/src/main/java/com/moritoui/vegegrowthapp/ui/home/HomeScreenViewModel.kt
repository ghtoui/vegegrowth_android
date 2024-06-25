package com.moritoui.vegegrowthapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moritoui.vegegrowthapp.model.FilterStatus
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeItemDetail
import com.moritoui.vegegrowthapp.model.VegeStatus
import com.moritoui.vegegrowthapp.model.filterStatusMap
import com.moritoui.vegegrowthapp.repository.datamigration.DataMigrationRepository
import com.moritoui.vegegrowthapp.ui.home.model.HomeScreenUiState
import com.moritoui.vegegrowthapp.ui.home.model.HomeVegetablesState
import com.moritoui.vegegrowthapp.usecases.AddVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.ChangeVegeItemStatusUseCase
import com.moritoui.vegegrowthapp.usecases.DeleteVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemDetailLastUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
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
class HomeScreenViewModel @Inject constructor(
    private val addVegeItemUseCase: AddVegeItemUseCase,
    private val deleteVegeItemUseCase: DeleteVegeItemUseCase,
    private val getVegeItemListUseCase: GetVegeItemListUseCase,
    private val changeVegeItemStatusUseCase: ChangeVegeItemStatusUseCase,
    private val getVegeItemDetailLast: GetVegeItemDetailLastUseCase,
    private val dataMigrationRepository: DataMigrationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState.initialState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    private val _isDataMigrating = dataMigrationRepository.isDataMigrating
    val isDataMigrating: StateFlow<Boolean> = _isDataMigrating.asStateFlow()

    private val _vegetables = MutableStateFlow<List<VegeItem>>(emptyList())
    private val _vegetableDetails = MutableStateFlow<List<VegeItemDetail?>>(emptyList())
    val vegetablesState: StateFlow<HomeVegetablesState> = combine(
        _vegetables,
        _vegetableDetails
    ) { vegetables, vegetableDetails ->
        HomeVegetablesState(
            vegetables = vegetables,
            vegetableDetails = vegetableDetails
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeVegetablesState.initial()
    )

    init {
        viewModelScope.launch {
            dataMigrationRepository.dataMigration()
        }
        monitorUiState()
    }

    fun closeDialog() {
        _uiState.update {
            it.copy(
                isOpenAddDialog = false
            )
        }
    }

    fun openAddDialog() {
        _uiState.update {
            it.copy(
                isOpenAddDialog = true,
                inputText = "",
                selectCategory = VegeCategory.None
            )
        }
    }

    fun selectStatus(vegeItem: VegeItem) {
        viewModelScope.launch {
            changeVegeItemStatusUseCase(vegeItem)
            _vegetables.value.map { old ->
                if (old.id == vegeItem.id) {
                    vegeItem
                } else {
                    old
                }
            }
            reloadVegetables()
        }
    }

    fun changeInputText(inputText: String) {
        val isAddAble = checkInputText(inputText = inputText)
        _uiState.update {
            it.copy(
                inputText = inputText,
                isAddAble = isAddAble
            )
        }
    }

    fun saveVegeItem() {
        val vegeItem =
            VegeItem(
                name = _uiState.value.inputText,
                category = _uiState.value.selectCategory,
                uuid = UUID.randomUUID().toString(),
                status = VegeStatus.Default
            )
        viewModelScope.launch {
            addVegeItemUseCase(vegeItem)
            reloadVegetables()
            closeDialog()
        }
    }

    fun changeDeleteMode() {
        _uiState.update {
            it.copy(
                selectMenu =
                if (_uiState.value.selectMenu == SelectMenu.Delete) {
                    SelectMenu.None
                } else {
                    SelectMenu.Delete
                }
            )
        }
    }

    fun changeEditMode() {
        _uiState.update {
            it.copy(
                selectMenu =
                if (_uiState.value.selectMenu == SelectMenu.Edit) {
                    SelectMenu.None
                } else {
                    SelectMenu.Edit
                }
            )
        }
    }

    fun deleteItem() {
        val deleteItem = _uiState.value.targetDeleteItem ?: return
        viewModelScope.launch {
            deleteVegeItemUseCase(deleteItem)
            _uiState.update {
                it.copy(targetDeleteItem = null)
            }
        }
    }

    fun onCancelMenuClick() {
        _uiState.update {
            it.copy(
                selectMenu = SelectMenu.None
            )
        }
    }

    fun selectCategory(selectCategory: VegeCategory) {
        _uiState.update {
            it.copy(
                selectCategory = selectCategory
            )
        }
    }

    // 現在選択されている
    fun setFilterItemList(filterStatus: FilterStatus) {
        _uiState.update {
            it.copy(
                filterStatus = filterStatus
            )
        }
    }

    /**
     * 削除ダイアログを閉じる
     */
    fun closeDeleteDialog() {
        _uiState.update {
            it.copy(
                isOpenDeleteDialog = !it.isOpenDeleteDialog
            )
        }
    }

    /**
     * 削除ダイアログを開いて，削除するものをセットする
     */
    fun openDeleteDialog(vegeItem: VegeItem) {
        _uiState.update {
            it.copy(
                isOpenDeleteDialog = true,
                targetDeleteItem = vegeItem
            )
        }
    }

    private fun checkInputText(inputText: String): Boolean = when (inputText) {
        "" -> false
        else -> true
    }

    /**
     * 登録されている野菜のリストを更新する
     */
    private fun reloadVegetables() {
        viewModelScope.launch {
            val filterStatus = _uiState.value.filterStatus
            val filteredVegetables = getVegeItemListUseCase().filter { item ->
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
    private suspend fun reloadVegetableDetailLast(vegeItem: VegeItem): VegeItemDetail? = getVegeItemDetailLast(vegeItem.id)

    /**
     * uiStateの変更を監視する
     */
    private fun monitorUiState() {
        _uiState.onEach {
            reloadVegetables()
        }.launchIn(viewModelScope)
    }
}
