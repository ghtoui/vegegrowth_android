package com.moritoui.vegegrowthapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moritoui.vegegrowthapp.model.FilterStatus
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus
import com.moritoui.vegegrowthapp.model.filterStatusMap
import com.moritoui.vegegrowthapp.repository.datamigration.DataMigrationRepository
import com.moritoui.vegegrowthapp.ui.home.model.HomeScreenUiState
import com.moritoui.vegegrowthapp.usecases.AddVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.ChangeVegeItemStatusUseCase
import com.moritoui.vegegrowthapp.usecases.DeleteVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel
@Inject
constructor(
    private val addVegeItemUseCase: AddVegeItemUseCase,
    private val deleteVegeItemUseCase: DeleteVegeItemUseCase,
    private val getVegeItemListUseCase: GetVegeItemListUseCase,
    private val changeVegeItemStatusUseCase: ChangeVegeItemStatusUseCase,
    private val dataMigrationRepository: DataMigrationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState.initialState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    private val _isDataMigrating = dataMigrationRepository.isDataMigrating
    val isDataMigrating: StateFlow<Boolean> = _isDataMigrating.asStateFlow()

    init {
        viewModelScope.launch {
            dataMigrationRepository.dataMigration()
            _uiState.update {
                it.copy(
                    vegetables = getVegeItemListUseCase()
                )
            }
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
        }
        _uiState.value.vegetables.map { old ->
            if (old.id == vegeItem.id) {
                vegeItem
            } else {
                old
            }
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

    fun deleteItem(vegeItem: VegeItem) {
        viewModelScope.launch {
            deleteVegeItemUseCase(vegeItem)
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

    private fun checkInputText(inputText: String): Boolean = when (inputText) {
        "" -> false
        else -> true
    }

    private fun reloadVegetables() {
        viewModelScope.launch {
            val filterStatus = _uiState.value.filterStatus
            val filteredVegetables =
                getVegeItemListUseCase().filter { item ->
                    if (filterStatus == FilterStatus.All) {
                        true
                    } else {
                        item.status == filterStatusMap[filterStatus] ||
                            item.category == filterStatusMap[filterStatus]
                    }
                }
            _uiState.update {
                it.copy(
                    vegetables = filteredVegetables
                )
            }
        }
    }

    /**
     * uiStateの変更を監視する
     */
    private fun monitorUiState() {
        _uiState.onEach {
            reloadVegetables()
        }.launchIn(viewModelScope)
    }
}
