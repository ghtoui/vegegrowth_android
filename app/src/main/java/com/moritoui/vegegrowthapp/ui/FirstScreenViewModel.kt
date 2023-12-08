package com.moritoui.vegegrowthapp.ui

import androidx.lifecycle.ViewModel
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.SortStatus
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus
import com.moritoui.vegegrowthapp.usecases.AddVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.DeleteVegeItemUseCase
import com.moritoui.vegegrowthapp.usecases.GetVegeItemListUseCase
import com.moritoui.vegegrowthapp.usecases.SaveVegeItemListUseCase
import com.moritoui.vegegrowthapp.usecases.SetSelectSortStatusUseCase
import com.moritoui.vegegrowthapp.usecases.SetSelectedIndexUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class FirstScreenUiState(
    val isOpenAddDialog: Boolean = false,
    val inputText: String = "",
    val selectCategory: VegeCategory = VegeCategory.None,
    val isAddAble: Boolean = false,
    val selectMenu: SelectMenu = SelectMenu.None,
    val selectStatus: VegeStatus = VegeStatus.Default,
    val sortStatus: SortStatus = SortStatus.All
)

@HiltViewModel
class FirstScreenViewModel @Inject constructor(
    private val addVegeItemUseCase: AddVegeItemUseCase,
    private val deleteVegeItemUseCase: DeleteVegeItemUseCase,
    private val getVegeItemListUseCase: GetVegeItemListUseCase,
    private val setSelectedIndexUseCase: SetSelectedIndexUseCase,
    private val setSelectSortStatusUseCase: SetSelectSortStatusUseCase,
    private val saveVegeItemListUseCase: SaveVegeItemListUseCase
) : ViewModel() {
    private var deleteList: MutableList<VegeItem> = mutableListOf()

    private val _uiState = MutableStateFlow(FirstScreenUiState())
    val uiState: StateFlow<FirstScreenUiState> = _uiState.asStateFlow()

    private var _vegeItemList: MutableList<VegeItem> = getVegeItemListUseCase()
    val vegeItemList: MutableList<VegeItem>
        get() = _vegeItemList

    private fun updateState(
        isOpenAddDialog: Boolean = _uiState.value.isOpenAddDialog,
        inputText: String = _uiState.value.inputText,
        selectCategory: VegeCategory = _uiState.value.selectCategory,
        isAddAble: Boolean = _uiState.value.isAddAble,
        selectMenu: SelectMenu = _uiState.value.selectMenu,
        sortStatus: SortStatus = _uiState.value.sortStatus
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                isOpenAddDialog = isOpenAddDialog,
                inputText = inputText,
                selectCategory = selectCategory,
                isAddAble = isAddAble,
                selectMenu = selectMenu,
                sortStatus = sortStatus
            )
        }
        setSelectSortStatusUseCase(sortStatus)
        updateVegeItemList()
    }

    private fun updateVegeItemList() {
        _vegeItemList = getVegeItemListUseCase()
    }

    fun selectedIndex(index: Int) {
        setSelectedIndexUseCase(index)
    }

    fun closeDialog() {
        updateState(
            isOpenAddDialog = false
        )
    }

    fun openAddDialog() {
        updateState(
            isOpenAddDialog = true,
            inputText = "",
            selectCategory = VegeCategory.None
        )
    }

    fun selectStatus() {
        saveVegeItemListUseCase()
    }

    fun changeInputText(inputText: String) {
        val isAddAble = checkInputText(inputText = inputText)
        updateState(
            inputText = inputText,
            isAddAble = isAddAble
        )
    }

    fun saveVegeItemListData() {
        val vegeItem = VegeItem(
            name = _uiState.value.inputText,
            category = _uiState.value.selectCategory,
            uuid = UUID.randomUUID().toString(),
            status = VegeStatus.Default
        )
        addVegeItemUseCase(vegeItem)
        closeDialog()
    }

    fun changeDeleteMode() {
        if (_uiState.value.selectMenu == SelectMenu.Delete) {
            updateState(selectMenu = SelectMenu.None)
            deleteItemList()
        } else {
            updateState(selectMenu = SelectMenu.Delete)
        }
    }

    fun changeEditMode() {
        if (_uiState.value.selectMenu == SelectMenu.Edit) {
            updateState(selectMenu = SelectMenu.None)
        } else {
            updateState(selectMenu = SelectMenu.Edit)
        }
    }

    private fun deleteItemList() {
        deleteVegeItemUseCase(deleteList)
        deleteList = mutableListOf()
    }

    fun deleteItem(item: VegeItem, isDelete: Boolean) {
        if (!isDelete) {
            deleteList.add(item)
        } else {
            deleteList.remove(item)
        }
    }

    fun cancelMenu() {
        deleteList = mutableListOf()
        updateState(selectMenu = SelectMenu.None)
    }

    fun selectCategory(selectCategory: VegeCategory) {
        updateState(selectCategory = selectCategory)
    }

    fun setSortItemList(sortStatus: SortStatus) {
        setSelectSortStatusUseCase(sortStatus = sortStatus)
        updateState(sortStatus = sortStatus)
    }

    private fun checkInputText(inputText: String): Boolean {
        return when (inputText) {
            "" -> false
            else -> true
        }
    }
}
