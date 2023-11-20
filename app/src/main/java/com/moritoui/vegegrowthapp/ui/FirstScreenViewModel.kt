package com.moritoui.vegegrowthapp.ui

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.moritoui.vegegrowthapp.di.FirstScreenUiState
import com.moritoui.vegegrowthapp.model.FileManagerImpl
import com.moritoui.vegegrowthapp.model.SelectMenu
import com.moritoui.vegegrowthapp.model.SortStatus
import com.moritoui.vegegrowthapp.model.VegeCategory
import com.moritoui.vegegrowthapp.model.VegeItem
import com.moritoui.vegegrowthapp.model.VegeStatus
import com.moritoui.vegegrowthapp.model.sortStatusMap
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FirstScreenViewModel @Inject constructor(
    private val fileManger: FileManagerImpl
) : ViewModel() {
    private var deleteList: MutableList<VegeItem> = mutableListOf()

    private val _uiState = MutableStateFlow(FirstScreenUiState())
    val uiState: StateFlow<FirstScreenUiState> = _uiState.asStateFlow()

    private var _vegeItemList: MutableList<VegeItem>
    private var sortItemList: MutableList<VegeItem>
    val vegeItemList: MutableList<VegeItem>
        get() = sortItemList

    init {
        this._vegeItemList = fileManger.getVegeItemList().toMutableStateList()
        this.sortItemList = _vegeItemList
    }

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
        sortItemList = sortList(
            sortStatus = _uiState.value.sortStatus,
            itemList = _vegeItemList
        )
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
        fileManger.saveVegeItemListData(vegeItemList = _vegeItemList)
    }

    fun changeInputText(inputText: String) {
        val isAddAble = checkInputText(inputText = inputText)
        updateState(
            inputText = inputText,
            isAddAble = isAddAble
        )
    }

    fun saveVegeItemListData() {
        _vegeItemList.add(
            VegeItem(
                name = _uiState.value.inputText,
                category = _uiState.value.selectCategory,
                uuid = UUID.randomUUID().toString(),
                status = VegeStatus.Default
            )
        )
        fileManger.saveVegeItemListData(vegeItemList = _vegeItemList)
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
        deleteList.forEach { item ->
            _vegeItemList.remove(item)
        }
        deleteList = mutableListOf()
        fileManger.saveVegeItemListData(vegeItemList = vegeItemList)
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
        updateState(sortStatus = sortStatus)
    }

    private fun checkInputText(inputText: String): Boolean {
        return when (inputText) {
            "" -> false
            else -> true
        }
    }

    private fun sortList(sortStatus: SortStatus, itemList: List<VegeItem>): MutableList<VegeItem> {
        return when (sortStatus) {
            SortStatus.All -> itemList.toMutableList()
            else -> {
                itemList.filter { item ->
                    item.status == sortStatusMap[sortStatus] || item.category == sortStatusMap[sortStatus]
                }.toMutableList()
            }
        }
    }
}
